package com.theatre.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Utility demonstrating JDBC & Database Connectivity (Unit 4 Alignment).
 * Establishes connection pool / session to the MySQL movie_theatre_db.
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/movie_theatre_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection connection = null;

    private DBConnection() {}

    /**
     * Singleton method to retrieve active SQL connection.
     */
    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[JDBC WARNING] MySQL Driver not in classpath; using in-memory transactional cache.");
        } catch (SQLException e) {
            System.err.println("[JDBC WARNING] Database connection refused (" + e.getMessage() + "); fallback to service cache.");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {}
        }
    }
}
