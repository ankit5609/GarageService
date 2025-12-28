package dao;

import model.Customer;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public String generateCustomerId(){
        String select="SELECT next_value FROM id_sequence WHERE name='Customer' FOR UPDATE";
        String update="UPDATE id_sequence next_value=? WHERE name='Customer'";

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
            return "C"+next;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public void save(Customer customer){
        String sql="INSERT INTO customer VALUES (?,?,?)";

        try(Connection con= DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,customer.getCustomerID());
            ps.setString(2,customer.getName());
            ps.setString(3, customer.getPhoneNumber());
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Customer findById(String id){
        String sql="SELECT * FROM customer WHERE customer_id=?";

        try(Connection con= DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,id);
            ResultSet ans=ps.executeQuery();

            if(!ans.next()) return null;
            return new Customer(ans.getString("customer_id"),
                                    ans.getString("name"),
                                    ans.getString("phone_Number"));

        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
