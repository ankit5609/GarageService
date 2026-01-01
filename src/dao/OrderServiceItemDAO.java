package dao;

import model.OrderServiceItem;
import model.ServiceItem;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceItemDAO {

    public String generateServiceItemId(){
        String select="SELECT next_value FROM id_sequence WHERE name='ServiceItem' FOR UPDATE";
        String update="UPDATE id_sequence SET next_value=? WHERE name='ServiceItem'";
        try(Connection con=DBConnection.getConnection()){
            con.setAutoCommit(false);
            int next;
            try(PreparedStatement ps=con.prepareStatement(select)){
                ResultSet rs=ps.executeQuery();
                rs.next();
                next=rs.getInt("next_value");
            }
            next+=1;
            try(PreparedStatement ps=con.prepareStatement(update)){
                ps.setInt(1,next);
                ps.executeUpdate();
            }
            con.commit();
            return "S"+next;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void saveService(String orderID, OrderServiceItem serviceItem){
        String sql="INSERT INTO order_serviceItem VALUES(?,?,?,?,?)";
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderID);
            ps.setString(2,serviceItem.getServiceItem().getServiceID());
            ps.setString(3,serviceItem.getServiceItem().getServiceName());
            ps.setDouble(4,serviceItem.getServiceItem().getPrice());
            ps.setInt(5,serviceItem.getQuantity());
            ps.executeUpdate();
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public void updateService(String orderId, OrderServiceItem serviceItem){
        String sql="UPDATE order_serviceItem SET serviceName=?,price=?,quantity=? WHERE order_id=? AND serviceID=?";
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,serviceItem.getServiceItem().getServiceName());
            ps.setDouble(2,serviceItem.getServiceItem().getPrice());
            ps.setInt(3,serviceItem.getQuantity());
            ps.setString(4,orderId);
            ps.setString(5,serviceItem.getServiceItem().getServiceID());
            ps.executeUpdate();
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public void deleteItem(String orderId,String serviceId){
        String sql="DELETE FROM order_serviceItem WHERE order_id=? AND serviceID=?";
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderId);
            ps.setString(2,serviceId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Service not found for deletion");
            }
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }

    public List<OrderServiceItem> findByOrderId(String orderId){
        String sql="SELECT * FROM order_serviceItem WHERE order_id=?";
        List<OrderServiceItem> services=new ArrayList<>();
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,orderId);
            ResultSet rs=ps.executeQuery();

            while(rs.next()){
                ServiceItem serviceItem=new ServiceItem(rs.getString("serviceID"),
                        rs.getString("serviceName"),
                        rs.getDouble("price"));

                services.add(new OrderServiceItem(serviceItem,rs.getInt("quantity")));
            }
            return services;
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
}
