package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utility class untuk test koneksi database PostgreSQL
 */
public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("TESTING KONEKSI DATABASE POSTGRESQL");
        System.out.println("=".repeat(60));
        
        testConnection();
        testProducts();
        testPromos();
        testUsers();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST SELESAI");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Test koneksi dasar ke database
     */
    public static void testConnection() {
        System.out.println("\n[1] Testing koneksi database...");
        
        try (Connection conn = JdbcConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Koneksi database BERHASIL!");
                System.out.println("   Database: " + conn.getCatalog());
                System.out.println("   URL: " + conn.getMetaData().getURL());
            } else {
                System.out.println("❌ Koneksi database GAGAL!");
            }
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }
    
    /**
     * Test query tabel products
     */
    public static void testProducts() {
        System.out.println("\n[2] Testing tabel PRODUCTS...");
        
        String sql = "SELECT code, name, price, stock FROM products ORDER BY code LIMIT 5";
        
        try (Connection conn = JdbcConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 0;
            System.out.println("   Produk yang tersedia:");
            System.out.println("   " + "-".repeat(50));
            
            while (rs.next()) {
                count++;
                System.out.printf("   %s | %-20s | Rp %,10.0f | Stock: %d%n",
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );
            }
            
            System.out.println("   " + "-".repeat(50));
            System.out.println("✅ Total produk: " + count);
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }
    
    /**
     * Test query tabel promos
     */
    public static void testPromos() {
        System.out.println("\n[3] Testing tabel PROMOS...");
        
        String sql = "SELECT code, name, discount, start_date, end_date FROM promos ORDER BY code";
        
        try (Connection conn = JdbcConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 0;
            System.out.println("   Promo yang tersedia:");
            System.out.println("   " + "-".repeat(50));
            
            while (rs.next()) {
                count++;
                System.out.printf("   %s | %-15s | Diskon: %.0f%% | %s s/d %s%n",
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("discount") * 100,
                    rs.getDate("start_date"),
                    rs.getDate("end_date")
                );
            }
            
            System.out.println("   " + "-".repeat(50));
            System.out.println("✅ Total promo: " + count);
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }
    
    /**
     * Test query tabel users
     */
    public static void testUsers() {
        System.out.println("\n[4] Testing tabel USERS...");
        
        String sql = "SELECT username, full_name, role FROM users ORDER BY username";
        
        try (Connection conn = JdbcConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 0;
            System.out.println("   User yang terdaftar:");
            System.out.println("   " + "-".repeat(50));
            
            while (rs.next()) {
                count++;
                System.out.printf("   %-10s | %-20s | Role: %s%n",
                    rs.getString("username"),
                    rs.getString("full_name"),
                    rs.getString("role")
                );
            }
            
            System.out.println("   " + "-".repeat(50));
            System.out.println("✅ Total user: " + count);
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }
}
