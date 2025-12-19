package model;

import enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class ServiceOrder {
    private String orderId;
    private Customer customer;
    private Vehicle vehicle;
    private List<ServiceItem> services;
    private OrderStatus orderStatus;

    public ServiceOrder(String orderId, Customer customer, Vehicle vehicle) {
        this.orderId = orderId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.services = new ArrayList<>();
        this.orderStatus = OrderStatus.CREATED;
    }

    public void addService(ServiceItem service){
        services.add(service);
    }
    public void startOrder(){
        if(orderStatus==OrderStatus.CREATED) {
            orderStatus = OrderStatus.IN_PROGRESS;
        }
        else {
            throw new IllegalStateException("Warning: First create an order");
        }
    }
    public void completeOrder(){
        if(orderStatus==OrderStatus.IN_PROGRESS) {
            orderStatus = OrderStatus.COMPLETED;
        }
        else {
            throw new IllegalStateException("Warning: Order is not yet Started");
        }
    }
    public double getTotalAmount(){
        double total=0.0;
        for(ServiceItem s:services){
            total+=s.getPrice();
        }
        return total;
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
