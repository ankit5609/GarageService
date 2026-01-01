package model;

public class Customer {
    private String customer_ID;
    private String name;
    private String phone_Number;

    public Customer(String customer_ID, String name, String phone_Number) {
        this.customer_ID = customer_ID;
        this.name = name;
        this.phone_Number = phone_Number;
    }

    public String getCustomerID() {
        return customer_ID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phone_Number;
    }
}
