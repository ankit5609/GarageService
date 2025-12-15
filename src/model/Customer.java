package model;

public class Customer {
    private String cID;
    private String name;
    private String phoneNumber;

    public Customer(String cID, String name, String phoneNumber) {
        this.cID = cID;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getcID() {
        return cID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
