package com.upb.agripos.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class untuk membaca konfigurasi dari application.properties
 */
public class DatabaseConfig {
    
    private static final Properties properties = new Properties();
    private static boolean loaded = false;
    
    static {
        loadProperties();
    }
    
    /**
     * Load properties dari file application.properties
     */
    private static void loadProperties() {
        if (loaded) return;
        
        try (InputStream input = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            
            if (input == null) {
                System.err.println("application.properties not found, using default values");
                setDefaultProperties();
                return;
            }
            
            properties.load(input);
            loaded = true;
            System.out.println("✅ Database configuration loaded from application.properties");
            
        } catch (IOException e) {
            System.err.println("Error loading application.properties: " + e.getMessage());
            setDefaultProperties();
        }
    }
    
    /**
     * Set default properties jika file tidak ditemukan
     */
    private static void setDefaultProperties() {
        properties.setProperty("db.url", "jdbc:postgresql://localhost:5432/agripos");
        properties.setProperty("db.username", "postgres");
        properties.setProperty("db.password", "242424");
        properties.setProperty("db.driver", "org.postgresql.Driver");
        loaded = true;
    }
    
    /**
     * Get database URL
     */
    public static String getUrl() {
        return properties.getProperty("db.url", "jdbc:postgresql://localhost:5432/agripos");
    }
    
    /**
     * Get database username
     */
    public static String getUsername() {
        return properties.getProperty("db.username", "postgres");
    }
    
    /**
     * Get database password
     */
    public static String getPassword() {
        return properties.getProperty("db.password", "242424");
    }
    
    /**
     * Get database driver class name
     */
    public static String getDriver() {
        return properties.getProperty("db.driver", "org.postgresql.Driver");
    }
    
    /**
     * Get property by key
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get property by key with default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Print all configuration
     */
    public static void printConfig() {
        System.out.println("=".repeat(60));
        System.out.println("DATABASE CONFIGURATION");
        System.out.println("=".repeat(60));
        System.out.println("URL      : " + maskPassword(getUrl()));
        System.out.println("Username : " + getUsername());
        System.out.println("Password : " + maskPassword(getPassword()));
        System.out.println("Driver   : " + getDriver());
        System.out.println("=".repeat(60));
    }
    
    /**
     * Mask password untuk keamanan saat print
     */
    private static String maskPassword(String text) {
        if (text == null || text.length() <= 4) {
            return "****";
        }
        return text.substring(0, 2) + "****" + text.substring(text.length() - 2);
    }
}
