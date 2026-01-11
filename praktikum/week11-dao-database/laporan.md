# Laporan Praktikum Minggu 11 
Topik: Data Access Object (DAO) dan CRUD Database dengan JDBC

## Identitas
- Nama  : Agan Chois
- NIM   : 240202893
- Kelas : 3IKRB

---

## Tujuan
1. Menjelaskan konsep Data Access Object (DAO) dalam pengembangan aplikasi OOP.
2. Menghubungkan aplikasi Java dengan basis data menggunakan JDBC.
3. Mengimplementasikan operasi CRUD (Create, Read, Update, Delete) secara lengkap.
4. Mengintegrasikan DAO dengan class aplikasi OOP sesuai prinsip desain yang baik.
---

## Dasar Teori
1. **Class** adalah blueprint (cetakan) yang digunakan untuk membuat objek dalam pemrograman berorientasi objek.
2. **Object** adalah hasil instansiasi dari class yang merepresentasikan data nyata beserta perilakunya.
3. **DAO (Data Access Object)** digunakan untuk memisahkan logika akses database dari logika bisnis aplikasi.
4. **JDBC (Java Database Connectivity)** berfungsi sebagai jembatan penghubung antara aplikasi Java dan database.
5. **Enkapsulasi** digunakan untuk menyembunyikan detail implementasi dan menjaga keamanan data melalui method getter dan setter.

---

## Langkah Praktikum
1. **Setup**

   * Menginstal PostgreSQL dan pgAdmin.
   * Membuat database `agripos` dan tabel `products`.
   * Membuat project Java menggunakan **Maven** dan menambahkan dependency **PostgreSQL JDBC Driver** pada `pom.xml`.

2. **Coding**

   * Membuat class model `Product`.
   * Membuat interface `ProductDAO`.
   * Mengimplementasikan DAO dengan JDBC pada `ProductDAOImpl`.
   * Membuat class `MainDAOTest` untuk menguji operasi CRUD.

3. **Run**

   * Menjalankan perintah `mvn clean compile`.
   * Menjalankan aplikasi dengan `mvn exec:java`.
   * Memastikan operasi CRUD berhasil dijalankan di database.

4. **File/Kode yang Dibuat**

   * `Product.java`
   * `ProductDAO.java`
   * `ProductDAOImpl.java`
   * `MainDAOTest.java`
   * `pom.xml`
   * `products.sql`

5. **Commit Message**

   ```
   week11-dao-database: implement CRUD DAO with JDBC PostgreSQL
   ```
---

## Kode Program
### 1. Basis Data

Gunakan PostgreSQL dengan ketentuan minimal berikut:

Nama database: `agripos`

Struktur tabel produk:
```sql
CREATE TABLE products (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    price DOUBLE PRECISION,
    stock INT
);
```

---

### 2. Class Model – Product

```java
package com.upb.agripos.model;

public class Product {
    private String code;
    private String name;
    private double price;
    private int stock;

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}
```

---

### 3. Interface DAO

```java
package com.upb.agripos.dao;

import java.util.List;
import com.upb.agripos.model.Product;

public interface ProductDAO {
    void insert(Product product) throws Exception;
    Product findByCode(String code) throws Exception;
    List<Product> findAll() throws Exception;
    void update(Product product) throws Exception;
    void delete(String code) throws Exception;
}
```

---

### 4. Implementasi DAO dengan JDBC

Implementasi DAO harus menggunakan PreparedStatement.

```java
package com.upb.agripos.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.upb.agripos.model.Product;

public class ProductDAOImpl implements ProductDAO {

    private final Connection connection;

    public ProductDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Product p) throws Exception {
        String sql = "INSERT INTO products(code, name, price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.executeUpdate();
        }
    }

    @Override
    public Product findByCode(String code) throws Exception {
        String sql = "SELECT * FROM products WHERE code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Product> findAll() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                ));
            }
        }
        return list;
    }

    @Override
    public void update(Product p) throws Exception {
        String sql = "UPDATE products SET name=?, price=?, stock=? WHERE code=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getCode());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String code) throws Exception {
        String sql = "DELETE FROM products WHERE code=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }
}
```

---

## Integrasi DAO dengan Aplikasi

DAO tidak boleh dipanggil langsung oleh UI. Integrasi dilakukan melalui class aplikasi (misalnya `MainDAOTest`) atau service.

```java
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
```
---

## Hasil Eksekusi
![Screenshot hasil](screenshots/HasilWeek11.png)
---

## Analisis
Pada praktikum ini, program berjalan dengan cara aplikasi Java membuat koneksi ke database PostgreSQL menggunakan JDBC, kemudian operasi CRUD (Create, Read, Update, Delete) dilakukan melalui class DAO sehingga logika akses data terpisah dari logika utama aplikasi. Dibandingkan dengan minggu sebelumnya yang masih menggunakan objek atau struktur data sederhana tanpa database, pendekatan minggu ini sudah terintegrasi langsung dengan basis data sehingga data tersimpan secara permanen. Kendala yang dihadapi adalah error driver JDBC tidak ditemukan dan kegagalan autentikasi PostgreSQL, yang diatasi dengan menambahkan dependency PostgreSQL pada pom.xml, menjalankan Maven dari direktori yang benar, serta memastikan username dan password database sesuai dengan konfigurasi PostgreSQL.
---

## Kesimpulan
Praktikum ini menunjukkan bahwa penggunaan pola DAO dan JDBC membuat aplikasi lebih terstruktur, mudah dikembangkan, dan terpisah dengan baik antara logika bisnis dan akses database. Dengan integrasi PostgreSQL, data dapat disimpan dan dikelola secara permanen melalui operasi CRUD. Meskipun terdapat kendala pada konfigurasi driver dan koneksi database, masalah tersebut dapat diatasi dengan pengaturan dependency dan konfigurasi yang benar, sehingga aplikasi dapat berjalan sesuai dengan tujuan praktikum.

---

## Quiz
1. **Implementasikan DAO Product menggunakan JDBC**
   **Jawaban:** DAO diimplementasikan dengan membuat interface `ProductDAO` dan class `ProductDAOImpl` yang menggunakan JDBC (`Connection`, `PreparedStatement`, dan `ResultSet`) untuk mengakses tabel `products` pada database PostgreSQL.

2. **Jalankan operasi CRUD lengkap**
   **Jawaban:** Operasi CRUD dijalankan melalui method `insert()`, `findByCode()`, `findAll()`, `update()`, dan `delete()` yang diuji pada class `MainDAOTest` sehingga data dapat ditambah, dibaca, diubah, dan dihapus dari database.

3. **Integrasikan DAO dengan class aplikasi OOP**
   **Jawaban:** DAO diintegrasikan dengan class aplikasi `MainDAOTest` sebagai penghubung antara program utama dan database, tanpa memanggil database secara langsung dari UI, sehingga sesuai dengan prinsip OOP dan pemisahan tanggung jawab.

