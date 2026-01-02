import enums.OrderStatus;
import enums.VehicleType;
import exceptions.InvalidOrderStateException;
import model.Customer;
import model.ServiceItem;
import model.ServiceOrder;
import model.Vehicle;

public class Testing {
    public void testStartAndCompleteOrder() {
        Customer c = new Customer("C1", "A", "999");
        Vehicle v = new Vehicle("V1", c, "Honda", VehicleType.CAR);
        ServiceOrder order = new ServiceOrder("O1", c, v);

        order.addService(new ServiceItem("S1", "Oil Change", 500), 1);
        order.startOrder();
        order.completeOrder();

        if (order.getOrderStatus() != OrderStatus.COMPLETED) {
            throw new RuntimeException("Order should be COMPLETED");
        }
    }

    public void testStartOrderWithoutServiceShouldFail() {
        try {
            Customer c = new Customer("C1", "A", "999");
            Vehicle v = new Vehicle("V1", c, "Honda", VehicleType.CAR);
            ServiceOrder order = new ServiceOrder("O1", c, v);

            order.startOrder();

            throw new RuntimeException("Expected exception was not thrown");
        } catch (InvalidOrderStateException e) {
            // correct behavior
        }
    }

    public void testAddServiceAfterStartShouldFail() {
        try {
            Customer c = new Customer("C1", "A", "999");
            Vehicle v = new Vehicle("V1", c, "Honda", VehicleType.CAR);
            ServiceOrder order = new ServiceOrder("O1", c, v);

            order.addService(new ServiceItem("S1", "Oil", 500), 1);
            order.startOrder();
            order.addService(new ServiceItem("S2", "Wash", 200), 1);

            throw new RuntimeException("Expected exception not thrown");
        } catch (InvalidOrderStateException e) {
            // correct
        }
    }

    public void testTotalAmountCalculation() {
        Customer c = new Customer("C1", "A", "999");
        Vehicle v = new Vehicle("V1", c, "Honda", VehicleType.CAR);
        ServiceOrder order = new ServiceOrder("O1", c, v);

        order.addService(new ServiceItem("S1", "Oil", 500), 2);
        order.addService(new ServiceItem("S2", "Wash", 200), 1);

        double total = order.getTotalAmount();

        if (total != 1200) {
            throw new RuntimeException("Total should be 1200 but was " + total);
        }
    }
    public static void main(String[] args) {
        Testing ob=new Testing();
        ob.testStartAndCompleteOrder();
        ob.testStartOrderWithoutServiceShouldFail();
        ob.testAddServiceAfterStartShouldFail();
        ob.testTotalAmountCalculation();

        System.out.println("ALL TESTS PASSED");
    }
}
