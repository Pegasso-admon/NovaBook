package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Prevents instantiation
    private DBConnection() {}

    // Returns a new database connection
    public static Connection getConnection() throws SQLException {
        String url = ConfigLoader.getProperty("db.url");
        String user = ConfigLoader.getProperty("db.user");
        String password = ConfigLoader.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }
}