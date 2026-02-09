package services;

import models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private final List<Transaction> transactions = new ArrayList<Transaction>();

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    public void viewTransactionsByAccount(String accountNumber){
        StringBuilder output = new StringBuilder("""
            TRANSACTIONS
            --------------------------------------------------------------------------
            TRANS ID  | TYPE          | AMOUNT        | BALANCE AFTER
            --------------------------------------------------------------------------
            """);

        int counter = 0;
        double netChange = 0;

        for(Transaction transaction : transactions){
            if(transaction == null) break;

            if(transaction.getAccountNumber().equals(accountNumber)){
                boolean isWithdrawal = transaction.getType().equals("WITHDRAWAL");
                netChange = netChange + (isWithdrawal ? -transaction.getAmount() : transaction.getAmount());

                output.append(String.format("%-9s | %-13s | %c$%-11.2f | $%.2f%n",
                        transaction.getTransactionId(),
                        transaction.getType(),
                        isWithdrawal ? '-' : '+',
                        transaction.getAmount(),
                        transaction.getBalanceAfter()
                ));

                counter++;
            }
        }

        output.append("--------------------------------------------------------------------------\n");
        output.append(String.format("Net Change: %c$%.2f%n",
                netChange < 0 ? '-' : '+',
                Math.abs(netChange)
        ));

        System.out.println(counter == 0 ? """
    --------------------------------------------------------------------------
    No transactions recorded for this account.
    --------------------------------------------------------------------------
    """ : output.toString());
    }

    public double calculateTotalDeposits(String accountNumber){
        double totalDeposits = 0;

        for(Transaction transaction : transactions){
            if(transaction == null) break;

            if(transaction.getAccountNumber().equalsIgnoreCase(accountNumber) && transaction.getType().equalsIgnoreCase("DEPOSIT")){
                totalDeposits += transaction.getAmount();
            }
        }

        return totalDeposits;
    }

    public double calculateTotalWithdrawals(String accountNumber){
        double totalWithdrawals = 0;

        for(Transaction transaction : transactions){
            if(transaction == null) break;

            if(transaction.getAccountNumber().equals(accountNumber) && transaction.getType().equals("WITHDRAWAL")){
                totalWithdrawals += transaction.getAmount();
            }
        }
        return totalWithdrawals;
    }
}
