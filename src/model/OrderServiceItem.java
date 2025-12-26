package model;

public class OrderServiceItem {
    private ServiceItem serviceItem;
    private int quantity;

    public OrderServiceItem(ServiceItem serviceItem, int quantity) {
        if(quantity<=0)
            throw new IllegalArgumentException("ERROR: Quantity must be greater than 0");
        this.serviceItem = serviceItem;
        this.quantity = quantity;
    }

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity<=0)
            throw new IllegalArgumentException("ERROR: Quantity must be greater than 0");
        this.quantity = quantity;
    }

    public double getTotalPrice(){
        return serviceItem.getPrice()*quantity;
    }
}
