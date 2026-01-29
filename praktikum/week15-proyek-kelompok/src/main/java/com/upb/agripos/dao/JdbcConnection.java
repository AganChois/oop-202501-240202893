package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import com.upb.agripos.config.DatabaseConfig;

/**
 * Utility class untuk koneksi ke database PostgreSQL
 * Konfigurasi dibaca dari application.properties
 */
public class JdbcConnection {

    // Baca konfigurasi dari DatabaseConfig yang load dari application.properties
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUsername();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    /**
     * Mendapatkan koneksi ke database
     * @return Connection object atau null jika gagal
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.err.println("❌ Error connecting to database: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Test koneksi database
     * @return true jika berhasil connect
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
