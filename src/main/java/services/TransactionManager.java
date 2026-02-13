package services;

import models.Transaction;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionManager {
    private final Map<String, List<Transaction>> transactions = Collections.synchronizedMap(new HashMap<>());
    private final FileManager fileManager;

    public TransactionManager() {
        this.fileManager = new FileManager();
    }

    public TransactionManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public synchronized void addTransaction(Transaction transaction){
        List<Transaction> userTransactions = transactions.computeIfAbsent(
                transaction.getAccountNumber(), _ -> Collections.synchronizedList(new ArrayList<>())
        );

        userTransactions.add(transaction);
    }

    public void viewTransactionsByAccount(String accountNumber){
        List<Transaction> userTransactions = transactions.get(accountNumber.toUpperCase());

        if(userTransactions == null || userTransactions.isEmpty()){
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

        System.out.println(output);
    }

    public void loadTransactions() {
        try {
            Map<String, List<Transaction>> loadedTransactions = fileManager.loadTransactions();
            transactions.clear();
            transactions.putAll(loadedTransactions);
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }

    public void saveTransactions() {
        try {
            fileManager.saveTransactions(transactions);
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }

    public double calculateTotalDeposits(String accountNumber){
        return transactions.get(accountNumber.toUpperCase()).stream().mapToDouble(Transaction::getAmount).sum();
    }
}