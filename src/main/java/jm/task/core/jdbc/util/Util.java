package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    public static final String URL = "jdbc:mysql://localhost:3306/new_schema_project?useSSL=false&serverTimezone=UTC";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";

    private static Connection connection = null;


    private Util() {
        throw new UnsupportedOperationException("This is a utilitarian class, instantiation is prohibited");
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting connection", e);
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException("Error closing connection", e);
            }
        }
    }
}
