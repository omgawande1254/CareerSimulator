package com.careersim.database;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/career_simulation";
    private static final String USER = "root";
    private static final String PASSWORD = "193281@W/f/"; // <- Change this

    public static Connection connect() {
        System.out.println("✅ Connected to MySQL!");

        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}