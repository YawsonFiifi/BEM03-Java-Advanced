package models;


public class RegularCustomer extends Customer {
    public RegularCustomer(String name, int age, String contact, String address){
        super(name, age, contact, address);
    }

    public void displayCustomerDetails(){
        
    }

    public String getCustomerType(){
        return "Regular";
    }
}
