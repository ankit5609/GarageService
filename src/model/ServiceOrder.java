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
    private List<OrderServiceItem> services;
    private OrderStatus orderStatus;

    public ServiceOrder(String orderId, Customer customer, Vehicle vehicle) {
        this.orderId = orderId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.services = new ArrayList<>();
        this.orderStatus = OrderStatus.CREATED;
    }

    public void addService(ServiceItem service,int quantity){
        if(orderStatus==OrderStatus.CANCELLED)
            throw new InvalidOrderStateException("ERROR: Order is cancelled, cannot add service");
        if(orderStatus!=OrderStatus.CREATED)
            throw new InvalidOrderStateException("ERROR: Cannot add services after order has started");
        for(OrderServiceItem serviceLoop :services){
            if(serviceLoop.getServiceItem().getServiceID().equals(service.getServiceID()))
                throw new DuplicateEntityException("Service:"+service.getServiceID()+" already added to order");
        }
        services.add(new OrderServiceItem(service,quantity));
    }

    public void deleteService(String serviceID){
        if(orderStatus==OrderStatus.CANCELLED)
            throw new InvalidOrderStateException("ERROR: Order is cancelled, cannot delete service");
        if(orderStatus!=OrderStatus.CREATED)
            throw new InvalidOrderStateException("ERROR: Cannot modify services after order has started.");
        boolean removed=services.removeIf(
                service -> service.getServiceItem().getServiceID().equals(serviceID)
        );
        if (!removed) {
            throw new IllegalArgumentException("Service not found in order: " + serviceID);
        }
    }

    public void updateService(ServiceItem updatedService,int updateQuantity){
        if(updateQuantity<=0)
            throw new IllegalArgumentException("ERROR: Quantity must be greater than 0");
        if(orderStatus==OrderStatus.CANCELLED)
            throw new InvalidOrderStateException("ERROR: Order is cancelled, cannot update service");
        if(orderStatus!=OrderStatus.CREATED)
            throw new InvalidOrderStateException("ERROR: Cannot add services after order has started");
        boolean updated=false;
        for (int i = 0; i < services.size(); i++) {
            ServiceItem existing = services.get(i).getServiceItem();

            if (existing.getServiceID().equals(updatedService.getServiceID())) {
                services.set(i, new OrderServiceItem(updatedService,updateQuantity));
                updated = true;
                break;
            }
        }
        if(!updated) throw new DuplicateEntityException("ERROR: Service:"+updatedService.getServiceID()+" not present in order");
    }

    public void cancelOrder(){
        if(orderStatus!=OrderStatus.CREATED)
            throw new InvalidOrderStateException("ERROR: Order started,cannot be cancelled");
        orderStatus=OrderStatus.CANCELLED;
    }
    public void startOrder(){
        if(orderStatus==OrderStatus.CANCELLED)
            throw new InvalidOrderStateException("ERROR: Order is cancelled, cannot start order");
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
        if(orderStatus==OrderStatus.CANCELLED)
            throw new InvalidOrderStateException("ERROR: Order is already cancelled.");
        if(orderStatus==OrderStatus.IN_PROGRESS) {
            orderStatus = OrderStatus.COMPLETED;
        }
        else {
            throw new InvalidOrderStateException("Warning: Order can only be completed from IN_PROGRESS state");
        }
    }
    public double getTotalAmount(){
        double total=0.0;
        for(OrderServiceItem s:services){
            total+=s.getTotalPrice();
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

    public List<OrderServiceItem> getServiceList(){
        return services;
    }
//    public void setOrderStatus(OrderStatus orderStatus){
//        this.orderStatus=orderStatus;
//    }
}
