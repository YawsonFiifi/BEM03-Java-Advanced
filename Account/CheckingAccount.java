package Account;

import Customer.Customer;


public class CheckingAccount extends Account {
    private double overdraftLimite;
    private double monthlyFees;

    public CheckingAccount(Customer customer, double initialDeposit){
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

    public boolean withdraw(double amount){
        if(amount - getBalance() > overdraftLimite) {
            System.out.printf("Withdrawal exceeds overdraft amount of $%s", overdraftLimite);
            return false;
        }

        setBalance(getBalance() - amount);

        return true;
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
