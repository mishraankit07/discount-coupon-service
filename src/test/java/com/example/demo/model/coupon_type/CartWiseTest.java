package com.example.demo.model.coupon_type;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CartWiseTest {

    private static CouponType cartWise;

    @BeforeAll
    static void setup(){
        cartWise = new CartWise(200, 10);
    }

    @Test
    @DisplayName("isApplicable should return true if cart's total is greater than or equal to amount")
    public void testIsApplicableReturnsTrue(){

        Product product1 = new Product("id1", "pepsi", 2, 50);
        Product product2 = new Product("id2", "coke", 2, 60);

        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        Cart cart = new Cart(productList);

        assertTrue(cartWise.isApplicable(cart));
    }

    @Test
    @DisplayName("isApplicable should return false if cart's total is smaller than amount")
    public void testIsApplicableReturnsFalse(){

        Product product1 = new Product("id1", "pepsi", 2, 50);
        Product product2 = new Product("id2", "coke", 2, 40);

        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        Cart cart = new Cart(productList);

        assertFalse(cartWise.isApplicable(cart));
    }

    @Test
    @DisplayName("apply should return correct discount amount")
    public void testApplyReturnsDiscountAmount(){

        Product product1 = new Product("id1", "pepsi", 2, 50);
        Product product2 = new Product("id2", "coke", 2, 60);

        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        Cart cart = new Cart(productList);
        assertEquals(cartWise.apply(cart), 22.0);
    }

    @Test
    @DisplayName("discountDetails should return the information about the discount")
    public void testDiscountDetails(){

        Product product1 = new Product("id1", "pepsi", 2, 50);
        Product product2 = new Product("id2", "coke", 2, 60);

        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        Cart cart = new Cart(productList);

        assertEquals(cartWise.discountDetails(), "Get discount of 10 for purchase greater than 200.0");
    }
}
