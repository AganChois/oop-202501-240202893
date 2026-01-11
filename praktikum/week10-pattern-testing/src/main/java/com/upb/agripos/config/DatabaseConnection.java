package com.upb.agripos.config;

public class DatabaseConnection {

    private static DatabaseConnection instance;

    // Constructor private agar tidak bisa diinstansiasi langsung
    private DatabaseConnection() {
        System.out.println("Database connected...");
    }

    // Method global untuk mengambil instance
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}
