package service;

import dao.CustomerDAO;
import dao.OrderServiceItemDAO;
import dao.ServiceOrderDAO;
import dao.VehicleDAO;
import enums.OrderStatus;
import exceptions.*;
import model.*;

import java.util.List;


public class GarageService {
    private final CustomerDAO customerDAO=new CustomerDAO();
    private final VehicleDAO vehicleDAO=new VehicleDAO();
    private final ServiceOrderDAO serviceOrderDAO=new ServiceOrderDAO();
    private final OrderServiceItemDAO orderServiceItemDAO=new OrderServiceItemDAO();

    public void addCustomer(Customer customer){
        if(customerDAO.findById(customer.getCustomerID())!=null)
            throw new DuplicateEntityException("ERROR: Customer with id="+customer.getCustomerID()+" already exist");
        else customerDAO.save(customer);
    }
    public void addVehicle(Vehicle vehicle){
        if(vehicleDAO.existByVehicleNumber(vehicle.getVehicleNumber()))
            throw new DuplicateEntityException("ERROR: Vehicle with Vehicle id="+vehicle.getVehicleNumber()+" already exist");
        else vehicleDAO.save(vehicle);
    }

    public void createOrder(String orderId,String customerID,String vehicleNumber){
        if(serviceOrderDAO.existsById(orderId)) throw new IllegalArgumentException("WARNING: Order already exist");
        Customer customer=customerDAO.findById(customerID);
        if(customer==null){
            throw new CustomerNotFoundException("ERROR: Customer with id=\""+customerID+"\" doesn't exist.");
        }
        Vehicle vehicle=vehicleDAO.getByVehicleNumber(vehicleNumber,customer);
        if(vehicle==null){
            throw new VehicleNotFoundException("ERROR: Vehicle with vehicle number=\""+vehicleNumber+"\" doesn't exist.");
        }
        ServiceOrder order=new ServiceOrder(orderId,customer,vehicle);
        serviceOrderDAO.save(order);
    }

    public Customer getCustomer(String ownerID){
        Customer customer=customerDAO.findById(ownerID);
        if(customer==null)
            throw new CustomerNotFoundException("ERROR: This customer is not registered.");
        else return customer;
    }

    public void addServiceToOrder(String order_id, ServiceItem serviceItem,int quantity){
        ServiceOrder order=getOrder(order_id);
        order.addService(serviceItem,quantity);
        orderServiceItemDAO.saveService(order.getOrderId(),new OrderServiceItem(serviceItem,quantity));
    }
    public void updateServiceInOrder(String order_id, ServiceItem serviceItem,int updatedQuantity){
        ServiceOrder order=getOrder(order_id);
        order.updateService(serviceItem,updatedQuantity);
        orderServiceItemDAO.updateService(order.getOrderId(),new OrderServiceItem(serviceItem,updatedQuantity));
    }
    public void removeServiceFromOrder(String order_id,String serviceId){
        ServiceOrder order=getOrder(order_id);
        order.deleteService(serviceId);
        orderServiceItemDAO.deleteItem(order.getOrderId(),serviceId);
    }
    public void startOrder(String order_id){
        ServiceOrder order=getOrder(order_id);
        order.startOrder();
        serviceOrderDAO.updateStatus(order.getOrderId(), OrderStatus.IN_PROGRESS);
    }
    public void completeOrder(String order_id){
        ServiceOrder order=getOrder(order_id);
        order.completeOrder();
        serviceOrderDAO.updateStatus(order.getOrderId(),OrderStatus.COMPLETED);
    }
    public void cancelOrder(String order_id){
        ServiceOrder order=getOrder(order_id);
        order.cancelOrder();
        serviceOrderDAO.updateStatus(order.getOrderId(), OrderStatus.CANCELLED);
    }
    public List<OrderServiceItem> loadOrderServices(String orderId){
        return orderServiceItemDAO.findByOrderId(orderId);
    }
    public ServiceOrder getOrder(String order_id){
        if(!serviceOrderDAO.existsById(order_id))
            throw new OrderNotFound("ERROR: This order does not exist.");

        ServiceOrder tempOrder=serviceOrderDAO.findById(order_id,
              customerDAO.findById(serviceOrderDAO.getCustomerId(order_id)),
                vehicleDAO.getByVehicleNumber(serviceOrderDAO.getVehicleNumber(order_id),
                        customerDAO.findById(serviceOrderDAO.getCustomerId(order_id))));

        List<OrderServiceItem> items=orderServiceItemDAO.findByOrderId(order_id);

        for(OrderServiceItem item : items){
            tempOrder.loadService(item);
        }
        return tempOrder;
    }

    public String generateCustomerId(){
        return customerDAO.generateCustomerId();
    }
    public  String generateOrderId(){
        return serviceOrderDAO.generateServiceId();
    }
    public String generateServiceItemId(){
        return orderServiceItemDAO.generateServiceItemId();
    }
}
