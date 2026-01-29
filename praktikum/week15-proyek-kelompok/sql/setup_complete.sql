-- =========================================
-- SETUP DATABASE AGRIPOS - JALANKAN SATU PER SATU
-- =========================================

-- LANGKAH 1: Buat database (jalankan sebagai user postgres)
-- Jika database sudah ada, skip langkah ini
CREATE DATABASE agripos
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE agripos IS 'Database untuk aplikasi AgriPOS';

-- =========================================
-- LANGKAH 2: Connect ke database agripos
-- Klik database 'agripos' di sidebar pgAdmin
-- lalu buka Query Tool baru
-- =========================================

-- =========================================
-- LANGKAH 3: Jalankan semua query di bawah ini
-- =========================================

-- DROP TABLE (URUTAN AMAN)
DROP TABLE IF EXISTS sales_items CASCADE;
DROP TABLE IF EXISTS inventory CASCADE;
DROP TABLE IF EXISTS sales CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS promos CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- USERS
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100),
    role VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- CATEGORIES
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PROMOS
CREATE TABLE promos (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    discount DECIMAL(5,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PRODUCTS
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    category_id INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INTEGER DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
);

-- SALES
CREATE TABLE sales (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2),
    payment_method VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sales_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

-- SALES ITEMS
CREATE TABLE sales_items (
    id SERIAL PRIMARY KEY,
    sale_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sales_items_sale
        FOREIGN KEY (sale_id)
        REFERENCES sales(id),
    CONSTRAINT fk_sales_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);

-- INVENTORY
CREATE TABLE inventory (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL,
    quantity_in INTEGER DEFAULT 0,
    quantity_out INTEGER DEFAULT 0,
    current_stock INTEGER DEFAULT 0,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);

-- INDEXES
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_sales_user ON sales(user_id);
CREATE INDEX idx_sales_items_sale ON sales_items(sale_id);
CREATE INDEX idx_sales_items_product ON sales_items(product_id);
CREATE INDEX idx_inventory_product ON inventory(product_id);
CREATE INDEX idx_promos_code ON promos(code);

-- =========================================
-- INSERT DATA AWAL
-- =========================================

-- CATEGORIES
INSERT INTO categories (name, description) VALUES
('Sembako', 'Kebutuhan pokok sehari-hari');

-- PROMOS
INSERT INTO promos (code, name, discount, start_date, end_date, active) VALUES
('PROMO10', 'Diskon 10%', 0.10, '2026-01-01', '2026-02-28', TRUE),
('PROMO20', 'Diskon 20%', 0.20, '2026-01-15', '2026-03-15', TRUE);

-- PRODUCTS
INSERT INTO products (code, name, category_id, price, stock, description) VALUES
('P001', 'Beras 10kg',        1, 12000, 50, 'Beras putih kualitas premium'),
('P002', 'Gula 1kg',          1, 15000, 30, 'Gula pasir kemasan 1kg'),
('P003', 'Minyak Goreng 2L',  1, 25000, 25, 'Minyak goreng kemasan 2 liter'),
('P004', 'Telur 1kg',         1, 18000, 40, 'Telur ayam negeri'),
('P005', 'Tepung Terigu 1kg', 1,  8000, 20, 'Tepung terigu serbaguna');

-- USERS (DEFAULT LOGIN)
INSERT INTO users (username, password, full_name, role) VALUES
('admin', 'admin123', 'Administrator', 'ADMIN'),
('kasir', 'kasir123', 'Kasir Toko', 'KASIR');

-- INVENTORY
INSERT INTO inventory (product_id, quantity_in, current_stock) VALUES
(1, 50, 50),
(2, 30, 30),
(3, 25, 25),
(4, 40, 40),
(5, 20, 20);

-- =========================================
-- VERIFIKASI DATA
-- =========================================

SELECT 'CATEGORIES' as table_name, COUNT(*) as total_rows FROM categories
UNION ALL
SELECT 'PROMOS', COUNT(*) FROM promos
UNION ALL
SELECT 'PRODUCTS', COUNT(*) FROM products
UNION ALL
SELECT 'USERS', COUNT(*) FROM users
UNION ALL
SELECT 'INVENTORY', COUNT(*) FROM inventory;

-- Jika semua berjalan sukses, Anda akan melihat:
-- CATEGORIES: 1
-- PROMOS: 2
-- PRODUCTS: 5
-- USERS: 2
-- INVENTORY: 5

SELECT 'Database AgriPOS berhasil dibuat dan diisi dengan data!' as status;
