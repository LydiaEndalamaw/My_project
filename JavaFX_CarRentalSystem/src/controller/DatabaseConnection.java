package controller;
import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseConnection {
    Connection connection=null;

public static Connection connectDatabase(){

    try {
        Class.forName("org.postgresql.Driver");
   Connection connection=DriverManager.getConnection("jdbc:postgresql://localhost:5432/CarRental","postgres","1111");
        return connection;
    } catch (Exception e) {
        return null;
       
    }
}

}
