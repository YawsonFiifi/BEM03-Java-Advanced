package services;

import models.*;
import models.exceptions.AccountCreationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionManagerTest {

    TransactionManager transactionManager;
    AccountManager accountManager;

    @BeforeEach
    void setUp() {
        transactionManager = new TransactionManager();
        accountManager = new AccountManager();
    }

    @Test
    @DisplayName("Test Calculate Total Deposit")
    void calculateTotalDeposits() throws AccountCreationException {
        Customer customer = new RegularCustomer("Fiifi", 22, "01111111111", "Accra");
        Account account = new SavingsAccount(customer, 500);

        // To simulate initial deposit (implemented in main.java)
        transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 500, 500));

        accountManager.addAccount(account);

        transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 1000, 1500));

        transactionManager.viewTransactionsByAccount("ACC001");
        Assertions.assertEquals(1500, transactionManager.calculateTotalDeposits(account.getAccountNumber()));
    }
}