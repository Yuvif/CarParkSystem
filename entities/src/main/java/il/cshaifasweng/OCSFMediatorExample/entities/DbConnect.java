package il.cshaifasweng.OCSFMediatorExample.entities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.Serializable;

public class DbConnect implements Serializable{
    private static String HOST = "127.0.0.1";
    private static int PORT = 3306;
    private static String DB_NAME = "Team4";
    private static String USERNAME = "root";
    private static String PASSWORD = "dangla1604";
    private static Connection connection;

    public static Connection getConnect()
    {
        try {
            connection = DriverManager.getConnection(String.format(
                    "jdbc:mysql://%s:%d/%s", HOST,PORT,DB_NAME),
                    USERNAME,PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
}