package garageServiceApplication;

import enums.VehicleType;
import model.*;
import service.GarageService;

import java.util.Scanner;

public class GarageServiceApplication {
    public static void main(String[] args) {

        GarageService garageService=new GarageService();
        Scanner sc=new Scanner(System.in);
        ServiceOrder currentOrder=null;
        int customerID=1, orderID =1;
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
                        garageService.addCustomer(new Customer("C" + customerID, name, ph));
                        System.out.println("Customer added with customer ID:"+("C" + customerID++));
                        break;
                    case 2:
                        System.out.print("Vehicle Number: ");
                        String vNum = sc.nextLine();
                        System.out.print("Owner Customer ID: ");
                        String ownerID = sc.nextLine();
                        System.out.print("Brand: ");
                        String brand = sc.nextLine();
                        System.out.print("Type (BIKE/CAR): ");
                        VehicleType type = VehicleType.valueOf(sc.nextLine());
                        Customer owner = garageService.getCustomer(ownerID);
                        garageService.addVehicle(new Vehicle(vNum, owner, brand, type));
                        System.out.println("Vehicle added");
                        break;
                    case 3:
                        System.out.print("Customer id: ");
                        String cID = sc.nextLine();
                        System.out.print("Vehicle Number: ");
                        String vNumber = sc.nextLine();
                        currentOrder = garageService.createOrder("OD" + orderID, cID, vNumber);
                        System.out.println("Order Created with Order id:" + ("OD" + orderID++));
                        break;
                    case 4:
                        if(currentOrder==null)
                            throw new RuntimeException("ERROR: No Order created.");
                        System.out.print("Service Id: ");
                        String sID = sc.nextLine();
                        System.out.print("Service Name: ");
                        String sName = sc.nextLine();
                        System.out.print("Price: ");
                        int price = sc.nextInt();
                        System.out.print("Quantity: ");
                        int quantity=sc.nextInt();
                        sc.nextLine();
                        currentOrder.addService(new ServiceItem(sID, sName, price),quantity);
                        System.out.println("Service added");
                        break;
                    case 5:
                        if(currentOrder==null)
                            throw new RuntimeException("ERROR: No Order created.");
                        System.out.print("Service ID to remove: ");
                        String sid = sc.nextLine();
                        currentOrder.deleteService(sid);
                        System.out.println("Service removed");
                        break;
                    case 6:
                        if(currentOrder==null)
                            throw new RuntimeException("ERROR: No Order created.");
                        System.out.println("To Update: ");
                        System.out.print("Service Id: ");
                        String siD = sc.nextLine();
                        System.out.print("Service Name: ");
                        String Sname = sc.nextLine();
                        System.out.print("Price: ");
                        int Price = sc.nextInt();
                        System.out.print("Quantity: ");
                        int quantitY=sc.nextInt();
                        sc.nextLine();
                        currentOrder.updateService(new ServiceItem(siD, Sname, Price),quantitY);
                        System.out.println("Service Updated");
                        break;
                    case 7:
                        if(currentOrder==null)
                            throw new RuntimeException("ERROR: No Order created.");
                        currentOrder.startOrder();
                        System.out.println("Order started");
                        break;
                    case 8:
                        if(currentOrder==null)
                            throw new RuntimeException("ERROR: No Order created.");
                        currentOrder.completeOrder();
                        System.out.println("Order completed");
                        break;
                    case 9:
                        if(currentOrder==null)
                            throw new RuntimeException("ERROR: No Order created.");
                        currentOrder.cancelOrder();
                        System.out.println("Order cancelled.");
                        break;

                    case 10:
                        if(currentOrder==null)
                            throw new RuntimeException("ERROR: No Order created.");
                        Bill bill = new Bill("B" + currentOrder.getOrderId(), currentOrder);
                        bill.printBill();
                        break;
                    case 11:
                        System.out.println("EXITING.......");
                        sc.close();
                        return;
                    default:
                        System.out.println("Enter valid choice from 1-10");
                }
            }
            catch(RuntimeException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
