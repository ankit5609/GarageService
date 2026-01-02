package dao;

import enums.VehicleType;
import exceptions.OwnershipMismatchException;
import model.Customer;
import model.Vehicle;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleDAO {

    public void save(Connection con,Vehicle vehicle){
        String sql="INSERT INTO vehicle VALUES (?,?,?,?)";

        try(
         PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,vehicle.getVehicleNumber());
            ps.setString(2,vehicle.getOwner().getCustomerID());
            ps.setString(3,vehicle.getBrand());
            ps.setString(4,vehicle.getVehicleType().name());
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public String getVehicleCustomerId(String vehicleNumber){
        String sql="SELECT * FROM vehicle WHERE vehicleNumber=?";

        try(Connection con=DBConnection.getConnection();
                PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1, vehicleNumber);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return null;
            else return rs.getString("customer_id");
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Vehicle getVehicle(String vehicleNumber, Customer customer){
        String sql="SELECT * FROM vehicle WHERE vehicleNumber=?";

        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1, vehicleNumber);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return null;
            return new Vehicle(rs.getString("vehicleNumber"),
                    customer,
                    rs.getString("brand"),
                    VehicleType.valueOf(rs.getString("vehicleType"))
            );
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Vehicle getVehicle(Connection con,String vehicleNumber, Customer customer){
        String sql="SELECT * FROM vehicle WHERE vehicleNumber=?";

        try(
            PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1, vehicleNumber);
            ResultSet rs=ps.executeQuery();
            if(!rs.next()) return null;
            return new Vehicle(rs.getString("vehicleNumber"),
                    customer,
                    rs.getString("brand"),
                    VehicleType.valueOf(rs.getString("vehicleType"))
            );
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public boolean existByVehicleNumber(String vehicleNumber){
        String sql="SELECT 1 FROM vehicle WHERE vehicleNumber=?";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,vehicleNumber);
            return ps.executeQuery().next();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
