package model;

import enums.OrderStatus;
import exceptions.InvalidOrderStateException;

import java.util.List;

public class Bill {
    private String billID;
    private ServiceOrder order;
    private double amount;

    public Bill(String billID, ServiceOrder order) {
        if(order.getOrderStatus()== OrderStatus.CANCELLED)
            throw new InvalidOrderStateException("ERROR: This order has been cancelled.");
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
        List<OrderServiceItem> orderServiceItem=order.getServiceList();
        System.out.printf("%-20s %-10s %-10s%n", "Service Name", "Quantity", "Price");
        for (int i = 0; i < orderServiceItem.size(); i++) {
            OrderServiceItem existing = orderServiceItem.get(i);
            System.out.printf(
                    "%-20s %-10d %-10.2f%n",
                    existing.getServiceItem().getServiceName(),
                    existing.getQuantity(),
                    existing.getTotalPrice()
            );        }
        System.out.println("----------------------------------------");
        System.out.printf("Total Amount: %.2f%n", order.getTotalAmount());
    }

}
