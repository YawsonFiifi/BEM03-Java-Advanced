package Utils;

import models.Account;
import models.Transaction;
import models.exceptions.InsufficientFundsException;
import models.exceptions.InvalidDepositException;
import services.AccountManager;
import services.TransactionManager;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Simulates concurrent transactions using threads and parallel streams
 * Demonstrates thread safety with synchronized methods
 */
public class ConcurrencySimulator {
    private final TransactionManager transactionManager;
    private final Random random = new Random();

    public ConcurrencySimulator(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void simulateConcurrentTransactions(Account account, int numTransactions) {
        try {
            double initialBalance = account.getBalance();

            simulateWithThreads(account, numTransactions);

            System.out.println("\n=== SIMULATION COMPLETE ===");
            System.out.printf("Initial Balance: $%.2f%n", initialBalance);
            System.out.printf("Final Balance: $%.2f%n", account.getBalance());
            System.out.println("===========================\n");

        } catch (Exception e) {
            System.err.println("Error during simulation: " + e.getMessage());
        }
    }

    // Simulate using traditional Thread approach
    private void simulateWithThreads(Account account, int numTransactions) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(numTransactions);

        for (int i = 0; i < numTransactions; i++) {
            final int transactionNum = i + 1;
            Thread thread = new Thread(() -> {
                try {
                    performRandomTransaction(account);
                } finally {
                    latch.countDown();
                }
            });
            thread.setName("Transaction-Thread-" + transactionNum);
            threads.add(thread);
        }

        // Start all threads
        threads.forEach(Thread::start);

        // Wait for all threads to complete
        latch.await();
    }

    // Perform a random transaction (deposit or withdrawal) in a thread-safe manner
    // This method demonstrates synchronized access to prevent race conditions
    private synchronized void performRandomTransaction(Account account) {
        try {
            boolean isDeposit = random.nextBoolean();
            double amount = 50 + random.nextDouble() * 200; // Random amount between $50-$250
            amount = Math.round(amount * 100.0) / 100.0; // Round to 2 decimal places

            String transactionType;
            double previousBalance = account.getBalance();

            // Log the attempt
            String threadName = Thread.currentThread().getName();
            System.out.printf("[%s] Attempting %s of $%.2f (Balance: $%.2f)%n",
                    threadName,
                    isDeposit ? "DEPOSIT" : "WITHDRAWAL",
                    amount,
                    previousBalance
            );

            if (isDeposit) {
                account.deposit(amount);
                transactionType = "DEPOSIT";
            } else {
                // Try to withdraw, but handle insufficient funds gracefully
                try {
                    account.withdraw(amount);
                    transactionType = "WITHDRAWAL";
                } catch (InsufficientFundsException e) {
                    System.out.printf("[%s] ⚠ Withdrawal failed: %s%n", threadName, e.getMessage());
                    return; // Skip this transaction
                }
            }

            double newBalance = account.getBalance();

            // Create and record transaction
            Transaction transaction = new Transaction(
                    account.getAccountNumber(),
                    transactionType,
                    amount,
                    newBalance
            );

            // Add to transaction manager (also synchronized)
            transactionManager.addTransaction(transaction);

            System.out.printf("[%s] ✓ %s successful: $%.2f → $%.2f%n",
                    threadName,
                    transactionType,
                    previousBalance,
                    newBalance
            );

            // Simulate some processing time
            Thread.sleep(random.nextInt(50));

        } catch (InvalidDepositException e) {
            System.err.printf("[%s] Deposit error: %s%n", Thread.currentThread().getName(), e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("[%s] Thread interrupted%n", Thread.currentThread().getName());
        } catch (Exception e) {
            System.err.printf("[%s] Unexpected error: %s%n", Thread.currentThread().getName(), e.getMessage());
        }
    }
}
