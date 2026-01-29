package com.upb.agripos;

import com.upb.agripos.model.Cart;
import com.upb.agripos.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartTest {

    @Test
    public void testTotalPrice() {
        Cart cart = new Cart();

        Product p1 = new Product("P01", "Beras", 10000, 10);
        Product p2 = new Product("P02", "Jagung", 5000, 5);

        cart.addItem(p1, 2); // 20000
        cart.addItem(p2, 3); // 15000

        assertEquals(35000, cart.getTotalPrice());
    }
}
