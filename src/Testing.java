//import model.*;
//import enums.VehicleType;
//import service.GarageService;
//
//public class Testing {
//
//    public static void main(String[] args) {
//
//        GarageService service = new GarageService();
//
//        // ---------- SETUP ----------
//        String customerId = service.generateCustomerId();
//        service.addCustomer(new Customer(customerId, "Test User", "9999999999"));
//
//        service.addVehicle(new Vehicle(
//                "TEST123",
//                service.getCustomer(customerId),
//                "Honda",
//                VehicleType.CAR
//        ));
//
//        String orderId = service.generateOrderId();
//        service.createOrder(orderId, customerId, "TEST123");
//
//        // ---------- ADD SERVICES ----------
//        service.addServiceToOrder(orderId,
//                new ServiceItem(service.generateServiceItemId(), "Oil Change", 500),
//                1
//        );
//
//        service.addServiceToOrder(orderId,
//                new ServiceItem(service.generateServiceItemId(), "Brake Service", 800),
//                1
//        );
//
//        // ---------- START + COMPLETE ----------
//        service.startOrder(orderId);
//        service.completeOrder(orderId);
//
//        // ---------- FIRST BILL ----------
//        ServiceOrder firstLoad = service.getOrder(orderId);
//        Bill bill1 = new Bill("B1", firstLoad);
//
//        double firstAmount = firstLoad.getFinalAmount();
//        System.out.println("First total: " + firstAmount);
//
//        // ---------- SECOND LOAD (fresh from DB) ----------
//        ServiceOrder secondLoad = service.getOrder(orderId);
//        Bill bill2 = new Bill("B2", secondLoad);
//
//        double secondAmount = secondLoad.getFinalAmount();
//        System.out.println("Second total: " + secondAmount);
//
//        // ---------- ASSERT ----------
//        if (firstAmount != secondAmount) {
//            throw new AssertionError(
//                    "BUG: Final amount changed after reload! " +
//                            firstAmount + " vs " + secondAmount
//            );
//        }
//
//        System.out.println("TEST PASSED: Final amount is stable.");
//    }
//}
