package com.example.demo1.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {

    // TODO: Set these via environment variables or a config file for production
    private static final String URL = System.getenv().getOrDefault("ALTRUITHM_DB_URL", "jdbc:mysql://localhost:3306/altruithm_db");
    private static final String USER = System.getenv().getOrDefault("ALTRUITHM_DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("ALTRUITHM_DB_PASSWORD", "DarkSorcerer@014");

    private Database() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}


