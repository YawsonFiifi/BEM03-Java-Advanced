package Customer;


abstract public class Customer {
    static int customerCounter = 0;

    private String customerId;
    private String name;
    private String contact;
    private String address;
    private int age;

    Customer(String name, int age, String contact, String address){
        this.customerId = "CUS" + ++customerCounter;
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
