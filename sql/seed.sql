-- =====================================================
-- RESET DATA
-- =====================================================
TRUNCATE TABLE
    sales_items,
    sales,
    inventory,
    products,
    categories,
    promos,
    users
RESTART IDENTITY CASCADE;

-- =====================================================
-- CATEGORIES
-- =====================================================
INSERT INTO categories (name, description) VALUES
('Sembako', 'Kebutuhan pokok sehari-hari');

-- =====================================================
-- PROMOS
-- =====================================================
INSERT INTO promos (code, name, discount, start_date, end_date, active) VALUES
('PROMO10', 'Diskon 10%', 0.10, '2026-01-01', '2026-02-28', TRUE),
('PROMO20', 'Diskon 20%', 0.20, '2026-01-15', '2026-03-15', TRUE);

-- =====================================================
-- PRODUCTS (SESUAI TAMPILAN APLIKASI)
-- =====================================================
INSERT INTO products (code, name, category_id, price, stock, description) VALUES
('P001', 'Beras 10kg',        1, 12000, 50, 'Beras putih kualitas premium'),
('P002', 'Gula 1kg',          1, 15000, 30, 'Gula pasir kemasan 1kg'),
('P003', 'Minyak Goreng 2L',  1, 25000, 25, 'Minyak goreng kemasan 2 liter'),
('P004', 'Telur 1kg',         1, 18000, 40, 'Telur ayam negeri'),
('P005', 'Tepung Terigu 1kg', 1,  8000, 20, 'Tepung terigu serbaguna');

-- =====================================================
-- USERS (DEFAULT LOGIN)
-- =====================================================
INSERT INTO users (username, password, full_name, role) VALUES
('admin', 'admin123', 'Administrator', 'ADMIN'),
('kasir', 'kasir123', 'Kasir Toko', 'KASIR');

-- =====================================================
-- INVENTORY
-- =====================================================
INSERT INTO inventory (product_id, quantity_in, current_stock) VALUES
(1, 50, 50),
(2, 30, 30),
(3, 25, 25),
(4, 40, 40),
(5, 20, 20);
