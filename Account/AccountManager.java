package Account;

import CustomExceptions.AccountNotFound;

public class AccountManager {
    private final Account[] accounts = new Account[50];
    static private int accountCount = 0;

    public void addAccount(Account account) {
        accounts[accountCount++] = account;
    }

    public Account findAccount(String accountNumber) throws AccountNotFound {
        for(Account account : accounts) {
            if(account == null) break;

            if(account.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                return account;
            }
        }

        throw new AccountNotFound("Account not found: " + accountNumber);
    }

    public void viewAllAccounts(){
        StringBuilder output = new StringBuilder("""
            ACCOUNT LISTING
            ---------------------------------------------------------------
            ACC NO | CUSTOMER NAME | TYPE    | BALANCE | STATUS
            ---------------------------------------------------------------
            """
        );

        for(Account account : accounts) {
            if(account == null) break;

            output.append(String.format("""
            %s | %s | %s | $%f | %s | %s |
            ---------------------------------------------------------------
            """,
                account.getAccountNumber(),
                account.getCustomer().getName(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getAccountType().equals("Savings")
                        ?String.format(" Interest Rate: %f%c | Min Balance: $%f", ((SavingsAccount)account).getInterestRate(),'%', ((SavingsAccount)account).getMinimumBalance())
                        :String.format(" Overdraft Limit: $%f | Monthly fee: $%f", ((CheckingAccount)account).getOverdraftLimit(), ((CheckingAccount)account).getMonthlyFees())
            ));
        }

        output.append(String.format("""
            Total Accounts: %d
            Total Bank Balance: $%f
            """,
            accountCount,
            getTotalBalance()
        ));

        System.out.print(output);
    }

    public double getTotalBalance(){
        double totalBalance = 0;
        for(Account account : accounts) {
            if(account == null) break;

            totalBalance += account.getBalance();
        }
        return totalBalance;
    }

    public int getAccountCount(){
        return accountCount;
    }
}
