package com.upb.agripos.view;

import com.upb.agripos.controller.LoginController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginView {

    private final LoginController controller;
    private String selectedRole = "KASIR";

    public LoginView(Stage stage) {
        this.controller = new LoginController(stage);
    }

    public Parent getView() {
        // Main container
        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #ffffff;");
        
        // ========== HEADER ==========
        VBox header = new VBox(0);
        header.setStyle("-fx-background-color: linear-gradient(to bottom, #00df16c1, #55a32b); -fx-padding: 35px 25px; -fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0;");
        
        // Logo keranjang dengan styling profesional
        Text cartIcon = new Text("🛒");
        cartIcon.setFont(Font.font("Arial", 60));
        cartIcon.setStyle("-fx-fill: #ffffff;");
        
        // Text di samping kanan logo
        VBox textBox = new VBox(6);
        textBox.setStyle("-fx-padding: 8 0 0 0;");
        
        Label titleLabel = new Label("AgriPOS");
        titleLabel.setStyle("-fx-font-size: 36; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        
        Label subtitleLabel = new Label("Agricultural Point of Sale System");
        subtitleLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #e8f5e9;");
        
        textBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Layout horizontal: logo keranjang di kiri, text "AgriPOS" di kanan
        HBox headerContent = new HBox(15);
        headerContent.setStyle("-fx-alignment: center-left;");
        headerContent.getChildren().addAll(cartIcon, textBox);
        
        header.getChildren().addAll(headerContent);
        VBox.setVgrow(header, Priority.NEVER);
        
        // ========== CONTENT ==========
        VBox content = new VBox(0);
        content.setStyle("-fx-background-color: #ffffff;");
        
        // Role Selection Section
        VBox roleSection = createRoleSection();
        
        // Form Section with card style
        VBox formSection = new VBox(20);
        formSection.setStyle("-fx-background-color: #fafafa; -fx-padding: 30px;");
        formSection.setPrefWidth(400);
        
        // Title untuk form
        Label formTitleLabel = new Label("Login");
        formTitleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #333333;");
        
        // Form fields
        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-padding: 0 0 8 0;");
        
        TextField userField = new TextField();
        userField.setStyle("-fx-padding: 12; -fx-font-size: 12; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-width: 1;");
        userField.setPromptText("👤 Masukkan username");
        userField.setPrefHeight(40);
        
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-padding: 8 0 8 0;");
        
        // Password field container with visibility toggle
        HBox passwordContainer = new HBox(0);
        passwordContainer.setStyle("-fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-width: 1; -fx-background-color: #ffffff;");
        
        PasswordField passField = new PasswordField();
        passField.setStyle("-fx-padding: 12; -fx-font-size: 12; -fx-border-color: transparent; -fx-background-color: transparent;");
        passField.setPromptText("🔒 Masukkan password");
        passField.setPrefHeight(40);
        HBox.setHgrow(passField, Priority.ALWAYS);
        
        TextField passVisibleField = new TextField();
        passVisibleField.setStyle("-fx-padding: 12; -fx-font-size: 12; -fx-border-color: transparent; -fx-background-color: transparent;");
        passVisibleField.setPromptText("🔒 Masukkan password");
        passVisibleField.setPrefHeight(40);
        passVisibleField.setVisible(false);
        passVisibleField.setManaged(false);
        HBox.setHgrow(passVisibleField, Priority.ALWAYS);

        // Toggle button untuk show/hide password
        Button togglePassBtn = new Button("👁");
        togglePassBtn.setStyle("-fx-font-size: 16; -fx-padding: 0; -fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand; -fx-text-fill: #666666;");
        togglePassBtn.setPrefHeight(40);
        togglePassBtn.setPrefWidth(40);
        togglePassBtn.setPadding(new Insets(0, 12, 0, 0));
        
        boolean[] isPasswordVisible = {false};
        
        togglePassBtn.setOnAction(e -> {
            isPasswordVisible[0] = !isPasswordVisible[0];
            
            if (isPasswordVisible[0]) {
                // Show password - tampilkan password
                passVisibleField.setText(passField.getText());
                passField.setVisible(false);
                passField.setManaged(false);
                passVisibleField.setVisible(true);
                passVisibleField.setManaged(true);
                togglePassBtn.setText("🙈"); // Icon mata tertutup
                togglePassBtn.setStyle("-fx-font-size: 16; -fx-padding: 0; -fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand; -fx-text-fill: #ff9500;");
            } else {
                // Hide password - sembunyikan password
                passField.setText(passVisibleField.getText());
                passVisibleField.setVisible(false);
                passVisibleField.setManaged(false);
                passField.setVisible(true);
                passField.setManaged(true);
                togglePassBtn.setText("👁"); // Icon mata terbuka
                togglePassBtn.setStyle("-fx-font-size: 16; -fx-padding: 0; -fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand; -fx-text-fill: #666666;");
            }
        });
        
        // Sync text between both fields
        passField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (isPasswordVisible[0]) {
                passVisibleField.setText(newVal);
            }
        });
        
        passVisibleField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (isPasswordVisible[0]) {
                passField.setText(newVal);
            }
        });
        
        passwordContainer.getChildren().addAll(passField, passVisibleField, togglePassBtn);
        
        Label errorMsg = new Label();
        errorMsg.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 11;");
        
        // Login Button
        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-font-size: 14; -fx-padding: 12; -fx-background-color: #ff9500; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        loginBtn.setPrefWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(45);
        
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-font-size: 14; -fx-padding: 12; -fx-background-color: #e68400; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-font-size: 14; -fx-padding: 12; -fx-background-color: #ff9500; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;"));
        
        loginBtn.setOnAction(e -> {
            errorMsg.setText("");
            String password = isPasswordVisible[0] ? passVisibleField.getText() : passField.getText();
            String errorMessage = controller.login(userField.getText(), password, selectedRole);
            if (errorMessage != null) {
                errorMsg.setText(errorMessage);
            }
        });
        
        // Add form fields
        formSection.getChildren().addAll(
            formTitleLabel,
            usernameLabel, userField,
            passwordLabel, passwordContainer,
            errorMsg,
            loginBtn
        );
        
        // Demo Credentials Section
        VBox demoSection = new VBox(8);
        demoSection.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 20px 30px;");
        
        Label demoLabel = new Label("📌 Demo Credentials");
        demoLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #666666;");
        
        HBox demoKasirBox = new HBox(8);
        demoKasirBox.setPadding(new Insets(0, 0, 0, 8));
        Label demoKasir = new Label("👤 Kasir: kasir / kasir123");
        demoKasir.setStyle("-fx-font-size: 11; -fx-text-fill: #555555;");
        demoKasirBox.getChildren().add(demoKasir);
        
        HBox demoAdminBox = new HBox(8);
        demoAdminBox.setPadding(new Insets(0, 0, 0, 8));
        Label demoAdmin = new Label("👤 Admin: admin / admin123");
        demoAdmin.setStyle("-fx-font-size: 11; -fx-text-fill: #555555;");
        demoAdminBox.getChildren().add(demoAdmin);
        
        demoSection.getChildren().addAll(demoLabel, demoKasirBox, demoAdminBox);
        
        // Wrapper untuk center form
        VBox formWrapper = new VBox();
        formWrapper.setPadding(new Insets(30, 0, 30, 0));
        formWrapper.setAlignment(Pos.TOP_CENTER);
        formWrapper.setStyle("-fx-background-color: #ffffff;");
        
        VBox formCard = new VBox();
        formCard.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #fafafa; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        formCard.setMaxWidth(450);
        formCard.getChildren().addAll(formSection, demoSection);
        
        formWrapper.getChildren().add(formCard);
        
        // Main content vertical arrangement
        content.getChildren().addAll(roleSection, formWrapper);
        VBox.setVgrow(formWrapper, Priority.ALWAYS);
        
        // Add header and content to main
        mainContainer.getChildren().addAll(header, content);
        
        return mainContainer;
    }
    
    private VBox createRoleSection() {
        VBox roleSection = new VBox(15);
        roleSection.setStyle("-fx-background-color: #ffffff; -fx-padding: 25px;");
        roleSection.setAlignment(Pos.TOP_CENTER);
        
        Label loginAsLabel = new Label("Login Sebagai");
        loginAsLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #333333;");
        
        HBox roleBox = new HBox(20);
        roleBox.setAlignment(Pos.CENTER);
        
        // Kasir Button
        Button kasirBtn = new Button("👤 Kasir\nTransaksi");
        kasirBtn.setStyle("-fx-font-size: 13; -fx-padding: 18; -fx-background-color: #ff9500; -fx-text-fill: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; -fx-border-color: #ff9500; -fx-border-width: 2; -fx-cursor: hand;");
        kasirBtn.setPrefWidth(160);
        kasirBtn.setPrefHeight(90);
        kasirBtn.setWrapText(true);
        
        // Admin Button
        Button adminBtn = new Button("⚙️ Admin\nKelola Sistem");
        adminBtn.setStyle("-fx-font-size: 13; -fx-padding: 18; -fx-background-color: #f0f0f0; -fx-text-fill: #666666; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; -fx-border-color: #cccccc; -fx-border-width: 2; -fx-cursor: hand;");
        adminBtn.setPrefWidth(160);
        adminBtn.setPrefHeight(90);
        adminBtn.setWrapText(true);
        
        kasirBtn.setOnAction(e -> {
            selectedRole = "KASIR";
            kasirBtn.setStyle("-fx-font-size: 13; -fx-padding: 18; -fx-background-color: #ff9500; -fx-text-fill: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; -fx-border-color: #ff9500; -fx-border-width: 2; -fx-cursor: hand;");
            adminBtn.setStyle("-fx-font-size: 13; -fx-padding: 18; -fx-background-color: #f0f0f0; -fx-text-fill: #666666; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; -fx-border-color: #cccccc; -fx-border-width: 2; -fx-cursor: hand;");
        });
        
        adminBtn.setOnAction(e -> {
            selectedRole = "ADMIN";
            adminBtn.setStyle("-fx-font-size: 13; -fx-padding: 18; -fx-background-color: #ff9500; -fx-text-fill: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; -fx-border-color: #ff9500; -fx-border-width: 2; -fx-cursor: hand;");
            kasirBtn.setStyle("-fx-font-size: 13; -fx-padding: 18; -fx-background-color: #f0f0f0; -fx-text-fill: #666666; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; -fx-border-color: #cccccc; -fx-border-width: 2; -fx-cursor: hand;");
        });
        
        roleBox.getChildren().addAll(kasirBtn, adminBtn);
        
        Separator separator = new Separator();
        separator.setStyle("-fx-padding: 10 0 10 0;");
        
        roleSection.getChildren().addAll(loginAsLabel, roleBox, separator);
        
        return roleSection;
    }

    public void show() {
        javafx.scene.Scene scene = new javafx.scene.Scene(getView(), 500, 600);
        javafx.stage.Stage stage = controller.getStage();
        stage.setScene(scene);
        stage.show();
    }
}
