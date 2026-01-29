# Panduan Setup Database PostgreSQL untuk AgriPOS

## Langkah-langkah Setup

### 1. Install PostgreSQL dan pgAdmin
- Download dari https://www.postgresql.org/download/
- Pilih version terbaru (15 atau 16)
- Selama instalasi, ingat password untuk user `postgres` (gunakan: `242424`)
- pgAdmin akan diinstall bersama

### 2. Buka pgAdmin
- Buka browser dan akses http://localhost:5050
- Login dengan email: `postgres@pgadmin.org` dan password: `admin` (default)
- Atau konfigurasi email saat instalasi

### 3. Buat Database Baru
1. Di pgAdmin, klik kanan pada **Databases**
2. Pilih **Create** → **Database**
3. Isi nama database: `agripos`
4. Klik **Save**

### 4. Jalankan SQL Schema
1. Di pgAdmin, buka Database `agripos`
2. Buka **Query Tool** (Tools → Query Tool)
3. Copy-paste seluruh kode dari file `sql/schema.sql`
4. Klik tombol **Execute** atau tekan F6

### 5. Jalankan SQL Seed (Data Awal)
1. Di Query Tool yang sama, clear semua query
2. Copy-paste seluruh kode dari file `sql/seed.sql`
3. Klik **Execute**

### 6. Verifikasi Database
1. Di pgAdmin, buka **Schemas** → **public** → **Tables**
2. Anda seharusnya melihat tabel-tabel:
   - users
   - categories
   - products
   - sales
   - sales_items
   - inventory

### 7. Konfigurasi Maven Dependencies
- Sudah ditambahkan PostgreSQL JDBC driver di `pom.xml`
- Jalankan: `mvn clean install` untuk download dependencies

### 8. Test Koneksi di Aplikasi
- Buka file `JdbcConnection.java`
- Ada method `testConnection()` yang bisa digunakan untuk tes
- Jalankan: `System.out.println(JdbcConnection.testConnection());`

## Struktur Tabel Database

### users
- id (Primary Key)
- username (Unique)
- password
- full_name
- role (ADMIN, CASHIER, MANAGER)
- created_at

### categories
- id (Primary Key)
- name (Unique)
- description
- created_at

### products
- id (Primary Key)
- name
- category_id (Foreign Key)
- price
- stock
- description
- created_at, updated_at

### sales
- id (Primary Key)
- user_id (Foreign Key)
- sale_date
- total_amount
- payment_method
- created_at

### sales_items
- id (Primary Key)
- sale_id (Foreign Key)
- product_id (Foreign Key)
- quantity
- unit_price
- subtotal
- created_at

### inventory
- id (Primary Key)
- product_id (Foreign Key)
- quantity_in
- quantity_out
- current_stock
- transaction_date
- notes
- created_at

## Troubleshooting

### Koneksi Ditolak
- Pastikan PostgreSQL Service sudah running
- Cek apakah port 5432 sudah digunakan
- Verifikasi username dan password di `JdbcConnection.java`

### Driver Tidak Ditemukan
- Jalankan: `mvn dependency:resolve`
- Bersihkan folder `.m2` jika perlu

### pgAdmin Error
- Restart pgAdmin service
- Clear browser cache
- Reset password pgAdmin di Command Prompt:
  ```
  pgAdmin4 --reset-password
  ```

## Data Sample yang Sudah Ditambahkan

### Users
- Username: `admin` / Password: `242424` (Role: ADMIN)
- Username: `kasir` / Password: `242424` (Role: CASHIER)
- Username: `manajer` / Password: `242424` (Role: MANAGER)

### Products Sample
- Tomat (Rp 5.000)
- Cabai (Rp 8.000)
- Wortel (Rp 4.000)
- Mangga (Rp 12.000)
- Pisang (Rp 3.000)
- Ayam (Rp 35.000)
- Ikan Laut (Rp 40.000)
- Garam (Rp 2.000)
- Merica (Rp 15.000)
