package model;

import enums.OrderStatus;
import exceptions.DuplicateEntityException;
import exceptions.InvalidOrderStateException;
import exceptions.ServiceNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrder {
    private String orderId;
    private Customer customer;
    private Vehicle vehicle;
    private List<OrderServiceItem> services;
    private OrderStatus orderStatus;
    private final LocalDateTime created_at;
    private LocalDateTime started_at;
    private LocalDateTime completed_at;
    private LocalDateTime cancelled_at;
    private Double final_subtotal;
    private Double final_discount;
    private Double final_tax;
    private Double final_amount;

    public ServiceOrder(String orderId, Customer customer, Vehicle vehicle,
                        OrderStatus orderStatus,
                        LocalDateTime created_at,LocalDateTime started_at,
                        LocalDateTime completed_at, LocalDateTime cancelled_at) {
        this.orderId = orderId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.orderStatus=orderStatus;
        this.services = new ArrayList<>();
        this.orderStatus = orderStatus;
        this.created_at=created_at;
        this.started_at=started_at;
        this.completed_at=completed_at;
        this.cancelled_at=cancelled_at;
    }

    public static ServiceOrder createNew(String order_id,Customer customer,Vehicle vehicle){
        return new ServiceOrder(
                order_id,customer,vehicle,OrderStatus.CREATED,
                LocalDateTime.now(),null,null,null
        );
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
        if(!updated) throw new ServiceNotFoundException("ERROR: Service:"+updatedService.getServiceID()+" not present in order");
    }

    public void cancelOrder(){
        if(orderStatus==OrderStatus.COMPLETED)
            throw new InvalidOrderStateException("ERROR: Order already Completed.");
        else if(orderStatus!=OrderStatus.CREATED)
            throw new InvalidOrderStateException("ERROR: Order started,cannot be cancelled");
        orderStatus=OrderStatus.CANCELLED;
        cancelled_at=LocalDateTime.now();
    }
    public void startOrder(){
        if(orderStatus==OrderStatus.CANCELLED)
            throw new InvalidOrderStateException("ERROR: Order is cancelled, cannot start order");
        if(orderStatus==OrderStatus.CREATED) {
            if(services.isEmpty())
                throw new InvalidOrderStateException("ERROR: Cannot start order without adding services");
            orderStatus = OrderStatus.IN_PROGRESS;
            started_at=LocalDateTime.now();

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
            completed_at=LocalDateTime.now();
            this.final_subtotal=getSubTotal();
            this.final_discount=getDiscountAmount();
            this.final_tax=getTaxAmount();
            this.final_amount=getSubTotal()-getDiscountAmount()+getTaxAmount();
        }
        else {
            throw new InvalidOrderStateException("Warning: Order can only be completed from IN_PROGRESS state");
        }
    }
    public double getFinalAmount(){
        if(orderStatus==OrderStatus.COMPLETED){
            return final_amount;
        }
        return getSubTotal()-getDiscountAmount()+getTaxAmount();
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

    public void setStarted_at(LocalDateTime started_at) {
        this.started_at = started_at;
    }

    public void setCompleted_at(LocalDateTime completed_at) {
        this.completed_at = completed_at;
    }

    public void setCancelled_at(LocalDateTime cancelled_at) {
        this.cancelled_at = cancelled_at;
    }

    public OrderStatus getOrderStatus(){
        return orderStatus;
    }
    public LocalDateTime getCreatedTime(){
        return created_at;
    }
    public LocalDateTime getStartedTime(){
        return started_at;
    }
    public LocalDateTime getCompletedTime(){
        return completed_at;
    }
    public LocalDateTime getCancelledTime(){
        return cancelled_at;
    }
    public List<OrderServiceItem> getServiceList(){
        return new ArrayList<>(services);
    }
    public void setOrderStatus(OrderStatus status){
        this.orderStatus = status;
    }
    public void loadService(OrderServiceItem item) {
        services.add(item);
    }

    public double getSubTotal() {
        double total=0.0;
        for(OrderServiceItem items:services){
            total+=items.getTotalPrice();
        }
        return total;
    }

    public double getDiscountAmount(){
        return 0.0;
    }
    public double getTaxAmount(){
        return 0.0;
    }

    public void setFinal_subtotal(Double final_subtotal) {
        this.final_subtotal = final_subtotal;
    }

    public void setFinal_discount(Double final_discount) {
        this.final_discount = final_discount;
    }

    public void setFinal_tax(Double final_tax) {
        this.final_tax = final_tax;
    }

    public void setFinal_amount(Double final_amount) {
        this.final_amount = final_amount;
    }
    public double getFrozenSubtotal() {
        return final_subtotal;
    }

    public double getFrozenDiscount() {
        return final_discount;
    }

    public double getFrozenTax() {
        return final_tax;
    }
    public double getBillSubtotal() {
        if (orderStatus == OrderStatus.COMPLETED) {
            return final_subtotal;
        }
        return getSubTotal();
    }

    public double getBillDiscount() {
        if (orderStatus == OrderStatus.COMPLETED) {
            return final_discount;
        }
        return getDiscountAmount();
    }

    public double getBillTax() {
        if (orderStatus == OrderStatus.COMPLETED) {
            return final_tax;
        }
        return getTaxAmount();
    }

}
