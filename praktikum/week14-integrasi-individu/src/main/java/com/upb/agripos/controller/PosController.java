package com.upb.agripos.controller;

import java.util.List;

import com.upb.agripos.dao.JdbcProductDAO;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.service.CartService;
import com.upb.agripos.service.ProductService;

public class PosController {

    private final ProductService productService =
            new ProductService(new JdbcProductDAO());
    private final CartService cartService = new CartService();

    // PRODUCT
    public void addProduct(Product p) {
        productService.add(p);
    }

    public void deleteProduct(String code) {
        productService.delete(code);
    }

    public List<Product> getProducts() {
        return productService.getAll();
    }

    // CART
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
        return cartService.getItems();
    }

    public double getCartTotal() {
        return cartService.getTotal();
    }
}
