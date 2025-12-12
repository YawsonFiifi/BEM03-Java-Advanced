package Account;
import CustomExceptions.AccountCreationException;
import CustomExceptions.WithdrawalException;
import Customer.Customer;


abstract public class Account {
    static int accountCounter = 0;
    
    private String accountNumber;
    private String status = "Active";
    private Customer customer;
    private double balance = 0;
    
    abstract public void displayAccountDetails();
    abstract public String getAccountType();

    Account(Customer customer, double initialDeposit) throws AccountCreationException {
        if(customer.getCustomerType().equals("Premium Customer") && initialDeposit < 10000) {
            throw new AccountCreationException("Minimum balance should be $10,000 or more for Premium Customers");
        }
        this.accountNumber = "ACC" + ++accountCounter;
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

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) throws WithdrawalException {
        balance -= amount;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public Customer getCustomer(){
        return customer;
    }
}
