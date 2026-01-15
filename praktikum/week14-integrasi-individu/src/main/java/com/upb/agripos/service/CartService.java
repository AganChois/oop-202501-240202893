package com.upb.agripos.service;

import java.util.List;

import com.upb.agripos.model.Cart;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;

public class CartService {
    private final Cart cart = new Cart();

    public void add(Product product, int qty) {
        cart.add(product, qty);
    }

    public void remove(CartItem item) {
        cart.getItems().remove(item);
    }

    public void clear() {
        cart.clear();
    }

    public List<CartItem> getItems() {
        return cart.getItems();
    }

    public double getTotal() {
        return cart.getTotal();
    }
}
