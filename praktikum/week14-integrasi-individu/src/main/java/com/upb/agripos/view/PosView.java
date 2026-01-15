package com.upb.agripos.view;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PosView {

    private final PosController controller;

    private TableView<Product> productTable = new TableView<>();
    private TableView<CartItem> cartTable = new TableView<>();

    private ObservableList<Product> productData = FXCollections.observableArrayList();
    private ObservableList<CartItem> cartData = FXCollections.observableArrayList();

    private Label totalLabel = new Label("Total Belanja: Rp 0");

    public PosView(PosController controller) {
        this.controller = controller;
        refreshProducts();
    }

    public Pane getRoot() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label header = new Label("=== AGRI-POS SYSTEM ===");
        header.getStyleClass().add("header");

        root.getChildren().addAll(
                header,
                productSection(),
                cartSection(),
                checkoutSection()
        );
        return root;
    }

    // ================= PRODUK =================
    private VBox productSection() {
        TextField tfCode = new TextField();
        TextField tfName = new TextField();
        TextField tfPrice = new TextField();
        TextField tfStock = new TextField();

        Button btnAdd = new Button("Tambah Produk");
        Button btnDelete = new Button("Hapus Produk");

        btnAdd.setOnAction(e -> {
            Product p = new Product(
                    tfCode.getText(),
                    tfName.getText(),
                    Double.parseDouble(tfPrice.getText()),
                    Integer.parseInt(tfStock.getText())
            );
            controller.addProduct(p);
            refreshProducts();
        });

        btnDelete.setOnAction(e -> {
            Product p = productTable.getSelectionModel().getSelectedItem();
            if (p != null) {
                controller.deleteProduct(p.getCode());
                refreshProducts();
            }
        });

        productTable.getColumns().setAll(
                col("Kode", "code"),
                col("Nama", "name"),
                col("Harga", "price"),
                col("Stok", "stock")
        );
        productTable.setItems(productData);

        VBox box = new VBox(5,
                new Label("Manajemen Produk"),
                new GridPane() {{
                    setHgap(5); setVgap(5);
                    addRow(0, new Label("Kode"), tfCode);
                    addRow(1, new Label("Nama"), tfName);
                    addRow(2, new Label("Harga"), tfPrice);
                    addRow(3, new Label("Stok"), tfStock);
                }},
                new HBox(5, btnAdd, btnDelete),
                productTable
        );
        box.getStyleClass().add("section");
        return box;
    }

    // ================= KERANJANG =================
    private VBox cartSection() {
        TextField tfQty = new TextField();
        tfQty.setPromptText("Jumlah");

        Button btnAddCart = new Button("Tambah ke Keranjang");
        Button btnRemove = new Button("Hapus Item");
        Button btnClear = new Button("Clear Keranjang");

        btnAddCart.setOnAction(e -> {
            Product p = productTable.getSelectionModel().getSelectedItem();
            if (p != null && !tfQty.getText().isEmpty()) {
                controller.addToCart(p, Integer.parseInt(tfQty.getText()));
                refreshCart();
            }
        });

        btnRemove.setOnAction(e -> {
            CartItem item = cartTable.getSelectionModel().getSelectedItem();
            if (item != null) {
                controller.removeCartItem(item);
                refreshCart();
            }
        });

        btnClear.setOnAction(e -> {
            controller.clearCart();
            refreshCart();
        });

        // ===== FIXED TABLE COLUMN =====
        TableColumn<CartItem, String> codeCol = new TableColumn<>("Kode");
        codeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProduct().getCode())
        );

        TableColumn<CartItem, String> nameCol = new TableColumn<>("Nama");
        nameCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProduct().getName())
        );

        TableColumn<CartItem, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getQuantity()).asObject()
        );

        TableColumn<CartItem, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getSubtotal()).asObject()
        );

        cartTable.getColumns().setAll(codeCol, nameCol, qtyCol, subtotalCol);
        cartTable.setItems(cartData);

        VBox box = new VBox(5,
                new Label("Keranjang Belanja"),
                new HBox(5, tfQty, btnAddCart),
                cartTable,
                new HBox(5, btnRemove, btnClear)
        );
        box.getStyleClass().add("section");
        return box;
    }

    // ================= CHECKOUT =================
    private VBox checkoutSection() {
        Button btnCheckout = new Button("Checkout");

        btnCheckout.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Checkout");
            alert.setHeaderText("Transaksi Berhasil");
            alert.setContentText("Total: Rp " + controller.getCartTotal());
            alert.showAndWait();

            controller.clearCart();
            refreshCart();
        });

        VBox box = new VBox(5, totalLabel, btnCheckout);
        box.getStyleClass().add("section");
        return box;
    }

    // ================= UTIL =================
    private void refreshProducts() {
        productData.setAll(controller.getProducts());
    }

    private void refreshCart() {
        cartData.setAll(controller.getCartItems());
        totalLabel.setText("Total Belanja: Rp " + controller.getCartTotal());
    }

    private <T> TableColumn<T, String> col(String title, String prop) {
        TableColumn<T, String> c = new TableColumn<>(title);
        c.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
        return c;
    }
}
