package model;

public class Bill {
    private String billID;
    private ServiceOrder order;
    private double amount;

    public Bill(String billID, ServiceOrder order) {
        this.billID = billID;
        this.order = order;
        amount= order.getTotalAmount();
    }
    public void printBill(){
        System.out.println("Bill ID: "+billID);
        System.out.println("Customer: "+ order.getCustomer().getName());
        System.out.println("Vehicle: "+ order.getVehicle().getVehicleNumber());
        System.out.println("Total Amount: "+ order.getTotalAmount());
    }

}
