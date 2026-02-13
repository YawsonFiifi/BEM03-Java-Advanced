package models;


abstract public class Customer {
    public static int customerCounter = 0;

    private final String customerId;

    private final String name;
    private final String contact;
    private final String address;
    private final int age;

    public String getCustomerId() {
        return customerId;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    Customer(String name, int age, String contact, String address){
        this.customerId = String.format("CUS%03d", ++customerCounter);
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.address = address;
    }

    abstract public String getCustomerType();

    public String getName(){
        return name;
    }
}
