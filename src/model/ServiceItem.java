package model;

public class ServiceItem {
    private String serviceID;
    private String serviceName;
    private double price;

    public ServiceItem(String serviceID, String serviceName, double price) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceID() {
        return serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }
}
