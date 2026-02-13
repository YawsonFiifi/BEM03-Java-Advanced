package models;
import models.exceptions.AccountCreationException;
import models.exceptions.InvalidDepositException;
import models.exceptions.InsufficientFundsException;


abstract public class Account {
    public static int accountCounter = 0;

    abstract public void displayAccountDetails();
    abstract public String getAccountType();
    
    private final String accountNumber;
    private String status = "Active";
    private Customer customer;
    private double balance;


    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    Account(Customer customer, double initialDeposit) throws AccountCreationException {
        if(customer.getCustomerType().equals("Premium Customer") && initialDeposit < 10000) {
            throw new AccountCreationException("Minimum balance should be $10,000 or more for Premium Customers");
        }
        this.accountNumber = String.format("ACC%03d", ++accountCounter);
        this.customer = customer;
        this.balance = initialDeposit;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        if(!status.equals("active") && !status.equals("inactive")) return;

        this.status = status;
    }

    public double getBalance(){
        return  balance;
    }

    public void setBalance(double amount){
        balance = amount;
    }

    public void deposit(double amount) throws InvalidDepositException {
        if(amount < 0) {
            throw new InvalidDepositException("Invalid amount, amount must be greater than 0");
        }

        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if(amount > balance) {
            throw new InsufficientFundsException("Insufficient funds. Current balance: " + balance);
        }

        balance -= amount;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public Customer getCustomer(){
        return customer;
    }
}
