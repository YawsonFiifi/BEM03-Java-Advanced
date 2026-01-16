package models;


abstract public class Customer {
    static int customerCounter = 0;

    private String customerId;

    private String name;
    private String contact;
    private String address;
    private int age;

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

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public Customer setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public Customer setAddress(String address) {
        this.address = address;
        return this;
    }

    public Customer setAge(int age) {
        this.age = age;
        return this;
    }

    Customer(String name, int age, String contact, String address){
        this.customerId = String.format("CUS%03d", ++customerCounter);
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.address = address;
    }

    abstract void displayCustomerDetails();
    abstract public String getCustomerType();

    public String getName(){
        return name;
    }
}
