package model;

public class ServiceItems {
    private String serviceID;
    private String serviceName;
    private double price;

    public ServiceItems(String serviceID, String serviceName, double price) {
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
