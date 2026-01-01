package garageServiceApplication;

import enums.VehicleType;
import model.*;
import service.GarageService;

import java.util.Scanner;

public class GarageServiceApplication {
    public static void main(String[] args) {

        GarageService garageService=new GarageService();
        Scanner sc=new Scanner(System.in);
        String orderId,cID,sID;
        while (true){
            System.out.println("-------------GARAGE SERVICE MENU-------------");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Vehicle");
            System.out.println("3. Create order");
            System.out.println("4. Add services to order");
            System.out.println("5. Remove services from order");
            System.out.println("6. Update services");
            System.out.println("7. Start the order");
            System.out.println("8. Complete the order");
            System.out.println("9. Cancel Order");
            System.out.println("10. Print Bill");
            System.out.println("11. EXIT");
            System.out.println("Choose from above options");
            int choice=sc.nextInt();
            sc.nextLine();
            try{
                switch (choice) {
                    case 1:
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Phone: ");
                        String ph = sc.nextLine();
                        cID= garageService.generateCustomerId();
                        garageService.addCustomer(new Customer(cID, name, ph));
                        System.out.println("Customer added with customer ID:"+cID);
                        break;
                    case 2:
                        System.out.print("Vehicle Number: ");
                        String vNum = sc.nextLine();
                        System.out.print("Owner Customer ID: ");
                        String ownerID = sc.nextLine();
                        System.out.print("Brand: ");
                        String brand = sc.nextLine();
                        System.out.print("Type (BIKE/CAR): ");
                        VehicleType type = VehicleType.valueOf(sc.nextLine().toUpperCase());
                        Customer owner = garageService.getCustomer(ownerID);
                        garageService.addVehicle(new Vehicle(vNum, owner, brand, type));
                        System.out.println("Vehicle added");
                        break;
                    case 3:
                        System.out.print("Customer id: ");
                        cID = sc.nextLine();
                        System.out.print("Vehicle Number: ");
                        String vNumber = sc.nextLine();
                        orderId= garageService.generateOrderId();
                        garageService.createOrder(orderId, cID, vNumber);
                        System.out.println("Order Created with Order id:" + (orderId));
                        break;
                    case 4:
                        System.out.print("Order ID: ");
                        orderId=sc.nextLine();
                        sID = garageService.generateServiceItemId();
                        System.out.print("Service Name: ");
                        String sName = sc.nextLine();
                        System.out.print("Price: ");
                        int price = sc.nextInt();
                        System.out.print("Quantity: ");
                        int quantity=sc.nextInt();
                        sc.nextLine();
                        garageService.addServiceToOrder(orderId,
                                new ServiceItem(sID,sName,price),quantity);
                        System.out.println("Service added");
                        break;
                    case 5:
                        System.out.print("Order ID: ");
                        orderId=sc.nextLine();
                        System.out.print("Service ID to remove: ");
                        sID = sc.nextLine();
                       garageService.removeServiceFromOrder(orderId,sID);
                        System.out.println("Service removed");
                        break;
                    case 6:
                        System.out.println("To Update: ");
                        System.out.print("Order ID: ");
                        orderId=sc.nextLine();
                        System.out.print("Service Id: ");
                        sID = sc.nextLine();
                        System.out.print("Service Name: ");
                        String Sname = sc.nextLine();
                        System.out.print("Price: ");
                        int Price = sc.nextInt();
                        System.out.print("Quantity: ");
                        int quantitY=sc.nextInt();
                        sc.nextLine();
                        garageService.updateServiceInOrder(orderId,
                                new ServiceItem(sID,Sname,Price),quantitY);
                        System.out.println("Service Updated");
                        break;
                    case 7:
                        System.out.print("Order ID: ");
                        orderId=sc.nextLine();
                        garageService.startOrder(orderId);
                        System.out.println("Order started");
                        break;
                    case 8:
                        System.out.print("Order ID: ");
                        orderId=sc.nextLine();
                        garageService.completeOrder(orderId);
                        System.out.println("Order completed");
                        break;
                    case 9:
                        System.out.print("Order ID: ");
                        orderId=sc.nextLine();
                        garageService.cancelOrder(orderId);
                        System.out.println("Order cancelled.");
                        break;

                    case 10:
                        System.out.print("Order ID: ");
                        orderId=sc.nextLine();
                        ServiceOrder currentOrder=garageService.getOrder(orderId);
                        Bill bill = new Bill("B" + currentOrder.getOrderId(), currentOrder);
                        bill.printBill();
                        break;
                    case 11:
                        System.out.println("EXITING.......");
                        sc.close();
                        return;
                    default:
                        System.out.println("Enter valid choice from 1-11");
                }
            }
            catch(RuntimeException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
