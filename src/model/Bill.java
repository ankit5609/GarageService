package model;

import enums.OrderStatus;
import exceptions.InvalidOrderStateException;

public class Bill {
    private String billID;
    private ServiceOrder order;
    private double amount;

    public Bill(String billID, ServiceOrder order) {
        if(order.getOrderStatus()!= OrderStatus.COMPLETED)
            throw new InvalidOrderStateException("ERROR: Order is not yet completed.");
        this.billID = billID;
        this.order = order;
        amount= order.getTotalAmount();
    }
    public void printBill(){
        System.out.println("Bill ID: "+billID);
        System.out.println("Customer: "+ order.getCustomer().getName());
        System.out.println("Vehicle: "+ order.getVehicle().getVehicleNumber());
        System.out.println("Total Amount: "+ order.getTotalAmount());
    }

}
