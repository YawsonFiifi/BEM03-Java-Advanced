package services;

import models.exceptions.AccountNotFound;
import models.Account;
import models.CheckingAccount;
import models.SavingsAccount;

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
        ACC NO    | CUSTOMER NAME        | TYPE          | BALANCE       | STATUS
        ---------------------------------------------------------------
        """
        );

        for(Account account : accounts) {
            if(account == null) break;

            String accountDetails = account.getAccountType().equals("Savings")
                    ? String.format("Interest Rate: %.1f%% | Min Balance: $%.2f",
                    ((SavingsAccount)account).getInterestRate(),
                    ((SavingsAccount)account).getMinimumBalance())
                    : String.format("Overdraft Limit: $%.2f | Monthly Fee: $%.2f",
                    ((CheckingAccount)account).getOverdraftLimit(),
                    ((CheckingAccount)account).getMonthlyFees());

            output.append(String.format("%-9s | %-20s | %-13s | $%-12.2f | %s%n",
                    account.getAccountNumber(),
                    account.getCustomer().getName(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.getStatus()
            ));

            output.append(String.format("          | %s%n",
                    accountDetails
            ));

            output.append("---------------------------------------------------------------\n");
        }

        output.append(String.format("""
        %nTotal Accounts: %d
        Total Bank Balance: $%.2f
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
