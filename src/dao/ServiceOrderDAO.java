package dao;

import enums.OrderStatus;
import model.Customer;
import model.ServiceOrder;
import model.Vehicle;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class ServiceOrderDAO {

    public int getNextId(Connection con,String name) {
        String select = "SELECT next_value FROM id_sequence WHERE name=? FOR UPDATE";

        try (PreparedStatement ps = con.prepareStatement(select)) {
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("next_value");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateNextId(Connection con, String name, int next) {
        String update="UPDATE id_sequence SET next_value=? WHERE name=?";
        try(PreparedStatement ps=con.prepareStatement(update)){
            ps.setInt(1,next);
            ps.setString(2,name);
            ps.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void save(Connection con,ServiceOrder order){
        String sql = """
                INSERT INTO service_order
                (order_id, customer_id, vehicleNumber, orderStatus, created_at)
                VALUES (?,?,?,?,?)""";

        try (
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,order.getOrderId());
            ps.setString(2,order.getCustomer().getCustomerID());
            ps.setString(3,order.getVehicle().getVehicleNumber());
            ps.setString(4,order.getOrderStatus().name());
            ps.setTimestamp(5, Timestamp.valueOf(order.getCreatedTime()));
            ps.executeUpdate();
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public boolean existsById(String orderId){
        String sql="SELECT * FROM service_order WHERE order_id=?";
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderId);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return false;
            else return true;
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public boolean existsById(Connection con,String orderId){
        String sql="SELECT * FROM service_order WHERE order_id=?";
        try (
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderId);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return false;
            else return true;
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public OrderStatus findStatusById(String orderId){
        String sql="SELECT * FROM service_order WHERE order_id=?";
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderId);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return null;

            return OrderStatus.valueOf(rs.getString("orderStatus"));
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public ServiceOrder findById(String orderId,Customer customer,Vehicle vehicle){
        String sql="SELECT * FROM service_order WHERE order_id=?";
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderId);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return null;

            ServiceOrder order=new ServiceOrder(orderId,customer,vehicle,
                    OrderStatus.valueOf(rs.getString("orderStatus")),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    (rs.getTimestamp("started_at")!=null)?rs.getTimestamp("started_at").toLocalDateTime():null,
                    (rs.getTimestamp("completed_at")!=null)?rs.getTimestamp("completed_at").toLocalDateTime():null,
                    (rs.getTimestamp("cancelled_at")!=null)?rs.getTimestamp("cancelled_at").toLocalDateTime():null);
            return order;
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public ServiceOrder findById(Connection con,String orderId,Customer customer,Vehicle vehicle){
        String sql="SELECT * FROM service_order WHERE order_id=?";
        try (
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderId);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return null;

            ServiceOrder order=new ServiceOrder(orderId,customer,vehicle,
                    OrderStatus.valueOf(rs.getString("orderStatus")),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    (rs.getTimestamp("started_at")!=null)?rs.getTimestamp("started_at").toLocalDateTime():null,
                    (rs.getTimestamp("completed_at")!=null)?rs.getTimestamp("completed_at").toLocalDateTime():null,
                    (rs.getTimestamp("cancelled_at")!=null)?rs.getTimestamp("cancelled_at").toLocalDateTime():null);
            return order;
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public void updateStatus(Connection con,ServiceOrder order){
        String sql = """
                    UPDATE service_order SET orderStatus = ?,
                     started_at = ?,
                     completed_at = ?,
                     cancelled_at = ?
                     WHERE order_id = ?""";
        try (
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,order.getOrderStatus().name());
            ps.setTimestamp(2,(order.getStartedTime()!=null)?Timestamp.valueOf(order.getStartedTime()):null);
            ps.setTimestamp(3,(order.getCompletedTime()!=null)?Timestamp.valueOf(order.getCompletedTime()):null);
            ps.setTimestamp(4,(order.getCancelledTime()!=null)?Timestamp.valueOf(order.getCancelledTime()):null);
            ps.setString(5,order.getOrderId());
            ps.executeUpdate();

        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public String getCustomerId(String orderId){
        String sql="SELECT * FROM service_order WHERE order_id=?";
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderId);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return null;

            return rs.getString("customer_id");
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }

    public String getVehicleNumber(String orderId){
        String sql="SELECT * FROM service_order WHERE order_id=?";
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderId);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return null;

            return rs.getString("vehicleNumber");
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public String getVehicleNumber(Connection con,String orderId){
        String sql="SELECT * FROM service_order WHERE order_id=?";
        try (
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderId);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return null;

            return rs.getString("vehicleNumber");
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
}
