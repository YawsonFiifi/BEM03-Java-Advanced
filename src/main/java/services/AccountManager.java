package services;

import models.exceptions.AccountNotFound;
import models.Account;
import models.CheckingAccount;
import models.SavingsAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountManager {
    private final List<Account> accounts = new ArrayList<Account>();

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public Account findAccount(String accountNumber) throws AccountNotFound {
        return accounts.stream().filter(account ->
                account.getAccountNumber().equals(accountNumber)).findFirst().orElseThrow(AccountNotFound::new);
    }

    public void viewAllAccounts(){
        StringBuilder output = new StringBuilder("""
        ACCOUNT LISTING
        ---------------------------------------------------------------
        ACC NO    | CUSTOMER NAME        | TYPE          | BALANCE       | STATUS
        ---------------------------------------------------------------
        """
        );

        String accountsDetails = accounts.stream().map(account -> {

            return String.format("%-9s | %-20s | %-13s | $%-12.2f | %s%n          | %s%n",
                    account.getAccountNumber(),
                    account.getCustomer().getName(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.getStatus(),
                    account.getAccountType().equals("Savings")
                            ? String.format("Interest Rate: %.1f%% | Min Balance: $%.2f",
                            ((SavingsAccount) account).getInterestRate(),
                            ((SavingsAccount) account).getMinimumBalance())
                            : String.format("Overdraft Limit: $%.2f | Monthly Fee: $%.2f",
                            ((CheckingAccount) account).getOverdraftLimit(),
                            ((CheckingAccount) account).getMonthlyFees())
            );

        }).collect(Collectors.joining("---------------------------------------------------------------\n"));

        output.append(accountsDetails);

        output.append(String.format("""
        %nTotal Accounts: %d
        Total Bank Balance: $%.2f
        """,
                accounts.size(),
                getTotalBalance()
        ));

        System.out.print(output);
    }

    public double getTotalBalance(){
        return accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }
}
