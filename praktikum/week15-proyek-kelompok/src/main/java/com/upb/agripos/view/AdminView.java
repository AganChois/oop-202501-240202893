package com.upb.agripos.view;

import java.time.LocalDate;

import com.upb.agripos.model.Product;
import com.upb.agripos.model.Promo;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.repository.DataRepository;
import com.upb.agripos.service.ProductService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminView {

    private final Stage stage;
    private final String username;
    private BorderPane mainLayout;
    private VBox contentArea;
    private TableView<Product> currentProductTable;
    private TableView<Promo> currentPromoTable;
    private final DataRepository dataRepository = DataRepository.getInstance();
    private final ProductService productService = new ProductService();
    
    // Gunakan data dari repository yang shared
    private final ObservableList<Product> products = dataRepository.getProducts();
    private final ObservableList<Promo> promos = dataRepository.getPromos();
    private final ObservableList<Transaction> transactions = dataRepository.getTransactions();
    
    // Track active menu button
    private Button activeMenuButton = null;
    private String activeMenuColor = "#ff9500";

    public AdminView(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
    }

    public void show() {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");
        
        // Header
        mainLayout.setTop(createHeader());
        
        // Sidebar
        mainLayout.setLeft(createSidebar());
        
        // Content area (center)
        contentArea = new VBox(20);
        contentArea.setStyle("-fx-background-color: #ffffff; -fx-padding: 20;");
        contentArea.setPrefWidth(800);
        showDashboard();
        
        mainLayout.setCenter(contentArea);
        
        stage.setTitle("AGRIPOS - Admin Panel");
        stage.setScene(new Scene(mainLayout, 1200, 700));
        stage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox(8);
        header.setStyle("-fx-background-color: linear-gradient(to bottom, #00df16c1, #55a32b); -fx-padding: 20px;");
        
        Label titleLabel = new Label("🛒 AgriPOS - Admin Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");
        
        Label userLabel = new Label("Login sebagai: " + username);
        userLabel.setFont(Font.font("System", 12));
        userLabel.setStyle("-fx-text-fill: #e8f5e9;");
        
        header.getChildren().addAll(titleLabel, userLabel);
        return header;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(2);
        sidebar.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20px; -fx-min-width: 200;");
        
        Label menuLabel = new Label("MENU");
        menuLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #ecf0f1; -fx-padding: 0 0 15 0;");
        
        // Dashboard Button
        Button dashboardBtn = createMenuButton("📊 Dashboard", "#ff9500", e -> showDashboard());
        activeMenuButton = dashboardBtn;
        
        // Produk Button
        Button produkBtn = createMenuButton("📦 Produk", "#34495e", e -> showProductManagement());
        
        // Promo Button
        Button promoBtn = createMenuButton("🎁 Promo", "#34495e", e -> showPromoManagement());
        
        // Laporan Button
        Button laporanBtn = createMenuButton("📈 Laporan Penjualan", "#34495e", e -> showSalesReport());
        
        // Logout Button
        Button logoutBtn = createMenuButton("🚪 Logout", "#e74c3c", e -> {
            new LoginView(stage).show();
        });
        
        sidebar.getChildren().addAll(
            menuLabel,
            dashboardBtn,
            produkBtn,
            promoBtn,
            laporanBtn,
            new Label(), // Separator
            logoutBtn
        );
        VBox.setVgrow(sidebar, Priority.ALWAYS);
        return sidebar;
    }

    private Button createMenuButton(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button btn = new Button(text);
        btn.setStyle("-fx-font-size: 13; -fx-padding: 12; -fx-background-color: " + color + "; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        btn.setPrefWidth(160);
        btn.setWrapText(true);
        
        btn.setOnAction(e -> {
            // Update active menu button
            setActiveMenuButton(btn, color);
            action.handle(e);
        });
        
        btn.setOnMouseEntered(e -> {
            if (btn != activeMenuButton) {
                if (color.equals("#e74c3c")) {
                    btn.setStyle("-fx-font-size: 13; -fx-padding: 12; -fx-background-color: #c0392b; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
                } else if (color.equals("#ff9500")) {
                    btn.setStyle("-fx-font-size: 13; -fx-padding: 12; -fx-background-color: #188732; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
                } else {
                    btn.setStyle("-fx-font-size: 13; -fx-padding: 12; -fx-background-color: #2c3e50; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #ff9500; -fx-border-width: 2;");
                }
            }
        });
        
        btn.setOnMouseExited(e -> {
            if (btn != activeMenuButton) {
                btn.setStyle("-fx-font-size: 13; -fx-padding: 12; -fx-background-color: " + color + "; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
            }
        });
        
        return btn;
    }
    
    private void setActiveMenuButton(Button btn, String color) {
        // Reset previous active button to original color
        if (activeMenuButton != null && activeMenuButton != btn) {
            // Determine original color based on button
            String originalColor = activeMenuColor;
            if (activeMenuColor.equals("#ff9500") && !color.equals("#ff9500")) {
                originalColor = "#34495e";
            }
            activeMenuButton.setStyle("-fx-font-size: 13; -fx-padding: 12; -fx-background-color: " + originalColor + "; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        }
        
        // Set new active button with highlight color
        String activeColor = "#ff9500";
        if (color.equals("#e74c3c")) {
            activeColor = "#c0392b";
        }
        btn.setStyle("-fx-font-size: 13; -fx-padding: 12; -fx-background-color: " + activeColor + "; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        
        activeMenuButton = btn;
        activeMenuColor = color;
    }

    private void showDashboard() {
        contentArea.getChildren().clear();
        
        Label dashboardTitle = new Label("Dashboard");
        dashboardTitle.setFont(Font.font("System", FontWeight.BOLD, 22));
        dashboardTitle.setStyle("-fx-text-fill: #2c3e50;");
        
        // Statistics cards
        VBox statsBox = createStatsCards();
        
        contentArea.getChildren().addAll(
            dashboardTitle,
            statsBox
        );
    }

    private VBox createStatsCards() {
        VBox statsBox = new VBox(15);
        statsBox.setPadding(new Insets(20));
        statsBox.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #f9f9f9;");
        
        // Stat card 1 - Total Produk
        VBox card1 = createStatCard("📦 Total Produk", String.valueOf(products.size()), "#3498db");
        
        // Stat card 2 - Total Stok
        int totalStock = products.stream().mapToInt(Product::getStock).sum();
        VBox card2 = createStatCard("📊 Total Stok", String.valueOf(totalStock), "#2ecc71");
        
        // Stat card 3 - Total Nilai Inventori
        long totalValue = products.stream().mapToLong(p -> (long) p.getPrice() * p.getStock()).sum();
        VBox card3 = createStatCard("💰 Nilai Inventori", "Rp " + totalValue, "#e74c3c");
        
        HBox cardsRow = new HBox(20);
        cardsRow.getChildren().addAll(card1, card2, card3);
        
        statsBox.getChildren().add(cardsRow);
        return statsBox;
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: " + color + "; -fx-padding: 20; -fx-border-radius: 8; -fx-background-radius: 8;");
        card.setPrefHeight(120);
        card.setPrefWidth(200);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #ffffff; -fx-font-weight: bold;");
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 20; -fx-text-fill: #ffffff; -fx-font-weight: bold;");
        
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private void showProductManagement() {
        contentArea.getChildren().clear();
        
        Label titleLabel = new Label("Manajemen Produk");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // Action bar
        VBox actionBar = createProductActionBar();
        
        // Product table
        TableView<Product> table = createProductTable();
        
        // Store reference for search filtering
        this.currentProductTable = table;
        
        contentArea.getChildren().addAll(
            titleLabel,
            actionBar,
            table
        );
        VBox.setVgrow(table, Priority.ALWAYS);
    }

    private VBox createProductActionBar() {
        VBox actionBar = new VBox(10);
        actionBar.setStyle("-fx-padding: 15;");
        
        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari produk berdasarkan nama atau kode...");
        searchField.setStyle("-fx-padding: 10; -fx-font-size: 12;");
        searchField.setPrefHeight(40);
        
        // Add button
        Button addBtn = new Button("➕ Tambah Produk Baru");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        addBtn.setOnAction(e -> showAddProductDialog());
        
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });
        
        actionBar.getChildren().addAll(searchField, addBtn);
        return actionBar;
    }
    
    private void filterProducts(String query) {
        if (query == null || query.isEmpty()) {
            // Tampilkan semua produk
            if (currentProductTable != null) {
                currentProductTable.setItems(products);
            }
        } else {
            // Filter berdasarkan nama atau kode
            String lowerQuery = query.toLowerCase();
            ObservableList<Product> filteredList = FXCollections.observableArrayList();
            
            for (Product product : products) {
                if (product.getName().toLowerCase().contains(lowerQuery) || 
                    product.getId().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(product);
                }
            }
            
            if (currentProductTable != null) {
                currentProductTable.setItems(filteredList);
            }
        }
    }

    private TableView<Product> createProductTable() {
        TableView<Product> table = new TableView<>(products);
        table.setStyle("-fx-font-size: 11;");
        
        // No column
        TableColumn<Product, Integer> noCol = new TableColumn<>("No");
        noCol.setPrefWidth(40);
        noCol.setCellValueFactory(param -> {
            int index = table.getItems().indexOf(param.getValue()) + 1;
            return new javafx.beans.property.SimpleObjectProperty<>(index);
        });
        
        // Code column
        TableColumn<Product, String> codeCol = new TableColumn<>("Kode Barang");
        codeCol.setPrefWidth(100);
        codeCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        // Name column
        TableColumn<Product, String> nameCol = new TableColumn<>("Nama Produk");
        nameCol.setPrefWidth(180);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setStyle("-fx-alignment: CENTER-LEFT;");
        
        // Price column
        TableColumn<Product, Double> priceCol = new TableColumn<>("Harga");
        priceCol.setPrefWidth(130);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(col -> new javafx.scene.control.TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("Rp %,.0f", item));
                }
            }
        });
        priceCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-text-alignment: left;");
        
        // Stock column
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stok");
        stockCol.setPrefWidth(100);
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setStyle("-fx-alignment: CENTER;");
        
        // Action column
        TableColumn<Product, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setPrefWidth(180);
        actionCol.setCellFactory(param -> new javafx.scene.control.TableCell<Product, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Hapus");
            
            {
                editBtn.setStyle("-fx-font-size: 11; -fx-padding: 8 15 8 15; -fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 3; -fx-background-radius: 3;");
                deleteBtn.setStyle("-fx-font-size: 11; -fx-padding: 8 15 8 15; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 3; -fx-background-radius: 3;");
                editBtn.setPrefWidth(65);
                deleteBtn.setPrefWidth(65);
                
                editBtn.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    showEditProductDialog(product);
                });
                
                deleteBtn.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    if (showConfirmDialog("Hapus Produk", "Apakah Anda yakin ingin menghapus produk ini?")) {
                        productService.deleteProduct(product.getId());
                        products.remove(product);
                        showInfo("Produk berhasil dihapus");
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(8);
                    hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    hbox.getChildren().addAll(editBtn, deleteBtn);
                    setGraphic(hbox);
                }
            }
        });
        
        table.getColumns().addAll(noCol, codeCol, nameCol, priceCol, stockCol, actionCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-font-size: 12; -fx-row-height: 40;");
        
        return table;
    }

    private void showAddProductDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tambah Produk");
        alert.setHeaderText("Form Tambah Produk Baru");
        
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));
        
        TextField codeField = new TextField();
        codeField.setPromptText("Kode Barang (misal: P006)");
        codeField.setPrefHeight(35);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Nama Produk");
        nameField.setPrefHeight(35);
        
        TextField priceField = new TextField();
        priceField.setPromptText("Harga (contoh: 12000)");
        priceField.setPrefHeight(35);
        
        TextField stockField = new TextField();
        stockField.setPromptText("Stok");
        stockField.setPrefHeight(35);
        
        Button saveBtn = new Button("Simpan");
        saveBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        saveBtn.setPrefWidth(100);
        
        saveBtn.setOnAction(e -> {
            try {
                String code = codeField.getText();
                String name = nameField.getText();
                // Hapus pemisah ribuan (titik) sebelum parsing
                String priceStr = priceField.getText().replaceAll("\\.", "");
                double price = Double.parseDouble(priceStr);
                int stock = Integer.parseInt(stockField.getText());
                
                if (code.isEmpty() || name.isEmpty()) {
                    showError("Kode dan nama produk tidak boleh kosong!");
                    return;
                }
                
                Product newProduct = new Product(code, name, price, stock);
                productService.addProduct(newProduct);
                products.add(newProduct);
                alert.close();
                showInfo("Produk berhasil ditambahkan!");
                
            } catch (NumberFormatException ex) {
                showError("Harga dan stok harus berupa angka!");
            }
        });
        
        formBox.getChildren().addAll(
            new Label("Kode Barang:"), codeField,
            new Label("Nama Produk:"), nameField,
            new Label("Harga:"), priceField,
            new Label("Stok:"), stockField,
            saveBtn
        );
        
        alert.getDialogPane().setContent(formBox);
        alert.showAndWait();
    }

    private void showEditProductDialog(Product product) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Produk");
        alert.setHeaderText("Edit Data Produk");
        
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));
        
        TextField nameField = new TextField(product.getName());
        nameField.setPromptText("Nama Produk");
        nameField.setPrefHeight(35);
        
        TextField priceField = new TextField(String.format("%.0f", product.getPrice()));
        priceField.setPromptText("Harga (contoh: 12000)");
        priceField.setPrefHeight(35);
        
        TextField stockField = new TextField(String.valueOf(product.getStock()));
        stockField.setPromptText("Stok");
        stockField.setPrefHeight(35);
        
        Button saveBtn = new Button("Simpan");
        saveBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        saveBtn.setPrefWidth(100);
        
        saveBtn.setOnAction(e -> {
            try {
                if (nameField.getText().isEmpty()) {
                    showError("Nama produk tidak boleh kosong!");
                    return;
                }
                
                product.setName(nameField.getText());
                // Hapus pemisah ribuan (titik) sebelum parsing
                String priceStr = priceField.getText().replaceAll("\\.", "");
                product.setPrice(Double.parseDouble(priceStr));
                product.setStock(Integer.parseInt(stockField.getText()));
                
                // Panggil service untuk update ke database/DAO
                productService.updateProduct(product);
                
                // Refresh table view
                if (currentProductTable != null) {
                    currentProductTable.refresh();
                }
                
                alert.close();
                showInfo("Produk berhasil di perbarui");
                
            } catch (NumberFormatException ex) {
                showError("Harga dan stok harus berupa angka!");
            }
        });
        
        formBox.getChildren().addAll(
            new Label("Nama Produk:"), nameField,
            new Label("Harga:"), priceField,
            new Label("Stok:"), stockField,
            saveBtn
        );
        
        alert.getDialogPane().setContent(formBox);
        alert.showAndWait();
    }

    private void showSalesReport() {
        contentArea.getChildren().clear();
        
        Label titleLabel = new Label("Laporan Penjualan");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        VBox filterBox = new VBox(10);
        filterBox.setStyle("-fx-padding: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #f9f9f9;");
        
        Label fromLabel = new Label("Dari Tanggal:");
        fromLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        DatePicker fromDate = new DatePicker();
        fromDate.setValue(LocalDate.now().minusDays(30));
        
        Label toLabel = new Label("Sampai Tanggal:");
        toLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        DatePicker toDate = new DatePicker();
        toDate.setValue(LocalDate.now());
        
        // Report table
        TableView<Transaction> reportTable = new TableView<>();
        reportTable.setStyle("-fx-font-size: 11;");
        
        // No column
        TableColumn<Transaction, Integer> noCol = new TableColumn<>("No");
        noCol.setPrefWidth(40);
        noCol.setCellValueFactory(param -> {
            int index = reportTable.getItems().indexOf(param.getValue()) + 1;
            return new javafx.beans.property.SimpleObjectProperty<>(index);
        });
        
        // ID Transaksi column
        TableColumn<Transaction, String> idCol = new TableColumn<>("ID Transaksi");
        idCol.setPrefWidth(110);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        // Produk column
        TableColumn<Transaction, String> productsCol = new TableColumn<>("Produk");
        productsCol.setPrefWidth(180);
        productsCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(
            formatProductsList(param.getValue().getItems())
        ));
        
        // Tanggal column
        TableColumn<Transaction, LocalDate> dateCol = new TableColumn<>("Tanggal");
        dateCol.setPrefWidth(100);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        // Waktu column
        TableColumn<Transaction, String> timeCol = new TableColumn<>("Waktu");
        timeCol.setPrefWidth(80);
        timeCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(
            param.getValue().getTime().toString()
        ));
        
        // Kasir column
        TableColumn<Transaction, String> cashierCol = new TableColumn<>("Kasir");
        cashierCol.setPrefWidth(90);
        cashierCol.setCellValueFactory(new PropertyValueFactory<>("cashier"));
        
        // Subtotal column
        TableColumn<Transaction, String> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setPrefWidth(100);
        subtotalCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(
            "Rp " + (long)param.getValue().getSubtotal()
        ));
        subtotalCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        // Diskon column
        TableColumn<Transaction, String> discountCol = new TableColumn<>("Diskon");
        discountCol.setPrefWidth(100);
        discountCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(
            "Rp " + (long)param.getValue().getDiscount()
        ));
        discountCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        // Total column
        TableColumn<Transaction, String> totalCol = new TableColumn<>("Total");
        totalCol.setPrefWidth(110);
        totalCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(
            "Rp " + (long)param.getValue().getTotal()
        ));
        totalCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        // Metode Bayar column
        TableColumn<Transaction, String> methodCol = new TableColumn<>("Metode Bayar");
        methodCol.setPrefWidth(110);
        methodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        methodCol.setStyle("-fx-alignment: CENTER;");
        
        reportTable.getColumns().addAll(noCol, idCol, productsCol, dateCol, timeCol, cashierCol, subtotalCol, discountCol, totalCol, methodCol);
        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        Button filterBtn = new Button("Tampilkan Laporan");
        filterBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        filterBtn.setOnAction(e -> {
            LocalDate from = fromDate.getValue();
            LocalDate to = toDate.getValue();
            
            // Filter transaksi berdasarkan tanggal
            ObservableList<Transaction> filteredData = FXCollections.observableArrayList();
            for (Transaction trx : transactions) {
                if (!trx.getDate().isBefore(from) && !trx.getDate().isAfter(to)) {
                    filteredData.add(trx);
                }
            }
            
            reportTable.setItems(filteredData);
            
            // Hitung total penjualan
            double totalSales = filteredData.stream().mapToDouble(Transaction::getTotal).sum();
            long totalTransactions = filteredData.size();
            
            showInfo("Laporan Periode: " + from + " s/d " + to + 
                    "\nTotal Transaksi: " + totalTransactions + 
                    "\nTotal Penjualan: Rp " + (long)totalSales);
        });
        
        HBox filterHBox = new HBox(20);
        filterHBox.getChildren().addAll(
            new VBox(5, fromLabel, fromDate),
            new VBox(5, toLabel, toDate),
            filterBtn
        );
        
        filterBox.getChildren().add(filterHBox);
        
        // Set initial data (semua transaksi)
        reportTable.setItems(transactions);
        
        contentArea.getChildren().addAll(
            titleLabel,
            filterBox,
            reportTable
        );
        VBox.setVgrow(reportTable, Priority.ALWAYS);
    }

    private void showPromoManagement() {
        contentArea.getChildren().clear();
        
        Label titleLabel = new Label("Manajemen Promo");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // Action bar
        VBox actionBar = createPromoActionBar();
        
        // Promo table
        TableView<Promo> table = createPromoTable();
        
        // Store reference for search filtering
        this.currentPromoTable = table;
        
        contentArea.getChildren().addAll(
            titleLabel,
            actionBar,
            table
        );
        VBox.setVgrow(table, Priority.ALWAYS);
    }

    private VBox createPromoActionBar() {
        VBox actionBar = new VBox(10);
        actionBar.setStyle("-fx-padding: 15;");
        
        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari promo berdasarkan kode atau nama...");
        searchField.setStyle("-fx-padding: 10; -fx-font-size: 12;");
        searchField.setPrefHeight(40);
        
        // Add button
        Button addBtn = new Button("➕ Tambah Promo Baru");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        addBtn.setOnAction(e -> showAddPromoDialog());
        
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPromos(newValue);
        });
        
        actionBar.getChildren().addAll(searchField, addBtn);
        return actionBar;
    }
    
    private void filterPromos(String query) {
        if (query == null || query.isEmpty()) {
            // Tampilkan semua promo
            if (currentPromoTable != null) {
                currentPromoTable.setItems(promos);
            }
        } else {
            // Filter berdasarkan kode atau nama
            String lowerQuery = query.toLowerCase();
            ObservableList<Promo> filteredList = FXCollections.observableArrayList();
            
            for (Promo promo : promos) {
                if (promo.getCode().toLowerCase().contains(lowerQuery) || 
                    promo.getName().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(promo);
                }
            }
            
            if (currentPromoTable != null) {
                currentPromoTable.setItems(filteredList);
            }
        }
    }

    private TableView<Promo> createPromoTable() {
        TableView<Promo> table = new TableView<>(promos);
        table.setStyle("-fx-font-size: 11;");
        
        // No column
        TableColumn<Promo, Integer> noCol = new TableColumn<>("No");
        noCol.setPrefWidth(40);
        noCol.setCellValueFactory(param -> {
            int index = table.getItems().indexOf(param.getValue()) + 1;
            return new javafx.beans.property.SimpleObjectProperty<>(index);
        });
        
        // Code column
        TableColumn<Promo, String> codeCol = new TableColumn<>("Kode Promo");
        codeCol.setPrefWidth(100);
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        
        // Name column
        TableColumn<Promo, String> nameCol = new TableColumn<>("Nama Promo");
        nameCol.setPrefWidth(150);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        // Discount column
        TableColumn<Promo, String> discountCol = new TableColumn<>("Diskon");
        discountCol.setPrefWidth(80);
        discountCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getDiscountPercentage()));
        discountCol.setStyle("-fx-alignment: CENTER;");
        
        // Start Date column
        TableColumn<Promo, LocalDate> startCol = new TableColumn<>("Tanggal Mulai");
        startCol.setPrefWidth(110);
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        
        // End Date column
        TableColumn<Promo, LocalDate> endCol = new TableColumn<>("Tanggal Berakhir");
        endCol.setPrefWidth(110);
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        
        // Status column
        TableColumn<Promo, String> statusCol = new TableColumn<>("Status");
        statusCol.setPrefWidth(80);
        statusCol.setCellValueFactory(param -> {
            String status = param.getValue().isActive() ? "✓ Aktif" : "✗ Non-aktif";
            return new javafx.beans.property.SimpleObjectProperty<>(status);
        });
        statusCol.setStyle("-fx-alignment: CENTER;");
        
        // Action column
        TableColumn<Promo, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(param -> new javafx.scene.control.TableCell<Promo, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Hapus");
            
            {
                editBtn.setStyle("-fx-font-size: 10; -fx-padding: 5; -fx-background-color: #3498db; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-font-size: 10; -fx-padding: 5; -fx-background-color: #e74c3c; -fx-text-fill: white;");
                
                editBtn.setOnAction(e -> {
                    Promo promo = getTableView().getItems().get(getIndex());
                    showEditPromoDialog(promo);
                });
                
                deleteBtn.setOnAction(e -> {
                    Promo promo = getTableView().getItems().get(getIndex());
                    if (showConfirmDialog("Hapus Promo", "Apakah Anda yakin ingin menghapus promo ini?")) {
                        promos.remove(promo);
                        showInfo("Promo berhasil dihapus");
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5);
                    hbox.getChildren().addAll(editBtn, deleteBtn);
                    setGraphic(hbox);
                }
            }
        });
        
        table.getColumns().addAll(noCol, codeCol, nameCol, discountCol, startCol, endCol, statusCol, actionCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        return table;
    }

    private void showAddPromoDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tambah Promo");
        alert.setHeaderText("Form Tambah Promo Baru");
        
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));
        
        TextField codeField = new TextField();
        codeField.setPromptText("Kode Promo (misal: PROMO50)");
        codeField.setPrefHeight(35);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Nama Promo");
        nameField.setPrefHeight(35);
        
        TextField discountField = new TextField();
        discountField.setPromptText("Diskon dalam persen (misal: 10 untuk 10%)");
        discountField.setPrefHeight(35);
        
        DatePicker startDateField = new DatePicker();
        startDateField.setValue(LocalDate.now());
        
        DatePicker endDateField = new DatePicker();
        endDateField.setValue(LocalDate.now().plusDays(30));
        
        Button saveBtn = new Button("Simpan");
        saveBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        saveBtn.setPrefWidth(100);
        
        saveBtn.setOnAction(e -> {
            try {
                String code = codeField.getText();
                String name = nameField.getText();
                double discount = Double.parseDouble(discountField.getText()) / 100.0;
                LocalDate startDate = startDateField.getValue();
                LocalDate endDate = endDateField.getValue();
                
                if (code.isEmpty() || name.isEmpty()) {
                    showError("Kode dan nama promo tidak boleh kosong!");
                    return;
                }
                
                if (discount < 0 || discount > 1) {
                    showError("Diskon harus antara 0-100%!");
                    return;
                }
                
                Promo newPromo = new Promo(code, name, discount, startDate, endDate);
                promos.add(newPromo);
                alert.close();
                showInfo("Promo berhasil ditambahkan!");
                
            } catch (NumberFormatException ex) {
                showError("Diskon harus berupa angka!");
            }
        });
        
        formBox.getChildren().addAll(
            new Label("Kode Promo:"), codeField,
            new Label("Nama Promo:"), nameField,
            new Label("Diskon (%):"), discountField,
            new Label("Tanggal Mulai:"), startDateField,
            new Label("Tanggal Berakhir:"), endDateField,
            saveBtn
        );
        
        alert.getDialogPane().setContent(formBox);
        alert.showAndWait();
    }

    private void showEditPromoDialog(Promo promo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Promo");
        alert.setHeaderText("Edit Data Promo");
        
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));
        
        TextField codeField = new TextField(promo.getCode());
        codeField.setPromptText("Kode Promo");
        codeField.setPrefHeight(35);
        
        TextField nameField = new TextField(promo.getName());
        nameField.setPromptText("Nama Promo");
        nameField.setPrefHeight(35);
        
        TextField discountField = new TextField(String.valueOf((int)(promo.getDiscount() * 100)));
        discountField.setPromptText("Diskon dalam persen");
        discountField.setPrefHeight(35);
        
        DatePicker startDateField = new DatePicker(promo.getStartDate());
        DatePicker endDateField = new DatePicker(promo.getEndDate());
        
        Button saveBtn = new Button("Simpan");
        saveBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        saveBtn.setPrefWidth(100);
        
        saveBtn.setOnAction(e -> {
            try {
                if (codeField.getText().isEmpty()) {
                    showError("Kode promo tidak boleh kosong!");
                    return;
                }
                
                if (nameField.getText().isEmpty()) {
                    showError("Nama promo tidak boleh kosong!");
                    return;
                }
                
                double discount = Double.parseDouble(discountField.getText()) / 100.0;
                
                if (discount < 0 || discount > 1) {
                    showError("Diskon harus antara 0-100%!");
                    return;
                }
                
                promo.setCode(codeField.getText());
                promo.setName(nameField.getText());
                promo.setDiscount(discount);
                promo.setStartDate(startDateField.getValue());
                promo.setEndDate(endDateField.getValue());
                
                // Refresh table view
                if (currentPromoTable != null) {
                    currentPromoTable.refresh();
                }
                
                alert.close();
                showInfo("Promo berhasil diperbarui!");
                
            } catch (NumberFormatException ex) {
                showError("Diskon harus berupa angka!");
            }
        });
        
        formBox.getChildren().addAll(
            new Label("Kode Promo:"), codeField,
            new Label("Nama Promo:"), nameField,
            new Label("Diskon (%):"), discountField,
            new Label("Tanggal Mulai:"), startDateField,
            new Label("Tanggal Berakhir:"), endDateField,
            saveBtn
        );
        
        alert.getDialogPane().setContent(formBox);
        alert.showAndWait();
    }

    // Helper methods
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
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

    private boolean showConfirmDialog(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        return alert.showAndWait().get() == javafx.scene.control.ButtonType.OK;
    }
    
    private String formatProductsList(java.util.List<com.upb.agripos.model.ItemTransaksi> items) {
        if (items == null || items.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            com.upb.agripos.model.ItemTransaksi item = items.get(i);
            sb.append("[").append(item.getProduct().getId()).append("] ")
              .append(item.getProduct().getName()).append(" (x").append(item.getQty()).append(")");
            if (i < items.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private void showSuccessNotification(String message) {
        // Create notification pane
        javafx.scene.layout.VBox notificationBox = new javafx.scene.layout.VBox();
        notificationBox.setStyle("-fx-background-color: #2ecc71; -fx-padding: 15; -fx-border-radius: 5; -fx-background-radius: 5;");
        notificationBox.setPrefWidth(350);
        notificationBox.setMaxHeight(60);
        
        Label notificationLabel = new Label(message);
        notificationLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #ffffff; -fx-font-weight: bold;");
        notificationBox.getChildren().add(notificationLabel);
        
        // Create popup
        javafx.stage.Popup popup = new javafx.stage.Popup();
        popup.getContent().add(notificationBox);
        
        // Position at top right
        popup.setX(stage.getX() + stage.getWidth() - 370);
        popup.setY(stage.getY() + 20);
        
        // Show popup
        popup.show(stage);
        
        // Auto hide after 3 seconds
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(3),
                e -> popup.hide()
            )
        );
        timeline.play();
    }
}
