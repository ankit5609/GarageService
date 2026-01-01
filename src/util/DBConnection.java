package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL="jdbc:mysql://localhost:3306/garage_db";
    private static final String User="root";
    private static final String password="13190000";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,User,password);
    }
}
