import models.Account;
import models.CheckingAccount;
import models.SavingsAccount;
import models.exceptions.AccountCreationException;
import services.AccountManager;
import models.Customer;
import models.PremiumCustomer;
import models.RegularCustomer;
import models.exceptions.AccountNotFound;
import models.exceptions.InsufficientFundsException;
import models.exceptions.InvalidDepositException;
import Screens.Menu;
import Screens.Prompt;
import models.Transaction;
import services.TransactionManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AccountManager accountManager = new AccountManager();
        TransactionManager transactionManager = new TransactionManager();

        while (true) {
            int menuResponse = new Menu(scanner, "BANK ACCOUNT MANAGEMENT - MAIN MENU", new String[]{
                    "Create Account",
                    "Manage Accounts",
                    "Perform Transactions",
                    "Generate Account Statements",
                    "Run Tests",
                    "Exit"
            }).openScreen();

            switch (menuResponse) {
                case 1: {
                    String name;
                    int age;
                    String contact;
                    String address;

                    try {
                        name = new Prompt(scanner, "ACCOUNT CREATION", "Enter customer name").openScreen();
                        age = Integer.parseInt(new Prompt(scanner, null, "Enter customer age").openScreen());
                        contact = new Prompt(scanner, null, "Enter customer contact").openScreen();
                        address = new Prompt(scanner, null, "Enter customer address").openScreen();
                    } catch (NumberFormatException nfe) {
                        System.out.println(nfe.getMessage());
                        continue;
                    }

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
                            System.out.println("Invalid input\n");
                            continue;
                        }
                    }

                    int accountType = new Menu(scanner, "Account Type", new String[]{
                            "Savings Account",
                            "Checking Account"
                    }).openScreen();

                    double initialDeposit;

                    try {
                        initialDeposit = Double.parseDouble(new Prompt(scanner, null, "Enter initial deposit amount").openScreen());
                    } catch (NumberFormatException nfe) {
                        System.out.println(nfe.getMessage());
                        continue;
                    }

                    Account account;

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
                                System.out.println("Invalid input\n");
                                continue;
                            }
                        }
                    } catch (AccountCreationException ace) {
                        System.out.println(ace.getMessage());
                        continue;
                    }

                    Transaction transaction = new Transaction(account.getAccountNumber(), "DEPOSIT", initialDeposit, initialDeposit);
                    transactionManager.addTransaction(transaction);

                    accountManager.addAccount(account);
                    System.out.println("Account Created Successfully\n");
                    account.displayAccountDetails();
                    System.out.print("Press Enter to Continue...");
                    scanner.nextLine();

                    continue;
                }
                case 2: {
                    accountManager.viewAllAccounts();
                    System.out.print("Press Enter to Continue...");
                    scanner.nextLine();

                    continue;
                }
                case 3: {
                    Account account;

                    try {
                        account = accountManager.findAccount(new Prompt(scanner, "Process Transaction", "Enter Account Number").openScreen());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
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
                            System.out.println("Invalid input\n");
                            continue;
                        }
                    }

                    double amount;

                    try {
                        amount = Double.parseDouble((new Prompt(scanner, null, "Enter Amount: ")).openScreen());
                    } catch (NumberFormatException nfe) {
                        System.out.println(nfe.getMessage());
                        continue;
                    } catch (Exception e) {
                        System.out.println("Invalid input\n");
                        continue;
                    }


                    boolean isWithdrawal = transactionType.equals("WITHDRAWAL");

                    double newBalance = account.getBalance() + (isWithdrawal ? -amount : amount);

                    Transaction transaction = new Transaction(account.getAccountNumber(), transactionType, amount, newBalance);

                    System.out.println("""
                            TRANSACTION CONFIRMATION
                            ----------------------------------------
                            """);
                    transaction.displayTransactionDetails();

                    String confirm = new Prompt(scanner, null, "Confirm Transaction? (Y/N)").openScreen();

                    if (!confirm.equalsIgnoreCase("Y")) continue;

                    if (isWithdrawal) {
                        try {
                            if (account.getAccountType().equals("Checking Account")) {
                                ((CheckingAccount) account).withdraw(amount);
                            } else {
                                ((SavingsAccount) account).withdraw(amount);
                            }
                        } catch (InsufficientFundsException ife) {
                            System.out.println(ife.getMessage());
                            continue;
                        }
                    } else {
                        try {
                            if (account.getAccountType().equals("Checking Account")) {
                                ((CheckingAccount) account).deposit(amount);
                            } else {
                                ((SavingsAccount) account).deposit(amount);
                            }
                        } catch (InvalidDepositException id) {
                            continue;
                        }
                    }

                    transactionManager.addTransaction(transaction);

                    System.out.println("Transaction added successfully\n");

                    System.out.print("Press Enter to Continue...");
                    scanner.nextLine();

                    continue;
                }
                case 4: {
                    Account account;

                    try {
                        account = accountManager.findAccount(new Prompt(scanner, "GENERATE ACCOUNT STATEMENT", "Enter Account Number").openScreen());
                    } catch (AccountNotFound anf) {
                        System.out.println(anf.getMessage());
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

                    System.out.print("Press Enter to Continue...");
                    scanner.nextLine();

                    continue;
                }
                case 5: {

                }
                case 6: {
                    System.out.print("Thank you for using the Bank Account Management System!\n");
                    break;
                }
                default: {
                    System.out.println("Invalid input\n");
                    continue;
                }
            }

            break;
        }
    }
}