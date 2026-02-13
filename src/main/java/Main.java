import Constants.RegexConstants;
import Screens.Menu;
import Screens.Prompt;
import models.*;
import models.exceptions.AccountCreationException;
import models.exceptions.AccountNotFound;
import models.exceptions.InsufficientFundsException;
import models.exceptions.InvalidDepositException;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import services.AccountManager;
import services.FileManager;
import services.TransactionManager;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

void main() {
    Scanner scanner = new Scanner(System.in);

    FileManager fileManager = new FileManager();
    AccountManager accountManager = new AccountManager();
    TransactionManager transactionManager = new TransactionManager();

    if (fileManager.dataFilesExist()) {
        IO.println("\nloading data from files");
        accountManager.loadAccounts();
        transactionManager.loadTransactions();
        IO.print("Press Enter to Continue...");
        scanner.nextLine();
    }

    while (true) {
        int menuResponse = new Menu(scanner, "BANK ACCOUNT MANAGEMENT - MAIN MENU", new String[]{
                "Create Account",
                "Manage Accounts",
                "Perform Transactions",
                "Save/Load Data",
                "Generate Account Statements",
                "Run Tests",
                "Exit"
        }).openScreen();

        switch (menuResponse) {
            case 1: {
                String name = new Prompt(scanner, "ACCOUNT CREATION", "Enter customer name", RegexConstants.NAME).openScreen();
                int age = Integer.parseInt(new Prompt(scanner, null, "Enter customer age", RegexConstants.AGE).openScreen());
                String contact = new Prompt(scanner, null, "Enter customer contact", RegexConstants.PHONE_NUMBER).openScreen();
                String address = new Prompt(scanner, null, "Enter customer address", RegexConstants.ADDRESS).openScreen();
                int customerType = new Menu(scanner, "Customer Type", new String[]{
                        "Regular Customer",
                        "Premium Customer"
                }).openScreen();

                Customer customer;

                switch (customerType) {
                    case 1: {
                        customer = new RegularCustomer(name, age, contact, address);
                        break;
                    }
                    case 2: {
                        customer = new PremiumCustomer(name, age, contact, address);
                        break;
                    }
                    default: {
                        IO.println("Invalid input\n");
                        continue;
                    }
                }

                int accountType = new Menu(scanner, "Account Type", new String[]{
                        "Savings Account",
                        "Checking Account"
                }).openScreen();

                Account account;
                double initialDeposit = Double.parseDouble(new Prompt(scanner, null, "Enter initial deposit amount", RegexConstants.MONEY).openScreen().replace("$", ""));

                try {
                    switch (accountType) {
                        case 1: {
                            account = new SavingsAccount(customer, initialDeposit);
                            break;
                        }
                        case 2: {
                            account = new CheckingAccount(customer, initialDeposit);
                            break;
                        }
                        default: {
                            IO.println("Invalid input\n");
                            continue;
                        }
                    }
                } catch (AccountCreationException ace) {
                    IO.println(ace.getMessage());
                    continue;
                }

                Transaction transaction = new Transaction(account.getAccountNumber(), "DEPOSIT", initialDeposit, initialDeposit);
                transactionManager.addTransaction(transaction);

                accountManager.addAccount(account);
                IO.println("Account Created Successfully\n");
                account.displayAccountDetails();
                IO.print("Press Enter to Continue...");
                scanner.nextLine();

                continue;
            }
            case 2: {
                accountManager.viewAllAccounts();
                IO.print("Press Enter to Continue...");
                scanner.nextLine();

                continue;
            }
            case 3: {
                Account account;

                try {
                    account = accountManager.findAccount(new Prompt(scanner, "Process Transaction", "Enter Account Number", RegexConstants.ACCOUNT_ID).openScreen());
                } catch (Exception e) {
                    IO.println(e.getMessage());
                    continue;
                }

                System.out.printf("""
                                Account Details:
                                Customer: %s,
                                Account Type: %s,
                                Current Balance: $%f,
                                """,
                        account.getCustomer().getName(),
                        account.getAccountType(),
                        account.getBalance()
                );

                int transactionTypeResponse = new Menu(scanner, "Transaction Type:", new String[]{"Deposit", "Withdraw"}).openScreen();

                String transactionType;

                switch (transactionTypeResponse) {
                    case 1: {
                        transactionType = "DEPOSIT";
                        break;
                    }
                    case 2: {
                        transactionType = "WITHDRAWAL";
                        break;
                    }
                    default: {
                        IO.println("Invalid input\n");
                        continue;
                    }
                }

                double amount;

                try {
                    amount = Double.parseDouble((new Prompt(scanner, null, "Enter Amount: ", RegexConstants.MONEY)).openScreen().replace("$", ""));
                } catch (NumberFormatException nfe) {
                    IO.println(nfe.getMessage());
                    continue;
                } catch (Exception e) {
                    IO.println("Invalid input\n");
                    continue;
                }


                boolean isWithdrawal = transactionType.equals("WITHDRAWAL");

                double newBalance = account.getBalance() + (isWithdrawal ? -amount : amount);

                Transaction transaction = new Transaction(account.getAccountNumber(), transactionType, amount, newBalance);

                IO.println("""
                        TRANSACTION CONFIRMATION
                        ----------------------------------------
                        """);
                transaction.displayTransactionDetails();

                String confirm = new Prompt(scanner, null, "Confirm Transaction? (Y/N)", RegexConstants.YES_OR_NO).openScreen();

                if (!(confirm.equalsIgnoreCase("Y") || confirm.equalsIgnoreCase("yes"))) continue;

                if (isWithdrawal) {
                    try {
                        account.withdraw(amount);
                    } catch (InsufficientFundsException ife) {
                        IO.println(ife.getMessage());
                        continue;
                    }
                } else {
                    try {
                        account.deposit(amount);
                    } catch (InvalidDepositException id) {
                        continue;
                    }
                }

                transactionManager.addTransaction(transaction);

                IO.println("Transaction added successfully\n");

                IO.print("Press Enter to Continue...");
                scanner.nextLine();

                continue;
            }
            case 4: {
                // SAVE/LOAD DATA
                int dataMenuResponse = new Menu(scanner, "DATA PERSISTENCE", new String[]{
                        "Save All Data",
                        "Load All Data"
                }).openScreen();

                switch (dataMenuResponse) {
                    case 1: {
                        IO.println("\n=== SAVING ALL DATA ===");
                        accountManager.saveAccounts();
                        transactionManager.saveTransactions();
                        System.out.println("✓ file save completed successfully\n");

                        break;
                    }
                    case 2: {
                        IO.println("\n=== LOADING ALL DATA ===");
                        accountManager.loadAccounts();
                        transactionManager.loadTransactions();
                        IO.println("✓ file load completed successfully\n");

                        break;
                    }
                    default: {
                        IO.println("Invalid input\n");
                        break;
                    }
                }

                IO.print("Press Enter to Continue...");
                scanner.nextLine();
                continue;
            }
            case 5: {
                Account account;

                try {
                    account = accountManager.findAccount(new Prompt(scanner, "GENERATE ACCOUNT STATEMENT", "Enter Account Number", RegexConstants.ACCOUNT_ID).openScreen());
                } catch (AccountNotFound anf) {
                    IO.println(anf.getMessage());
                    continue;
                }

                System.out.printf("""
                                Account: %s (%s),
                                Current Balance: $%f,
                                """,
                        account.getCustomer().getName(),
                        account.getAccountType(),
                        account.getBalance()
                );

                transactionManager.viewTransactionsByAccount(account.getAccountNumber());

                IO.print("Press Enter to Continue...");
                scanner.nextLine();

                continue;
            }
            case 6: {
                IO.println("\n--- RUNNING SYSTEM TESTS (JUnit 5) ---");

                // 1. Create a request to discover tests in your project package
                LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                        .selectors(selectPackage("java")) // Change "tests" to the package where your JUnit files live
                        .build();

                Launcher launcher = LauncherFactory.create();
                SummaryGeneratingListener listener = new SummaryGeneratingListener();

                // 2. Register a listener to capture results
                launcher.registerTestExecutionListeners(listener);

                // 3. Execute the tests
                launcher.execute(request);

                // 4. Display the results to the console
                TestExecutionSummary summary = listener.getSummary();

                IO.println("\n================ TEST SUMMARY ================");
                IO.println("Tests Found:     " + summary.getTestsFoundCount());
                IO.println("Tests Succeeded: " + summary.getTestsSucceededCount());
                IO.println("Tests Failed:    " + summary.getTestsFailedCount());

                if (summary.getTestsFailedCount() > 0) {
                    IO.println("\n--- FAILURE DETAILS ---");
                    summary.getFailures().forEach(failure -> {
                        IO.println("Test: " + failure.getTestIdentifier().getDisplayName());
                        IO.println("Reason: " + failure.getException().getMessage());
                    });
                }
                IO.println("==============================================");

                IO.print("Press Enter to return to Main Menu...");
                scanner.nextLine();
                continue;
            }
            case 7: {
                accountManager.saveAccounts();
                transactionManager.saveTransactions();
                IO.println("Data saved successfully!");

                IO.print("\nThank you for using the Bank Account Management System!\n");
                break;
            }
            default: {
                IO.println("Invalid input\n");
                continue;
            }
        }

        break;
    }
}