package com.upb.agripos;

import com.upb.agripos.model.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductName() {
        Product product = new Product("P01", "Benih Jagung");
        assertEquals("Benih Jagung", product.getName());
    }

    @Test
    public void testProductCode() {
        Product product = new Product("P01", "Benih Jagung");
        assertEquals("P01", product.getCode());
    }

    @Test
    public void testDifferentProducts() {
        Product p1 = new Product("P01", "Benih Jagung");
        Product p2 = new Product("P02", "Benih Jagung");
        assertNotEquals(p1.getCode(), p2.getCode());

        Product p3 = new Product("P01", "Benih Padi");
        assertNotEquals(p1.getName(), p3.getName());
    }

    @Test
    public void testNullValuesAllowed() {
        Product p = new Product(null, null);
        assertNull(p.getCode());
        assertNull(p.getName());
    }
}