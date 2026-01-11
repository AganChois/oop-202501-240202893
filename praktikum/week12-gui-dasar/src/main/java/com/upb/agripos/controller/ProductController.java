package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;

public class ProductController {

    private ProductService productService;

    public ProductController() {
        this.productService = new ProductService();
    }

    public Product tambahProduk(String code, String name, double price, int stock) {
        Product product = new Product(code, name, price, stock);
        productService.addProduct(product);
        return product;
    }
}
