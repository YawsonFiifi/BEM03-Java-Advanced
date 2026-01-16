package models;


public class PremiumCustomer extends Customer {
    private double minimumBalance = 10000;

    public PremiumCustomer(String name, int age, String contact, String address){
        super(name, age, contact, address);
    }

    public void displayCustomerDetails(){

    }

    public String getCustomerType(){
        return "Premium";
    }

    public double getMinimumBalance(){
        return minimumBalance;
    }

    public void setMinimumBalance(double amount){
        minimumBalance = amount;
    }

    public boolean hasWaivedFees(){
        return true;
    }
}
