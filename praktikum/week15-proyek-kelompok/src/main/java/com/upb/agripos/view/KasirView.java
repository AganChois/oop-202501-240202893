package com.upb.agripos.view;

import java.time.LocalDate;
import java.time.LocalTime;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.ItemTransaksi;
import com.upb.agripos.model.Product;
import com.upb.agripos.model.Promo;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.repository.DataRepository;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class KasirView {

    private final Stage stage;
    private final String username;
    private final DataRepository dataRepository = DataRepository.getInstance();
    private final ObservableList<CartItem> cart = FXCollections.observableArrayList();
    private final Label totalLabel = new Label("Rp 0");
    private final Label stockLabel = new Label("Sisa Stok: -");
    private final Label promoStatusLabel = new Label("");
    private double promoDiscount = 0;
    
    // Produk yang tersedia - gunakan data dari repository yang shared
    private final ObservableList<Product> products = dataRepository.getProducts();
    // Promo yang tersedia - gunakan data dari repository yang shared
    private final ObservableList<Promo> promos = dataRepository.getPromos();

    public KasirView(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
    }

    public void show() {
        // ========================
        // LAYOUT UTAMA
        // ========================
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");
        
        // Header
        mainLayout.setTop(createHeader());
        
        // Center - Main content
        mainLayout.setCenter(createMainContent());
        
        // Bottom - Total dan tombol pembayaran
        mainLayout.setBottom(createFooter());
        
        stage.setTitle("AGRIPOS - Menu Kasir");
        stage.setScene(new Scene(mainLayout, 1000, 750));
        stage.show();
    }

    private VBox createHeader() {
        Label titleLabel = new Label("SISTEM KASIR AgriPOS");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label subtitleLabel = new Label("Toko Pertanian Digital");
        subtitleLabel.setFont(Font.font("System", 12));
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");
        
        // Bagian kiri
        VBox leftSection = new VBox(8);
        leftSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Bagian kanan - Username dan Logout
        Label userLabel = new Label("Halo, " + username);
        userLabel.setFont(Font.font("System", 12));
        userLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Button logoutBtn = new Button("LOGOUT");
        logoutBtn.setStyle("-fx-font-size: 11; -fx-padding: 6 12; -fx-background-color: #e74c3c; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> {
            new LoginView(stage).show();
        });
        
        HBox rightSection = new HBox(10);
        rightSection.setAlignment(Pos.CENTER_RIGHT);
        rightSection.getChildren().addAll(userLabel, logoutBtn);
        
        // Header layout
        HBox headerContent = new HBox();
        headerContent.setPadding(new Insets(15));
        headerContent.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 0 0 1 0;");
        headerContent.getChildren().addAll(leftSection, rightSection);
        HBox.setHgrow(rightSection, Priority.ALWAYS);
        
        // Wrap dalam VBox untuk return type yang sesuai
        VBox header = new VBox();
        header.getChildren().add(headerContent);
        
        return header;
    }

    private HBox createMainContent() {
        // ========================
        // LEFT PANEL - Pilih Produk
        // ========================
        VBox leftPanel = createProductPanel();
        
        // ========================
        // RIGHT PANEL - Keranjang & Total
        // ========================
        VBox rightPanel = createCartPanel();
        
        // Layout horizontal
        HBox mainContent = new HBox(15);
        mainContent.setPadding(new Insets(15));
        mainContent.getChildren().addAll(leftPanel, rightPanel);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        
        return mainContent;
    }

    private VBox createProductPanel() {
        Label productLabel = new Label("PILIH PRODUK");
        productLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        productLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // ========================
        // BARCODE / KODE PRODUK INPUT
        // ========================
        Label barcodeLabel = new Label("SCAN BARCODE / KODE PRODUK");
        barcodeLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        barcodeLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        TextField barcodeField = new TextField();
        barcodeField.setPromptText("Scan barcode atau ketik kode produk (P001-P005)");
        barcodeField.setPrefWidth(200);
        barcodeField.setStyle("-fx-font-size: 11; -fx-padding: 6;");
        
        ComboBox<Product> productCombo = new ComboBox<>(products);
        productCombo.setPromptText("Atau pilih dari dropdown...");
        productCombo.setPrefWidth(200);
        productCombo.setCellFactory(param -> new javafx.scene.control.ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - Rp " + item.getPrice());
                }
            }
        });
        productCombo.setButtonCell(new javafx.scene.control.ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Pilih produk...");
                } else {
                    setText(item.getName() + " - Rp " + item.getPrice());
                }
            }
        });
        
        // Update stock label saat produk dipilih
        productCombo.setOnAction(e -> {
            if (productCombo.getValue() != null) {
                stockLabel.setText("Sisa Stok: " + productCombo.getValue().getStock() + " pcs");
            }
        });
        
        // Search barcode
        barcodeField.setOnAction(e -> {
            String code = barcodeField.getText().trim().toUpperCase();
            Product found = products.stream()
                .filter(p -> p.getId().equals(code))
                .findFirst()
                .orElse(null);
            
            if (found != null) {
                productCombo.setValue(found);
                barcodeField.clear();
            } else {
                showError("Kode produk tidak ditemukan!");
            }
        });
        
        // Stock info
        stockLabel.setFont(Font.font("System", 11));
        stockLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        
        Label qtyLabel = new Label("JUMLAH");
        qtyLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        qtyLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        TextField qtyField = new TextField();
        qtyField.setPromptText("Masukkan jumlah");
        qtyField.setPrefWidth(200);
        qtyField.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        
        Button addBtn = new Button("TAMBAH KE KERANJANG");
        addBtn.setPrefWidth(200);
        addBtn.setPrefHeight(40);
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        
        addBtn.setOnAction(e -> {
            if (productCombo.getValue() == null) {
                showError("Pilih produk terlebih dahulu!");
                return;
            }
            
            try {
                int qty = Integer.parseInt(qtyField.getText());
                Product selectedProduct = productCombo.getValue();
                
                if (qty <= 0) {
                    showError("Jumlah harus lebih dari 0");
                    return;
                }
                
                if (qty > selectedProduct.getStock()) {
                    showError("Stok tidak cukup! Stok tersedia: " + selectedProduct.getStock());
                    return;
                }
                
                // Cek apakah produk sudah ada di keranjang
                boolean exists = cart.stream().anyMatch(item -> item.getProduct().getId().equals(selectedProduct.getId()));
                
                if (exists) {
                    showError("Produk sudah ada di keranjang. Gunakan tombol Update untuk mengubah jumlah.");
                    return;
                }
                
                cart.add(new CartItem(selectedProduct, qty));
                selectedProduct.reduceStock(qty);
                updateTotal();
                
                productCombo.setValue(null);
                qtyField.clear();
                barcodeField.clear();
                stockLabel.setText("Sisa Stok: -");
                showSuccess("Produk berhasil ditambahkan ke keranjang!");
                
            } catch (NumberFormatException ex) {
                showError("Masukkan angka yang valid!");
            }
        });
        
        VBox leftPanel = new VBox(12);
        leftPanel.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-padding: 15; -fx-background-color: white;");
        leftPanel.setPrefWidth(280);
        leftPanel.getChildren().addAll(
            productLabel,
            barcodeLabel,
            barcodeField,
            productCombo,
            stockLabel,
            qtyLabel,
            qtyField,
            addBtn
        );
        
        return leftPanel;
    }

    private VBox createCartPanel() {
        Label cartLabel = new Label("KERANJANG BELANJA");
        cartLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        cartLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // ========================
        // TABLE KERANJANG
        // ========================
        TableView<CartItem> cartTable = new TableView<>(cart);
        cartTable.setStyle("-fx-font-size: 11;");
        
        TableColumn<CartItem, String> noCol = new TableColumn<>("No");
        noCol.setPrefWidth(40);
        noCol.setCellValueFactory(param -> {
            int index = cartTable.getItems().indexOf(param.getValue()) + 1;
            return new SimpleObjectProperty<>(String.valueOf(index));
        });
        
        TableColumn<CartItem, String> nameCol = new TableColumn<>("Produk");
        nameCol.setPrefWidth(120);
        nameCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getProduct().getName()));
        
        TableColumn<CartItem, Double> priceCol = new TableColumn<>("Harga");
        priceCol.setPrefWidth(80);
        priceCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getProduct().getPrice()));
        priceCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        TableColumn<CartItem, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setPrefWidth(60);
        qtyCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getQty()));
        qtyCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<CartItem, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setPrefWidth(100);
        subtotalCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getSubtotal()));
        subtotalCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        TableColumn<CartItem, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setPrefWidth(70);
        actionCol.setCellFactory(param -> new javafx.scene.control.TableCell<CartItem, Void>() {
            private final Button deleteBtn = new Button("Hapus");
            
            {
                deleteBtn.setStyle("-fx-font-size: 10; -fx-padding: 4; -fx-background-color: #e74c3c; -fx-text-fill: white;");
                deleteBtn.setOnAction(e -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    item.getProduct().addStock(item.getQty());
                    cart.remove(item);
                    updateTotal();
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });
        
        cartTable.getColumns().addAll(noCol, nameCol, priceCol, qtyCol, subtotalCol, actionCol);
        cartTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        
        VBox cartPanel = new VBox(12);
        cartPanel.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-padding: 15; -fx-background-color: white;");
        cartPanel.getChildren().addAll(cartLabel, cartTable);
        VBox.setVgrow(cartTable, Priority.ALWAYS);
        
        return cartPanel;
    }

    private HBox createFooter() {
        VBox checkoutArea = new VBox(10);
        checkoutArea.setStyle("-fx-padding: 15;");
        
        // ========================
        // KODE PROMO / DISKON
        // ========================
        HBox promoBox = new HBox(8);
        promoBox.setAlignment(Pos.CENTER_LEFT);
        
        Label promoLabel = new Label("Kode Promo:");
        promoLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        promoLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        TextField promoField = new TextField();
        promoField.setPromptText("Masukkan kode promo");
        promoField.setPrefWidth(150);
        promoField.setStyle("-fx-font-size: 11; -fx-padding: 6;");
        
        Button promoBtn = new Button("GUNAKAN");
        promoBtn.setStyle("-fx-font-size: 11; -fx-padding: 6 12; -fx-background-color: #3498db; -fx-text-fill: white;");
        promoBtn.setOnAction(e -> {
            String promoCode = promoField.getText().trim().toUpperCase();
            
            // Cari promo di database berdasarkan kode
            Promo foundPromo = null;
            for (Promo promo : promos) {
                if (promo.getCode().equalsIgnoreCase(promoCode)) {
                    foundPromo = promo;
                    break;
                }
            }
            
            if (foundPromo != null) {
                // Validasi tanggal promo (apakah masih aktif)
                LocalDate today = LocalDate.now();
                if (today.isBefore(foundPromo.getStartDate())) {
                    showError("Promo belum dimulai!\nTanggal mulai: " + foundPromo.getStartDate());
                    promoStatusLabel.setText("");
                } else if (today.isAfter(foundPromo.getEndDate())) {
                    showError("Promo sudah berakhir!\nTanggal berakhir: " + foundPromo.getEndDate());
                    promoStatusLabel.setText("");
                } else if (!foundPromo.isActive()) {
                    showError("Promo tidak aktif!");
                    promoStatusLabel.setText("");
                } else {
                    // Promo valid - terapkan diskon
                    promoDiscount = foundPromo.getDiscount();
                    int discountPercent = (int)(foundPromo.getDiscount() * 100);
                    promoStatusLabel.setText("✓ Diskon " + discountPercent + "% berhasil digunakan!");
                    promoStatusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    updateTotal();
                    promoField.clear();
                    showSuccess("Kode promo " + foundPromo.getCode() + " berhasil digunakan!\nDiskon " + discountPercent + "% diterapkan (" + foundPromo.getName() + ").");
                }
            } else {
                showError("Kode promo tidak ditemukan!\nMohon periksa kembali kode promo Anda.");
                promoStatusLabel.setText("");
            }
        });
        
        promoStatusLabel.setFont(Font.font("System", 11));
        promoBox.getChildren().addAll(promoLabel, promoField, promoBtn, promoStatusLabel);
        
        // ========================
        // TOTAL DAN METODE BAYAR
        // ========================
        HBox totalBox = new HBox(15);
        totalBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox totalSection = new VBox(8);
        Label totalTextLabel = new Label("TOTAL:");
        totalTextLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        totalTextLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        totalLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        totalLabel.setStyle("-fx-text-fill: #27ae60;");
        
        HBox totalDisplayBox = new HBox(10);
        totalDisplayBox.setAlignment(Pos.CENTER_LEFT);
        totalDisplayBox.getChildren().addAll(totalTextLabel, totalLabel);
        
        totalSection.getChildren().addAll(totalDisplayBox);
        
        // Metode Pembayaran
        VBox paymentMethodSection = new VBox(8);
        Label methodLabel = new Label("METODE PEMBAYARAN:");
        methodLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        methodLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        ToggleGroup paymentGroup = new ToggleGroup();
        
        RadioButton tunaiRadio = new RadioButton("Tunai (Hitung Kembalian)");
        tunaiRadio.setToggleGroup(paymentGroup);
        tunaiRadio.setSelected(true);
        tunaiRadio.setStyle("-fx-font-size: 11;");
        
        RadioButton ewalletRadio = new RadioButton("E-Wallet (Gateway)");
        ewalletRadio.setToggleGroup(paymentGroup);
        ewalletRadio.setStyle("-fx-font-size: 11;");
        
        HBox paymentMethodBox = new HBox(20);
        paymentMethodBox.getChildren().addAll(tunaiRadio, ewalletRadio);
        
        paymentMethodSection.getChildren().addAll(methodLabel, paymentMethodBox);
        
        totalBox.getChildren().addAll(totalSection, paymentMethodSection);
        HBox.setHgrow(paymentMethodSection, Priority.ALWAYS);
        
        // ========================
        // TOMBOL AKSI
        // ========================
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button clearBtn = new Button("BATAL");
        clearBtn.setPrefHeight(40);
        clearBtn.setPrefWidth(120);
        clearBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        clearBtn.setOnAction(e -> {
            cart.forEach(item -> item.getProduct().addStock(item.getQty()));
            cart.clear();
            promoDiscount = 0;
            promoStatusLabel.setText("");
            updateTotal();
            showInfo("Keranjang telah dikosongkan");
        });
        
        Button bayarBtn = new Button("PROSES BAYAR");
        bayarBtn.setPrefHeight(40);
        bayarBtn.setPrefWidth(150);
        bayarBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        bayarBtn.setOnAction(e -> {
            if (cart.isEmpty()) {
                showError("Keranjang kosong! Tambahkan produk terlebih dahulu.");
                return;
            }
            
            String paymentMethod = tunaiRadio.isSelected() ? "TUNAI" : "E-WALLET";
            double subtotal = cart.stream().mapToDouble(CartItem::getSubtotal).sum();
            double discountAmount = subtotal * promoDiscount;
            double total = subtotal - discountAmount;
            
            StringBuilder receipt = new StringBuilder("=== STRUK PEMBAYARAN ===\n\n");
            receipt.append("Metode Pembayaran: ").append(paymentMethod).append("\n\n");
            receipt.append("--- DETAIL BARANG ---\n");
            cart.forEach(item -> 
                receipt.append(item.getProduct().getName())
                       .append(" x").append(item.getQty())
                       .append(" = Rp ").append((long)item.getSubtotal()).append("\n")
            );
            receipt.append("\nSubtotal: Rp ").append((long)subtotal);
            
            if (promoDiscount > 0) {
                receipt.append("\nDiskon (").append((int)(promoDiscount*100)).append("%): -Rp ").append((long)discountAmount);
            }
            
            receipt.append("\n\nTOTAL: Rp ").append((long)total);
            
            // Generate transaction ID
            String transactionId = "TRX" + System.currentTimeMillis();
            
            if (tunaiRadio.isSelected()) {
                receipt.append("\n\n--- PEMBAYARAN TUNAI ---");
                receipt.append("\nKasir: ").append(username);
            } else {
                receipt.append("\n\n--- PEMBAYARAN E-WALLET ---");
                receipt.append("\nProses melalui Payment Gateway");
                receipt.append("\nKasir: ").append(username);
            }
            
            receipt.append("\n\nNo. Transaksi: ").append(transactionId);
            receipt.append("\nWaktu: ").append(LocalTime.now());
            
            // Simpan transaksi ke repository
            // Convert CartItem ke ItemTransaksi
            java.util.List<ItemTransaksi> transactionItems = new java.util.ArrayList<>();
            for (CartItem item : cart) {
                transactionItems.add(new ItemTransaksi(item.getProduct(), item.getQty()));
            }
            
            Transaction transaction = new Transaction(
                transactionId,
                transactionItems,
                subtotal,
                discountAmount,
                total,
                paymentMethod,
                LocalDate.now(),
                LocalTime.now(),
                username
            );
            dataRepository.addTransaction(transaction);
            
            showSuccess(receipt.toString());
            
            // Reset (stok sudah dikurangi saat produk ditambah ke keranjang)
            cart.clear();
            promoDiscount = 0;
            promoStatusLabel.setText("");
            updateTotal();
        });
        
        buttonBox.getChildren().addAll(clearBtn, bayarBtn);
        
        checkoutArea.getChildren().addAll(promoBox, totalBox, buttonBox);
        
        HBox footer = new HBox();
        footer.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 1 0 0 0;");
        footer.getChildren().add(checkoutArea);
        HBox.setHgrow(checkoutArea, Priority.ALWAYS);
        
        return footer;
    }

    // ========================
    // HELPER METHODS
    // ========================
    private void updateTotal() {
        double subtotal = cart.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
        double discountAmount = subtotal * promoDiscount;
        double total = subtotal - discountAmount;
        totalLabel.setText("Rp " + (long)total);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
