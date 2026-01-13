package model;

import java.math.BigDecimal;

public class ServiceItem {
    private String serviceID;
    private String serviceName;
    private BigDecimal price;

    public ServiceItem(String serviceID, String serviceName, BigDecimal price) {
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

    public BigDecimal getPrice() {
        return price;
    }
}
