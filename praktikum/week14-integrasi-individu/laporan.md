# Laporan Praktikum Minggu 14 
Topik: Integrasi Individu (OOP + Database + GUI)

## Identitas
- Nama  : Agan Chois
- NIM   : 240202893
- Kelas : 3IKRB

---

## Tujuan
1. Mengintegrasikan konsep OOP (Bab 1–5) ke dalam satu aplikasi yang utuh.
2. Mengimplementasikan rancangan UML + SOLID (Bab 6) menjadi kode nyata.
3. Mengintegrasikan Collections + Keranjang (Bab 7) ke alur aplikasi.
4. Menerapkan exception handling (Bab 9) untuk validasi dan error flow.
5. Menerapkan pattern + unit testing (Bab 10) pada bagian yang relevan.
6. Menghubungkan aplikasi dengan database via DAO + JDBC (Bab 11).
7. Menyajikan aplikasi berbasis JavaFX (Bab 12–13) yang terhubung ke backend.

---

## Dasar Teori
1. MVC (Model View Controller) memisahkan logika aplikasi, tampilan, dan pengendali agar kode lebih terstruktur.
2. JavaFX digunakan untuk membangun antarmuka grafis berbasis event-driven.
3. Collection (List) digunakan untuk menyimpan data keranjang belanja secara dinamis.
4. JDBC digunakan untuk menghubungkan aplikasi Java dengan database.
5. Encapsulation memastikan data hanya diakses melalui method tertentu.

---

## Langkah Praktikum
1. Menyiapkan project Maven dan dependensi JavaFX serta JDBC.
2. Membuat struktur MVC (model, view, controller, service).
3. Mengimplementasikan manajemen produk terhubung database.
4. Membuat fitur keranjang belanja menggunakan Collection.
5. Menjalankan dan menguji aplikasi menggunakan mvn javafx:run.
6. Commit yang digunakan:
   week14-integrasi-javafx-mvc-pos
---

## Kode Program

