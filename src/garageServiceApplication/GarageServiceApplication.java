package garageServiceApplication;

import enums.VehicleType;
import model.*;
import service.GarageService;

public class GarageServiceApplication {
    public static void main(String[] args) {
        GarageService garage =new GarageService();
        Customer customer=new Customer("C001","Ankit","7782902125");
        Customer customer2=new Customer("C002","Aditi","9508939745");
        garage.addCustomer(customer2);
        Vehicle vehicle=new Vehicle("BR10U 2595",customer2,"Honda", VehicleType.BIKE);
        garage.addVehicle(vehicle);

        ServiceOrder order=garage.createOrder("O001","C002","BR10U 2595");
        ServiceItem oil=new ServiceItem("G001","Oil change",450);
        ServiceItem wash=new ServiceItem("G002","Washing",100);
        order.addService(oil);
        order.addService(wash);

        order.startOrder();
        order.completeOrder();

        Bill bill=new Bill("B001",order);
        bill.printBill();
    }
}
