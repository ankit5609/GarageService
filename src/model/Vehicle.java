package model;

import emums.VehicleType;

public class Vehicle {
    private String vehicleNumber;
    private Customer owner;
    private String Brand;
    VehicleType vehicleType;

    public Vehicle(String vehicleNumber, Customer owner, String brand, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.owner = owner;
        Brand = brand;
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public Customer getOwner() {
        return owner;
    }

    public String getBrand() {
        return Brand;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
