package models;

import models.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Customer premiumCustomer;
    private Customer regularCustomer;

    @BeforeEach
    public void setUp() {
        // Reset account counter before each test
        Account.accountCounter = 0;

        regularCustomer = new RegularCustomer("Fiifi Yawson", 22, "0506525792", "Accra");
        premiumCustomer = new PremiumCustomer("Jane Smith", 30, "0111122234", "Obuasi");
    }

    @Test
    public void testDepositUpdatesBalance() throws Exception {
        SavingsAccount account = new SavingsAccount(regularCustomer, 1000);
        double initialBalance = account.getBalance();

        account.deposit(500);

        assertEquals(initialBalance + 500, account.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Test Withdraw Below Minimum Exception")
    public void testWithdrawBelowMinimumThrowsException() throws Exception {
        SavingsAccount account = new SavingsAccount(regularCustomer, 1000);

        // Minimum balance is 500, current balance is 1000
        // Trying to withdraw 600 would leave 400, which is below minimum
        assertThrows(InsufficientFundsException.class, () -> {
            account.withdraw(600);
        });
    }

    @Test
    @DisplayName("Test Overdraft Limit Allowed")
    public void testOverdraftWithinLimitAllowed() throws Exception {
        CheckingAccount account = new CheckingAccount(premiumCustomer, 500);

        // Overdraft limit is 1000, so withdrawing 1200 should be allowed
        // (500 balance + 1000 overdraft = 1500 total available)
        assertDoesNotThrow(() -> {
            account.withdraw(1200);
        });

        assertEquals(-700, account.getBalance());
    }

    @Test
    @DisplayName("Test Overdraft Exceeded Exception")
    public void testOverdraftExceedThrowsException() throws Exception {
        CheckingAccount account = new CheckingAccount(premiumCustomer, 500);

        // Overdraft limit is 1000
        // Balance is 500, so max withdrawal is 1500
        // Trying to withdraw 1600 should throw exception
        assertThrows(InsufficientFundsException.class, () -> {
            account.withdraw(1600);
        });
    }
}