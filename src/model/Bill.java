package model;

import javax.xml.transform.Source;

public class Bill {
    private String billID;
    private ServiceOrder orders;
    private double amount;

    public Bill(String billID, ServiceOrder orders) {
        this.billID = billID;
        this.orders = orders;
        amount=orders.getTotalAmount();
    }
    public void printBill(){
        System.out.println("Bill ID: "+billID);
        System.out.println("Customer: "+orders.getCustomer().getName());
        System.out.println("Vehicle: "+orders.getVehicle().getVehicleNumber());
        System.out.println("Total Amount: "+orders.getTotalAmount());
    }

}
