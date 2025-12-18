package model;

import enums.VehicleType;

public class Vehicle {
    private String vehicleNumber;
    private Customer owner;
    private String brand;
    private VehicleType vehicleType;

    public Vehicle(String vehicleNumber, Customer owner, String brand, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.owner = owner;
        this.brand = brand;
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public Customer getOwner() {
        return owner;
    }

    public String getBrand() {
        return brand;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
