package service;

import model.Customer;
import model.ServiceOrder;
import model.Vehicle;

import java.util.HashMap;
import java.util.Map;


public class GarageService {
    private Map<String, Customer> customerList=new HashMap<>();
    private Map<String, Vehicle> vehicleList=new HashMap<>();
    private Map<String, ServiceOrder> serviceOrder=new HashMap<>();

    public void addCustomer(Customer customer){
        customerList.put(customer.getCustomerID(),customer);
    }
    public void addVehicle(Vehicle vehicle){
        vehicleList.put(vehicle.getVehicleNumber(),vehicle);
    }

    public ServiceOrder createOrder(String orderId,String customerID,String vehicleNumber){
        Customer customer=customerList.get(customerID);
        Vehicle vehicle=vehicleList.get(vehicleNumber);
        ServiceOrder order=new ServiceOrder(orderId,customer,vehicle);
        serviceOrder.put(orderId,order);
        return order;
    }

    public ServiceOrder getOrder(String orderId){
        return serviceOrder.get(orderId);
    }
}
