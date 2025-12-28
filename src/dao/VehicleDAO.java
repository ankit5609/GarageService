package dao;

import enums.VehicleType;
import model.Customer;
import model.Vehicle;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleDAO {

    public void save(Vehicle vehicle){
        String sql="INSERT INTO vehicle VALUES (?,?,?,?)";

        try(Connection con= DBConnection.getConnection();
         PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,vehicle.getVehicleNumber());
            ps.setString(2,vehicle.getOwner().getCustomerID());
            ps.setString(3,vehicle.getBrand());
            ps.setString(4,vehicle.getVehicleType().name());
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Vehicle getByVehicleNumber(String vehicleNumber, Customer customer){
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
            throw new RuntimeException(e.getMessage());
        }
    }
    public boolean existByVehicleNumber(String vehicleNumber){
        return getByVehicleNumber(vehicleNumber, new Customer("", "", "")) != null;
    }
}
