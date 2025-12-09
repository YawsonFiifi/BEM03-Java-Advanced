package Account;

import Customer.Customer;


public class SavingsAccount extends Account{
    private double interestRate;
    private double minimumBalance;

    public SavingsAccount(Customer customer, double initialDeposit){
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
    
    public boolean withdraw(double amount){
        if(getBalance() - amount < minimumBalance) {
            System.out.println("Withdrawal limit reached");
            return false;
        };

        setBalance(getBalance() - amount);

        return true;
    } 

    public double calculateInterest(){
        return interestRate * getBalance();
    }

    public double getMinimumBalance(){
        return minimumBalance;
    }

    public double getInterestRate(){ return interestRate; }
}
