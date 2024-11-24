package com.example.demo.model.coupon_type;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductWiseTest {

    private static ProductWise productWise;

    @BeforeAll
    static void setup(){
        productWise = new ProductWise();
    }

    @Test
    @DisplayName("isApplicable should return true if products which have discount on them is present in the cart")
    public void testIsApplicable(){

        Product p1 = new Product("1", "pepsi", 20,12.0);
        Product p2 = new Product("2", "coke", 30,12.0);

        productWise.addProductDiscount(p1, 10);

        Cart cart = new Cart(List.of(p1, p2));
        assertTrue(productWise.isApplicable(cart));
    }

    @Test
    @DisplayName("apply should return the total discount amount on the cart")
    public void testApply(){

        Product p1 = new Product("1", "pepsi", 20,10.0);
        Product p2 = new Product("2", "coke", 30,12.0);

        productWise.addProductDiscount(p1, 10);
        Cart cart = new Cart(List.of(p1, p2));

        assertEquals(productWise.apply(cart), 20.0);
    }
}
