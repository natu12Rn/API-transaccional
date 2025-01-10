package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static String url = "jdbc:postgresql://localhost:5432/testt";
    private static String user = "postgres";
    private static String password = "hola";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

}
