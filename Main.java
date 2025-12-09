import Account.Account;
import Account.CheckingAccount;
import Account.SavingsAccount;
import Account.AccountManager;
import Customer.Customer;
import Customer.PremiumCustomer;
import Customer.RegularCustomer;
import Screens.Menu;
import Screens.Prompt;
import Transaction.Transaction;
import Transaction.TransactionManager;


void main() {
    Scanner scanner = new Scanner(System.in);

    AccountManager accountManager = new AccountManager();
    TransactionManager transactionManager = new TransactionManager();

    while (true) {
        int menuResponse = new Menu(scanner, "BANK ACCOUNT MANAGEMENT - MAIN MENU", new String[]{
                "Create Account",
                "View Accounts",
                "Process Transaction",
                "View Transaction Details",
                "Exit"
        }).openScreen();

        switch (menuResponse) {
            case 1: {
                String name = new Prompt(scanner, "ACCOUNT CREATION", "Enter customer name").openScreen();
                int age = Integer.parseInt(new Prompt(scanner, null, "Enter customer age").openScreen());
                String contact = new Prompt(scanner, null, "Enter customer contact").openScreen();
                String address = new Prompt(scanner, null, "Enter customer address").openScreen();

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

                double initialDeposit = Double.parseDouble(new Prompt(scanner, null, "Enter initial deposit amount").openScreen());

                if(customer.getCustomerType().equals("Premium Customer") && initialDeposit < 10000) {
                    System.out.println("Minimum balance should be $10,000 or more for Premium Customers");
                    continue;
                }

                Account account;

                switch (accountType) {
                    case 1: {
                        account = new SavingsAccount(customer, initialDeposit);
                        break;
                    }
                    case 2: {
                        if(initialDeposit < 500){
                            System.out.println("Initial deposit amount must be greater than 500");
                            continue;
                        }

                        account = new CheckingAccount(customer, initialDeposit);
                        break;
                    }
                    default: {
                        System.out.println("Invalid input\n");
                        continue;
                    }
                }

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
                Account account = accountManager.findAccount(new Prompt(scanner, "Process Transaction", "Enter Account Number").openScreen());

                if(account == null) {
                    System.out.println("Account not found\n");

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

                int transactionTypeResponse = new Menu(scanner, "Transaction Type:",  new String[]{"Deposit", "Withdraw"}).openScreen();

                String transactionType;

                switch (transactionTypeResponse) {
                    case 1: {
                        transactionType = "DEPOSIT";
                        break;
                    }case 2: {
                        transactionType = "WITHDRAWAL";
                        break;
                    }
                    default: {
                        System.out.println("Invalid input\n");
                        continue;
                    }
                }

                double amount = Double.parseDouble((new Prompt(scanner, null, "Enter Amount: ")).openScreen());

                boolean isWithdrawal = transactionType.equals("WITHDRAWAL");

                double newBalance = isWithdrawal
                        ? account.getBalance() - amount
                        : account.getBalance() + amount;

                Transaction transaction = new Transaction(account.getAccountNumber(), transactionType, amount, newBalance);

                System.out.println("""
                TRANSACTION CONFIRMATION
                ----------------------------------------
                """);
                transaction.displayTransactionDetails();

                String confirm = new Prompt(scanner,null,"Confirm Transaction? (Y/N)").openScreen();

                if(!confirm.equalsIgnoreCase("Y")) continue;

                if(isWithdrawal){
                    if(!account.withdraw(amount)) continue;
                }else{
                    account.deposit(amount);
                }

                transactionManager.addTransaction(transaction);

                System.out.println("Transaction added successfully\n");

                System.out.print("Press Enter to Continue...");
                scanner.nextLine();

                continue;
            }
            case 4:{
                Account account = accountManager.findAccount(new Prompt(scanner, "VIEW TRANSACTION HISTORY", "Enter Account Number").openScreen());

                if(account == null) {
                    System.out.println("Account not found\n");

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

                transactionManager.viewTransactionsByAccount(account.getAccountNumber());

                System.out.print("Press Enter to Continue...");
                scanner.nextLine();

                continue;
            }
            case 5: {
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