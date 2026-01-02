package service;

import dao.CustomerDAO;
import dao.OrderServiceItemDAO;
import dao.ServiceOrderDAO;
import dao.VehicleDAO;
import enums.OrderStatus;
import exceptions.*;
import model.*;
import util.DBConnection;

import java.sql.Connection;
import java.util.List;


public class GarageService {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final ServiceOrderDAO serviceOrderDAO = new ServiceOrderDAO();
    private final OrderServiceItemDAO orderServiceItemDAO = new OrderServiceItemDAO();

    public void addCustomer(Customer customer) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            if (customerDAO.findById(customer.getCustomerID()) != null)
                throw new DuplicateEntityException("ERROR: Customer with id=" + customer.getCustomerID() + " already exist");
            else customerDAO.save(con, customer);
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void addVehicle(Vehicle vehicle) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            if (vehicleDAO.existByVehicleNumber(vehicle.getVehicleNumber()))
                throw new DuplicateEntityException("ERROR: Vehicle with Vehicle id=" + vehicle.getVehicleNumber() + " already exist");
            else vehicleDAO.save(con, vehicle);
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void createOrder(String orderId, String customerID, String vehicleNumber) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            if (serviceOrderDAO.existsById(orderId)) throw new IllegalArgumentException("WARNING: Order already exist");
            Customer customer = customerDAO.findById(customerID);
            if (customer == null) {
                throw new CustomerNotFoundException("ERROR: Customer with id=\"" + customerID + "\" doesn't exist.");
            }
            validateCustomerAndVehicleOwnership(customerID,vehicleNumber);
            Vehicle vehicle = vehicleDAO.getVehicle(vehicleNumber, customer);
            if (vehicle == null) {
                throw new VehicleNotFoundException("ERROR: Vehicle with vehicle number=\"" + vehicleNumber + "\" doesn't exist.");
            }
            ServiceOrder order = new ServiceOrder(orderId, customer, vehicle);
            serviceOrderDAO.save(con, order);
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public Customer getCustomer(String ownerID) {
        Customer customer = customerDAO.findById(ownerID);
        if (customer == null)
            throw new CustomerNotFoundException("ERROR: This customer is not registered.");
        else return customer;
    }

    public void addServiceToOrder(String order_id, ServiceItem serviceItem, int quantity) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            ServiceOrder order = getOrder(con,order_id);
            order.addService(serviceItem, quantity);
            orderServiceItemDAO.saveService(con, order.getOrderId(), new OrderServiceItem(serviceItem, quantity));
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void updateServiceInOrder(String order_id, ServiceItem serviceItem, int updatedQuantity) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            ServiceOrder order = getOrder(con,order_id);
            order.updateService(serviceItem, updatedQuantity);
            orderServiceItemDAO.updateService(con, order.getOrderId(), new OrderServiceItem(serviceItem, updatedQuantity));
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void removeServiceFromOrder(String order_id, String serviceId) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            ServiceOrder order = getOrder(con,order_id);
            order.deleteService(serviceId);
            orderServiceItemDAO.deleteItem(con, order.getOrderId(), serviceId);
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void startOrder(String order_id) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            ServiceOrder order = getOrder(con,order_id);
            order.startOrder();
            serviceOrderDAO.updateStatus(con, order.getOrderId(), OrderStatus.IN_PROGRESS);
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void completeOrder(String order_id) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            ServiceOrder order = getOrder(con,order_id);
            order.completeOrder();
            serviceOrderDAO.updateStatus(con, order.getOrderId(), OrderStatus.COMPLETED);
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void cancelOrder(String order_id) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            ServiceOrder order = getOrder(con,order_id);
            order.cancelOrder();
            serviceOrderDAO.updateStatus(con, order.getOrderId(), OrderStatus.CANCELLED);
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public List<OrderServiceItem> loadOrderServices(String orderId) {
        return orderServiceItemDAO.findByOrderId(orderId);
    }

    public ServiceOrder getOrder(String order_id) {
        if (!serviceOrderDAO.existsById(order_id))
            throw new OrderNotFound("ERROR: This order does not exist.");

        ServiceOrder tempOrder = serviceOrderDAO.findById(order_id,
                customerDAO.findById(serviceOrderDAO.getCustomerId(order_id)),
                vehicleDAO.getVehicle(serviceOrderDAO.getVehicleNumber(order_id),
                customerDAO.findById(serviceOrderDAO.getCustomerId(order_id))));
        List<OrderServiceItem> items = orderServiceItemDAO.findByOrderId(order_id);

        for (OrderServiceItem item : items) {
            tempOrder.loadService(item);
        }
        return tempOrder;
    }
    private ServiceOrder getOrder(Connection con, String orderId) {
        String customerId = serviceOrderDAO.getCustomerId(orderId);
        if (customerId == null)
            throw new OrderNotFound("ERROR: This order does not exist.");

        Customer customer = customerDAO.findById(con, customerId);
        if (customer == null)
            throw new CustomerNotFoundException("ERROR: Customer not found.");

        String vehicleNumber = serviceOrderDAO.getVehicleNumber(orderId);
        Vehicle vehicle = vehicleDAO.getVehicle(con, vehicleNumber, customer);
        if (vehicle == null)
            throw new VehicleNotFoundException("ERROR: Vehicle not found.");

        ServiceOrder order = serviceOrderDAO.findById(con, orderId, customer, vehicle);
        if (order == null)
            throw new OrderNotFound("ERROR: This order does not exist.");

        List<OrderServiceItem> items = orderServiceItemDAO.findByOrderId(con, orderId);
        for (OrderServiceItem item : items) {
            order.loadService(item);
        }

        return order;
    }

    public String generateCustomerId() {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            int next = customerDAO.getNextId(con, "Customer");
            next += 1;
            customerDAO.updateNextId(con, "Customer", next);
            con.commit();
            return "C" + next;
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public String generateOrderId() {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            int next = serviceOrderDAO.getNextId(con, "ServiceOrder");
            next += 1;
            serviceOrderDAO.updateNextId(con, "ServiceOrder", next);
            con.commit();
            return "OD" + next;
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public String generateServiceItemId() {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            int next = orderServiceItemDAO.getNextId(con, "ServiceItem");
            next += 1;
            orderServiceItemDAO.updateNextId(con, "ServiceItem", next);
            con.commit();
            return "S" + next;
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
    public void validateCustomerAndVehicleOwnership(String customerID,String vehicleNumber){
        String ans=vehicleDAO.getVehicleCustomerId(vehicleNumber);
        if(ans==null)
            throw new VehicleNotFoundException("ERROR: Vehicle number: "+vehicleNumber+" is not registered.");
        if(!ans.equals(customerID))
            throw new OwnershipMismatchException("Vehicle with vehicle number: "+vehicleNumber+"" +
                    " doesn't belong to customer with customer id: "+customerID);
    }
//    public Customer getCustomerWithVehicleNumber
}
