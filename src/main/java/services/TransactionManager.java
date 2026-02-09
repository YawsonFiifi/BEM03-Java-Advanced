package services;

import models.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionManager {
    private final List<Transaction> transactions = new ArrayList<Transaction>();

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    public void viewTransactionsByAccount(String accountNumber){
        List<Transaction> userTransactions = transactions.stream()
                .filter(transaction ->
                        transaction.getAccountNumber().equalsIgnoreCase(accountNumber)
                ).toList();

        if(userTransactions.isEmpty()){
            System.out.println("""
                --------------------------------------------------------------------------
                No transactions recorded for this account.
                --------------------------------------------------------------------------
                """
            );

            return;
        }

        StringBuilder output = new StringBuilder("""
            TRANSACTIONS
            --------------------------------------------------------------------------
            TRANS ID  | TYPE          | AMOUNT        | BALANCE AFTER
            --------------------------------------------------------------------------
            """);

        output.append(
                userTransactions.stream()
                .map(transaction -> {
                    boolean isWithdrawal = transaction.getType().equals("WITHDRAWAL");

                    return String.format("%-9s | %-13s | %c$%-11.2f | $%.2f%n",
                            transaction.getTransactionId(),
                            transaction.getType(),
                            isWithdrawal ? '-' : '+',
                            transaction.getAmount(),
                            transaction.getBalanceAfter()
                    );
                })
                .collect(Collectors.joining())
        );

        double netChange = userTransactions.stream()
                .map(transaction ->
                        transaction.getType().equals("WITHDRAWAL") ? -transaction.getAmount() : transaction.getAmount()
                ).mapToDouble(Double::doubleValue).sum();

        output.append("--------------------------------------------------------------------------\n");
        output.append(String.format("Net Change: %c$%.2f%n",
                netChange < 0 ? '-' : '+',
                Math.abs(netChange)
        ));

        System.out.println(output.toString());
    }

    public double calculateTotalDeposits(String accountNumber){
        return transactions.stream()
            .filter(transaction ->
                        transaction.getAccountNumber().equalsIgnoreCase(accountNumber) && transaction.getType().equals("DEPOSIT")
            )
            .mapToDouble(Transaction::getAmount).sum();
    }

    public double calculateTotalWithdrawals(String accountNumber){
        return transactions.stream()
                .filter(transaction ->
                        transaction.getAccountNumber().equalsIgnoreCase(accountNumber) && transaction.getType().equals("WITHDRAWAL")
                )
                .mapToDouble(Transaction::getAmount).sum();
    }
}
