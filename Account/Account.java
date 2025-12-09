package Account;
import Customer.Customer;


abstract public class Account {
    static int accountCounter = 0;
    
    private String accountNumber;
    private String status = "Active";
    private Customer customer;
    private double balance = 0;
    
    abstract public void displayAccountDetails();
    abstract public String getAccountType();

    Account(Customer customer, double initialDeposit){
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

    public boolean withdraw(double amount){
        balance -= amount;

        return true;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public Customer getCustomer(){
        return customer;
    }
}
