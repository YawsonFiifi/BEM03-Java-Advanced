package services;

import models.*;
import models.exceptions.AccountCreationException;
import models.exceptions.AccountNotFound;
import org.junit.jupiter.api.*;

import javax.security.auth.login.AccountNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class AccountManagerTest {

    AccountManager accountManager;

    @BeforeEach
    void setup() throws AccountCreationException {
        accountManager = new AccountManager();
    }

    @Test
    @DisplayName("Test Find Account")
    void findAccount() throws AccountNotFound, AccountCreationException {
        Customer customer = new RegularCustomer("Fiifi", 22, "01111111111", "Accra");
        Account account = new SavingsAccount(customer, 1000);

        // Throws account not found
        Assertions.assertThrows(AccountNotFound.class, () -> accountManager.findAccount("ACC001"));

        accountManager.addAccount(account);

        // Check for account being added to accounts manager
        account = accountManager.findAccount("ACC001");
        customer = account.getCustomer();

        Assertions.assertEquals(account.getAccountNumber(), "ACC001");
        Assertions.assertEquals(customer, account.getCustomer());
        Assertions.assertEquals(customer.getName(), "Fiifi");
    }

    @Test
    @DisplayName("Test Account not found")
    void accoundNotFound() throws AccountCreationException {
        Customer customer = new RegularCustomer("Fiifi", 22, "01111111111", "Accra");
        Account account = new SavingsAccount(customer, 1000);

        // Throws account not found
        Assertions.assertThrows(AccountNotFound.class, () -> accountManager.findAccount("ACC001"));
    }

    @Test
    void getTotalBalance() {
    }
}