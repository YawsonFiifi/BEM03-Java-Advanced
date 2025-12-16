package Account;

import CustomExceptions.AccountCreationException;
import CustomExceptions.InsufficientFundsException;
import Customer.Customer;


public class SavingsAccount extends Account{
    private double interestRate;
    private double minimumBalance;

    public SavingsAccount(Customer customer, double initialDeposit) throws AccountCreationException {
        if(initialDeposit < 500){
            throw new AccountCreationException("Initial deposit amount must be greater than 500 for savings accounts. Initial deposite is "+ initialDeposit);
        }

        this.interestRate = 3.5;
        this.minimumBalance = 500;

        super(customer, initialDeposit);
    }

    
    public void displayAccountDetails(){
        System.out.printf("""
            Account Number: %s
            Customer: %s (%s)
            Account Type: %s
            Initial Balance: $%s
            Interest Rate: %f%c
            Minimum Balance: $%f
            Status: %s
        """,
                getAccountNumber(),
                getCustomer().getName(),
                getCustomer().getCustomerType(),
                getAccountType(),
                getBalance(),
                getInterestRate(),
                '%',
                getMinimumBalance(),
                getStatus()
        );
    }
    
    public String getAccountType(){
        return "Savings";
    }
    
    public void withdraw(double amount) throws InsufficientFundsException {
        if(getBalance() - amount < minimumBalance) {
            throw new InsufficientFundsException("Withdrawal limit reached, maximum withdrawal amount is " + (getBalance() - minimumBalance));
        };

        setBalance(getBalance() - amount);
    } 

    public double calculateInterest(){
        return interestRate * getBalance();
    }

    public double getMinimumBalance(){
        return minimumBalance;
    }

    public double getInterestRate(){ return interestRate; }
}
