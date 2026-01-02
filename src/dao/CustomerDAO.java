package dao;

import model.Customer;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public int getNextId(Connection con, String name) {
        String select = "SELECT next_value FROM id_sequence WHERE name=? FOR UPDATE";


        try (PreparedStatement ps = con.prepareStatement(select)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("next_value");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void updateNextId(Connection con, String name, int next) {
        String update = "UPDATE id_sequence SET next_value=? WHERE name=?";

        try (PreparedStatement ps = con.prepareStatement(update)) {
            ps.setInt(1, next);
            ps.setString(2, name);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Connection con,Customer customer) {
        String sql = "INSERT INTO customer VALUES (?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customer.getCustomerID());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getPhoneNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer findById(String id) {
        String sql = "SELECT * FROM customer WHERE customer_id=?";

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet ans = ps.executeQuery();

            if (!ans.next()) return null;
            return new Customer(ans.getString("customer_id"), ans.getString("name"), ans.getString("phone_Number"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Customer findById(Connection con,String id) {
        String sql = "SELECT * FROM customer WHERE customer_id=?";

        try ( PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet ans = ps.executeQuery();

            if (!ans.next()) return null;
            return new Customer(ans.getString("customer_id"), ans.getString("name"), ans.getString("phone_Number"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
