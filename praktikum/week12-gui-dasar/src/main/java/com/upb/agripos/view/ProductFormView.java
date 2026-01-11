package com.upb.agripos.view;

import com.upb.agripos.controller.ProductController;
import com.upb.agripos.model.Product;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ProductFormView extends VBox {

    private TextField txtCode = new TextField();
    private TextField txtName = new TextField();
    private TextField txtPrice = new TextField();
    private TextField txtStock = new TextField();
    private Button btnAdd = new Button("Tambah Produk");
    private ListView<String> listView = new ListView<>();

    private ProductController controller = new ProductController();

    public ProductFormView() {
        setPadding(new Insets(15));
        setSpacing(10);

        txtCode.setPromptText("Kode Produk");
        txtName.setPromptText("Nama Produk");
        txtPrice.setPromptText("Harga");
        txtStock.setPromptText("Stok");

        btnAdd.setOnAction(event -> tambahProduk());

        getChildren().addAll(
            txtCode,
            txtName,
            txtPrice,
            txtStock,
            btnAdd,
            listView
        );
    }

    private void tambahProduk() {
        try {
            Product p = controller.tambahProduk(
                txtCode.getText(),
                txtName.getText(),
                Double.parseDouble(txtPrice.getText()),
                Integer.parseInt(txtStock.getText())
            );

            listView.getItems().add(
                p.getCode() + " - " + p.getName()
            );

            clearForm();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Input tidak valid!");
            alert.show();
        }
    }

    private void clearForm() {
        txtCode.clear();
        txtName.clear();
        txtPrice.clear();
        txtStock.clear();
    }
}
