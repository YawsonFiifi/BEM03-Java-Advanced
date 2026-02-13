package services;

import models.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    private static final String DATA_DIR = "data";

    public FileManager() {
        // Create data directory if it doesn't exist
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectory(dataPath);
            }
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    /**
     * Save all accounts to file
     * Format: accountNumber|customerType|name|age|contact|address|accountType|balance|status|extraField1|extraField2
     */
    public void saveAccounts(Map<String, Account> accounts) throws IOException {
        Path accountsPath = Paths.get(DATA_DIR, ACCOUNTS_FILE);

        System.out.println("Accounts saving to accounts.txt");

        List<String> lines = accounts.values().stream()
                .map(account -> {
                    Customer customer = account.getCustomer();
                    StringBuilder line = new StringBuilder();

                    line.append(account.getAccountNumber()).append("|");
                    line.append(customer.getCustomerType()).append("|");
                    line.append(customer.getName()).append("|");
                    line.append(customer.getAge()).append("|");
                    line.append(customer.getContact()).append("|");
                    line.append(customer.getAddress()).append("|");
                    line.append(account.getAccountType()).append("|");
                    line.append(account.getBalance()).append("|");
                    line.append(account.getStatus()).append("|");

                    // Add account-specific fields
                    if (account.getAccountType().equals("Savings")) {
                        line.append(((SavingsAccount)account).getInterestRate()).append("|");
                        line.append(((SavingsAccount)account).getMinimumBalance());
                    } else {
                        line.append(((CheckingAccount)account).getOverdraftLimit()).append("|");
                        line.append(((CheckingAccount)account).getMonthlyFees());
                    }

                    return line.toString();
                })
                .collect(Collectors.toList());

        Files.write(accountsPath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("✓ Accounts saved to accounts.txt");
    }

    public Map<String, Account> loadAccounts() throws IOException {
        Path accountsPath = Paths.get(DATA_DIR, ACCOUNTS_FILE);

        if (!Files.exists(accountsPath)) {
            System.out.println("No accounts file found. Starting with empty account list.");
            return new HashMap<>();
        }


        Map<String, Account> accounts = new HashMap<>();

        try (Stream<String> lines = Files.lines(accountsPath)) {
            List<Account> accountList = lines
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseAccountLine)
                    .filter(Objects::nonNull)
                    .toList();

            // Convert list to map
            accountList.forEach(account ->
                    accounts.put(account.getAccountNumber(), account)
            );

            // Update static counters
            if (!accounts.isEmpty()) {
                Account.accountCounter = accountList.stream()
                        .mapToInt(a -> Integer.parseInt(a.getAccountNumber().substring(3)))
                        .max()
                        .orElse(0);

                Customer.customerCounter = accountList.stream()
                        .mapToInt(a -> Integer.parseInt(a.getCustomer().getCustomerId().substring(3)))
                        .max()
                        .orElse(0);
            }
        }

        System.out.println("✓ " + accounts.size() + " account(s) loaded successfully from accounts.txt");
        return accounts;
    }

    /**
     * Parse a single account line using method references
     */
    private Account parseAccountLine(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 9) return null;

            String accountNumber = parts[0];
            String customerType = parts[1];
            String name = parts[2];
            int age = Integer.parseInt(parts[3]);
            String contact = parts[4];
            String address = parts[5];
            String accountType = parts[6];
            double balance = Double.parseDouble(parts[7]);
            String status = parts[8];

            // Create customer
            Customer customer;
            if (customerType.equals("Premium")) {
                customer = new PremiumCustomer(name, age, contact, address);
            } else {
                customer = new RegularCustomer(name, age, contact, address);
            }

            // Create account
            Account account;
            if (accountType.equals("Savings")) {
                account = new SavingsAccount(customer, balance);
            } else {
                account = new CheckingAccount(customer, balance);
                if (parts.length >= 11) {
                    double overdraftLimit = Double.parseDouble(parts[9]);
                    ((CheckingAccount) account).setOverdraftLimit(overdraftLimit);
                }
            }

            account.setStatus(status);

            // Manually set account number to preserve original
            try {
                java.lang.reflect.Field field = Account.class.getDeclaredField("accountNumber");
                field.setAccessible(true);
                field.set(account, accountNumber);
            } catch (Exception e) {
                System.err.println("Could not restore account number: " + e.getMessage());
            }

            return account;
        } catch (Exception e) {
            System.err.println("Error parsing account line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Save all transactions to file
     * Format: transactionId|accountNumber|type|amount|balanceAfter|timestamp
     */
    public void saveTransactions(Map<String, List<Transaction>> transactions) throws IOException {
        Path transactionsPath = Paths.get(DATA_DIR, TRANSACTIONS_FILE);

        System.out.println("Transaction saving to transaction.txt");

        List<String> lines = transactions.values().stream()
                .flatMap(List::stream)
                .map(transaction -> String.format("%s|%s|%s|%f|%f|%s",
                        transaction.getTransactionId(),
                        transaction.getAccountNumber(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getBalanceAfter(),
                        transaction.getTimestamp()
                ))
                .collect(Collectors.toList());

        Files.write(transactionsPath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("✓ Transactions saved to transaction.txt");
    }

    /**
     * Load transactions from file using functional stream processing
     */
    public Map<String, List<Transaction>> loadTransactions() throws IOException {
        Path transactionsPath = Paths.get(DATA_DIR, TRANSACTIONS_FILE);

        if (!Files.exists(transactionsPath)) {
            System.out.println("No transactions file found. Starting with empty transaction list.");
            return new HashMap<>();
        }

        Map<String, List<Transaction>> transactions;

        try (Stream<String> lines = Files.lines(transactionsPath)) {
            List<Transaction> transactionList = lines
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseTransactionLine)
                    .filter(Objects::nonNull)
                    .toList();

            // Group transactions by account number
            transactions = transactionList.stream()
                    .collect(Collectors.groupingBy(
                            Transaction::getAccountNumber,
                            HashMap::new,
                            Collectors.toCollection(ArrayList::new)
                    ));

            // Update transaction counter
            if (!transactionList.isEmpty()) {
                Transaction.transactionCounter = transactionList.stream()
                        .mapToInt(t -> Integer.parseInt(t.getTransactionId().substring(3)))
                        .max()
                        .orElse(0);
            }
        }

        System.out.println("✓ " + transactions.size() + " transaction(s) loaded successfully from transaction.txt");
        return transactions;
    }

    /**
     * Parse a single transaction line
     */
    private Transaction parseTransactionLine(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 6) return null;

            String transactionId = parts[0];
            String accountNumber = parts[1];
            String type = parts[2];
            double amount = Double.parseDouble(parts[3]);
            double balanceAfter = Double.parseDouble(parts[4]);
            String timestamp = parts[5];

            Transaction transaction = new Transaction(accountNumber, type, amount, balanceAfter);

            // Manually set transaction ID and timestamp to preserve original
            try {
                java.lang.reflect.Field idField = Transaction.class.getDeclaredField("transactionId");
                idField.setAccessible(true);
                idField.set(transaction, transactionId);

                java.lang.reflect.Field tsField = Transaction.class.getDeclaredField("timestamp");
                tsField.setAccessible(true);
                tsField.set(transaction, timestamp);
            } catch (Exception e) {
                System.err.println("Could not restore transaction fields: " + e.getMessage());
            }

            return transaction;
        } catch (Exception e) {
            System.err.println("Error parsing transaction line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    public boolean dataFilesExist() {
        Path accountsPath = Paths.get(DATA_DIR, ACCOUNTS_FILE);
        Path transactionsPath = Paths.get(DATA_DIR, TRANSACTIONS_FILE);
        return Files.exists(accountsPath) || Files.exists(transactionsPath);
    }

}
