package com.upb.agripos;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.CartService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    @Test
    void testTotal() {
        CartService cart = new CartService();
        cart.add(new Product("01","Pupuk",4000,10), 2);
        assertEquals(8000, cart.getTotal());
    }
}
