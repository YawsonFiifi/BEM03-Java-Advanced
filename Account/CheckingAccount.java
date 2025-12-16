package Account;

import CustomExceptions.AccountCreationException;
import CustomExceptions.InsufficientFundsException;
import Customer.Customer;


public class CheckingAccount extends Account {
    private double overdraftLimite;
    private double monthlyFees;

    public CheckingAccount(Customer customer, double initialDeposit) throws AccountCreationException {
        overdraftLimite = 1000;
        monthlyFees = customer.getCustomerType().equals("Premium") ? 0 : 10;

        super(customer, initialDeposit);
    }

    public void displayAccountDetails(){
        System.out.printf("""
            Account Number: %s
            Customer: %s (%s)
            Account Type: %s
            initial Balance: $%s
            overdraft limit: $%f
            monthly fees: $%s
            Status: %s
        """,
            getAccountNumber(),
            getCustomer().getName(),
            getCustomer().getCustomerType(),
            getAccountType(),
            getBalance(),
            getOverdraftLimit(),
            String.format("%f %s", getMonthlyFees(), getCustomer().getCustomerType().equals("Premium")
                    ? "(WAIVED - Premium Customer)"
                    : ""
            ),
            getStatus()
        );
    }

    public String getAccountType(){
        return "Checking";
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if(amount - getBalance() > overdraftLimite) {
            throw new InsufficientFundsException("Withdrawal exceeds overdraft amount of $"+overdraftLimite);
        }

        setBalance(getBalance() - amount);
    }

    public void applyMonthlyFee(){
        setBalance(getBalance() - monthlyFees);
    }

    public double getOverdraftLimit(){
        return overdraftLimite;
    }

    public double getMonthlyFees(){
        return monthlyFees;
    }
}
