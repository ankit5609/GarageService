package service;

import model.Customer;
import model.ServiceItem;
import model.ServiceOrder;
import model.Vehicle;

import java.util.HashMap;
import java.util.Map;


public class GarageService {
    private Map<String, Customer> customerList=new HashMap<>();
    private Map<String, Vehicle> vehicleList=new HashMap<>();
    private Map<String, ServiceOrder> serviceOrder=new HashMap<>();

    public void addCustomer(Customer customer){
        if(customerList.containsKey(customer.getCustomerID()))
            throw new IllegalArgumentException("ERROR: Customer with id="+customer.getCustomerID()+" already exist");
        else customerList.put(customer.getCustomerID(),customer);
    }
    public void addVehicle(Vehicle vehicle){
        if(vehicleList.containsKey(vehicle.getVehicleNumber()))
            throw new IllegalArgumentException("ERROR: Vehicle with Vehicle id="+vehicle.getVehicleNumber()+" already exist");
        else vehicleList.put(vehicle.getVehicleNumber(),vehicle);
    }
    public ServiceOrder prepareOrder(String orderId, String customerId, String vehicleNumber, ServiceItem services[]){
        ServiceOrder order=createOrder(orderId,customerId,vehicleNumber);
        for(ServiceItem service : services){
            order.addService(service);
        }
        order.startOrder();
        order.completeOrder();
        return order;
    }
    public ServiceOrder createOrder(String orderId,String customerID,String vehicleNumber){
        if(serviceOrder.containsKey(orderId)) throw new IllegalArgumentException("WARNING: Order already exist");
        Customer customer=customerList.get(customerID);
        Vehicle vehicle=vehicleList.get(vehicleNumber);
        if(customer==null){
            throw new IllegalArgumentException("ERROR: Customer with id=\""+customerID+"\" doesn't exist.");
        }
        else if(vehicle==null){
            throw new IllegalArgumentException("ERROR: Vehicle with vehicle number=\""+vehicleNumber+"\" doesn't exist.");
        }
        if(!vehicle.getOwner().equals(customer))
            throw new IllegalArgumentException("Vehicle " + vehicleNumber + " does not belong to customer " + customerID);
        ServiceOrder order=new ServiceOrder(orderId,customer,vehicle);
        serviceOrder.put(orderId,order);
        return order;
    }

    public ServiceOrder getOrder(String orderId){
        return serviceOrder.get(orderId);
    }
}
