package model;

import emums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class ServiceOrder {
    private String orderId;
    private Customer customer;
    private Vehicle vehicle;
    private List<ServiceItems> services;
    private OrderStatus orderStatus;

    public ServiceOrder(String orderId, Customer customer, Vehicle vehicle) {
        this.orderId = orderId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.services = new ArrayList<>();
        this.orderStatus = OrderStatus.CREATED;
    }

    public void addService(ServiceItems service){
        services.add(service);
    }
    public void start(){
        orderStatus=OrderStatus.IN_PROGRESS;
    }
    public void complete(){
        orderStatus=OrderStatus.COMPLETED;
    }
    public double getTotalAmount(){
        double total=0.0;
        for(ServiceItems s:services){
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
