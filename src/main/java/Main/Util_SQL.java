package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util_SQL {

    //  Database credentials
    static final String POSTGRES_URL = "jdbc:postgresql://127.0.0.1:5432/";
    static final String USER = "postgres";
    static final String PASS = "Jcafkml1984";

    public static Connection getConnect(String baseName){

        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return connection;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            connection = DriverManager.getConnection(POSTGRES_URL + baseName, USER, PASS);
        }catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return connection;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database: " + baseName + " now");
        } else {
            System.out.println("Failed to make connection to database: " + baseName);
        }

        return connection;
    }
}