```java
1️. AppJavaFX.java
package com.upb.agripos;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.dao.JdbcProductDAO;
import com.upb.agripos.service.CartService;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.PosView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        System.out.println("Hello World, I am Agan Chois-240202893");

        ProductService productService =
                new ProductService(new JdbcProductDAO());
        CartService cartService = new CartService();

        PosController controller =
                new PosController(productService, cartService);

        PosView view = new PosView(controller);

        Scene scene = new Scene(view.getRoot(), 900, 600);
        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );

        stage.setTitle("Agri-POS System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
2. Product.java
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
}

3. CartItem.java
package com.upb.agripos.model;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getCode() {
        return product.getCode();
    }

    public String getName() {
        return product.getName();
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }
}

4. Cart.java
package com.upb.agripos.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void add(Product product, int qty) {
        items.add(new CartItem(product, qty));
    }

    public void remove(CartItem item) {
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }
}
5. ProductDAO.java
package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> findAll();
    void insert(Product product);
    void delete(String code);
}

6. JdbcProductDAO.java
package com.upb.agripos.dao;

import com.upb.agripos.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDAO implements ProductDAO {

    private final String URL = "jdbc:postgresql://localhost:5432/agripos";
    private final String USER = "postgres";
    private final String PASS = "postgres";

    private Connection getConn() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection c = getConn();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void insert(Product p) {
        String sql = "INSERT INTO products VALUES (?,?,?,?)";
        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String code) {
        String sql = "DELETE FROM products WHERE code=?";
        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
7. ProductService.java
package com.upb.agripos.service;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.model.Product;
import java.util.List;

public class ProductService {
    private final ProductDAO dao;

    public ProductService(ProductDAO dao) {
        this.dao = dao;
    }

    public List<Product> getAll() {
        return dao.findAll();
    }

    public void add(Product p) {
        dao.insert(p);
    }

    public void delete(String code) {
        dao.delete(code);
    }
}

8. CartService.java
package com.upb.agripos.service;

import com.upb.agripos.model.Cart;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;

import java.util.List;

public class CartService {
    private final Cart cart = new Cart();

    public void add(Product p, int qty) {
        cart.add(p, qty);
    }

    public void remove(CartItem item) {
        cart.remove(item);
    }

    public void clear() {
        cart.clear();
    }

    public List<CartItem> items() {
        return cart.getItems();
    }

    public double total() {
        return cart.getTotal();
    }
}
9. PosController.java
package com.upb.agripos.controller;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.service.CartService;
import com.upb.agripos.service.ProductService;

import java.util.List;

public class PosController {

    private final ProductService productService;
    private final CartService cartService;

    public PosController(ProductService ps, CartService cs) {
        this.productService = ps;
        this.cartService = cs;
    }

    public List<Product> getProducts() {
        return productService.getAll();
    }

    public void addProduct(Product p) {
        productService.add(p);
    }

    public void deleteProduct(String code) {
        productService.delete(code);
    }

    public void addToCart(Product p, int qty) {
        cartService.add(p, qty);
    }

    public void removeCartItem(CartItem item) {
        cartService.remove(item);
    }

    public void clearCart() {
        cartService.clear();
    }

    public List<CartItem> getCartItems() {
        return cartService.items();
    }

    public double getCartTotal() {
        return cartService.total();
    }
}
10. PosView.java
package com.upb.agripos.view;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;

import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class PosView {

    private final PosController controller;

    private TableView<Product> productTable = new TableView<>();
    private TableView<CartItem> cartTable = new TableView<>();

    private ObservableList<Product> productData = FXCollections.observableArrayList();
    private ObservableList<CartItem> cartData = FXCollections.observableArrayList();

    private Label totalLabel = new Label("Total: Rp 0");

    public PosView(PosController controller) {
        this.controller = controller;
        refreshProducts();
    }

    public Pane getRoot() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        root.getChildren().addAll(
                new Label("AGRI-POS SYSTEM"),
                productSection(),
                cartSection(),
                checkoutSection()
        );
        return root;
    }

    private VBox productSection() {
        TextField c = new TextField();
        TextField n = new TextField();
        TextField p = new TextField();
        TextField s = new TextField();

        Button add = new Button("Tambah Produk");
        Button del = new Button("Hapus Produk");

        add.setOnAction(e -> {
            controller.addProduct(
                    new Product(c.getText(), n.getText(),
                            Double.parseDouble(p.getText()),
                            Integer.parseInt(s.getText()))
            );
            refreshProducts();
        });

        del.setOnAction(e -> {
            Product prod = productTable.getSelectionModel().getSelectedItem();
            if (prod != null) {
                controller.deleteProduct(prod.getCode());
                refreshProducts();
            }
        });

        productTable.getColumns().addAll(
                column("Kode", "code"),
                column("Nama", "name"),
                column("Harga", "price"),
                column("Stok", "stock")
        );
        productTable.setItems(productData);

        return new VBox(5, c, n, p, s, new HBox(5, add, del), productTable);
    }

    private VBox cartSection() {
        TextField qty = new TextField();
        Button add = new Button("Tambah ke Keranjang");
        Button del = new Button("Hapus");

        add.setOnAction(e -> {
            Product p = productTable.getSelectionModel().getSelectedItem();
            if (p != null) {
                controller.addToCart(p, Integer.parseInt(qty.getText()));
                refreshCart();
            }
        });

        del.setOnAction(e -> {
            CartItem i = cartTable.getSelectionModel().getSelectedItem();
            if (i != null) {
                controller.removeCartItem(i);
                refreshCart();
            }
        });

        cartTable.getColumns().addAll(
                column("Kode", "code"),
                column("Nama", "name"),
                column("Qty", "quantity"),
                column("Subtotal", "subtotal")
        );
        cartTable.setItems(cartData);

        return new VBox(5, qty, add, cartTable, del);
    }

    private VBox checkoutSection() {
        Button checkout = new Button("Checkout");
        checkout.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION,
                    "Total: Rp " + controller.getCartTotal());
            a.show();
            controller.clearCart();
            refreshCart();
        });
        return new VBox(5, totalLabel, checkout);
    }

    private void refreshProducts() {
        productData.setAll(controller.getProducts());
    }

    private void refreshCart() {
        cartData.setAll(controller.getCartItems());
        totalLabel.setText("Total: Rp " + controller.getCartTotal());
    }

    private <T> TableColumn<T, ?> column(String title, String prop) {
        TableColumn<T, Object> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        return c;
    }
}
11. CartServiceTest.java
package com.upb.agripos;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.CartService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    @Test
    void testTotal() {
        CartService cs = new CartService();
        cs.add(new Product("P1", "Test", 1000, 10), 2);
        assertEquals(2000, cs.total());
    }
}


---

## Hasil Eksekusi
![Screenshot hasil](screenshots/HasilWeek14.png)


## Analisis
Aplikasi berjalan dengan alur MVC di mana View menangani interaksi pengguna, Controller mengatur logika, dan Model menyimpan data produk serta keranjang. Dibandingkan minggu sebelumnya yang hanya fokus pada OOP dan DAO, praktikum ini menambahkan JavaFX dan integrasi antarmuka grafis. Kendala utama yang dihadapi adalah data kode dan nama produk tidak muncul di tabel keranjang, yang diatasi dengan memperbaiki TableColumn menggunakan CellValueFactory berbasis properti objek.

## Kesimpulan
Dengan mengintegrasikan JavaFX, MVC, database, dan Collection, aplikasi POS dapat berjalan secara interaktif, terstruktur, dan mudah dikembangkan, serta mampu menangani manajemen produk dan transaksi dengan baik.

