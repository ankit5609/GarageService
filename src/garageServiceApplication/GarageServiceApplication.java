package garageServiceApplication;

import enums.VehicleType;
import model.*;
import service.GarageService;

public class GarageServiceApplication {
    public static void main(String[] args) {
        try {
            GarageService garage = new GarageService();
            Customer customer = new Customer("C001", "Ankit", "7782902125");
            Customer customer2 = new Customer("C002", "Aditi", "9508939745");
            garage.addCustomer(customer2);
            garage.addCustomer(customer);
            Vehicle vehicle = new Vehicle("BR10U 2595", customer2, "Honda", VehicleType.BIKE);
            garage.addVehicle(vehicle);
            ServiceItem oil = new ServiceItem("G001", "Oil change", 450);
            ServiceItem wash = new ServiceItem("G002", "Washing", 100);
            ServiceItem services[]={oil,wash};
            ServiceOrder order = garage.prepareOrder("O001", "C001", "BR10U 2595",services);

            Bill bill = new Bill("B001", order);
            bill.printBill();
        }
        catch (IllegalArgumentException  | IllegalStateException e){
            System.out.println(e.getMessage());
        }
    }
}
