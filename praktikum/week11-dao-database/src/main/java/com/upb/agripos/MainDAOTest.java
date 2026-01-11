package com.upb.agripos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.model.Product;

public class MainDAOTest {

    public static void main(String[] args) {
        // IDENTITAS MAHASISWA
        System.out.println("=================================");
        System.out.println("Nama  : Agan Chois");      
        System.out.println("Kelas : 3IKRB");        
        System.out.println("NIM   : 240202893");
        System.out.println("=================================\n");

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/agripos",
                    "postgres",
                    "242424"
            );

            ProductDAO dao = new ProductDAOImpl(conn);

            // CREATE
            dao.insert(new Product("P01", "Pupuk Organik", 25000, 10));

            // UPDATE
            dao.update(new Product("P01", "Pupuk Organik Premium", 30000, 8));

            // READ BY CODE
            Product p = dao.findByCode("P01");
            System.out.println("Produk: " + p.getName());

            // READ ALL
            List<Product> list = dao.findAll();
            for (Product pr : list) {
                System.out.println(pr.getCode() + " - " + pr.getName());
            }

            // DELETE
            dao.delete("P01");

            conn.close();
            System.out.println("\nCRUD DAO selesai dijalankan.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

