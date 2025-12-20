package model;

import enums.OrderStatus;
import exceptions.DuplicateEntityException;
import exceptions.InvalidOrderStateException;

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
        if(orderStatus!=OrderStatus.CREATED)
            throw new InvalidOrderStateException("ERROR: Cannot add services after order has started");
        for(ServiceItem serviceLoop :services){
            if(serviceLoop.getServiceID().equals(service.getServiceID()))
                throw new DuplicateEntityException("Service:"+service.getServiceID()+" already added to order");
        }
        services.add(service);
    }

    public void deleteService(String serviceID){
        if(orderStatus!=OrderStatus.CREATED)
            throw new InvalidOrderStateException("ERROR: Cannot modify services after order has started.");
        boolean removed=services.removeIf(
                service -> service.getServiceID().equals(serviceID)
        );
        if (!removed) {
            throw new IllegalArgumentException("Service not found in order: " + serviceID);
        }
    }

    public void updateService(ServiceItem service){
        if(orderStatus!=OrderStatus.CREATED)
            throw new InvalidOrderStateException("ERROR: Cannot add services after order has started");
        for(ServiceItem serviceLoop :services){
            if(serviceLoop.getServiceID().equals(service.getServiceID()))
                throw new DuplicateEntityException("Service:"+service.getServiceID()+" already added to order");
        }
        services.add(service);
    }
    public void startOrder(){
        if(orderStatus==OrderStatus.CREATED) {
            if(services.isEmpty())
                throw new InvalidOrderStateException("ERROR: Cannot start order without adding services");
            orderStatus = OrderStatus.IN_PROGRESS;
        }
        else {
            throw new InvalidOrderStateException("Warning: Order can only be started from CREATED state");
        }
    }
    public void completeOrder(){
        if(orderStatus==OrderStatus.IN_PROGRESS) {
            orderStatus = OrderStatus.COMPLETED;
        }
        else {
            throw new InvalidOrderStateException("Warning: Order can only be completed from IN_PROGRESS state");
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

    public OrderStatus getOrderStatus(){
        return orderStatus;
    }
}
