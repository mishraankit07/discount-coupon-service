package com.example.demo.model.coupon_type;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BxGyTest {

    @Test
    @DisplayName("isApplicable should return true if BxGy type coupon can be applied to cart")
    public void testIsApplicableReturnsTrue(){

        List<Product> buyItems = new ArrayList<>();
        buyItems.add(new Product("id1", "X", 3, 10.0));
        buyItems.add(new Product("id2", "Y", 3, 20.0));
        buyItems.add(new Product("id3", "Z", 3, 30.0));

        List<Product> getItems = new ArrayList<>();
        getItems.add(new Product("id4", "A", 3, 10.0));
        getItems.add(new Product("id5", "B", 3, 20.0));
        getItems.add(new Product("id6", "C", 3, 30.0));

        CouponType bxGy = new BxGy(buyItems, getItems, 2, 1, 2);

        List<Product> cartProducts = new ArrayList<>();
        cartProducts.add(new Product("id1", "X", 3, 10.0));
        cartProducts.add(new Product("id3", "Z", 1, 30.0));
        cartProducts.add(new Product("id6", "C", 3, 30.0));

        Cart cart = new Cart(cartProducts);

        assertTrue(bxGy.isApplicable(cart));
    }

    @Test
    @DisplayName("isApplicable should return false if BxGy type coupon can be applied to cart")
    public void testIsApplicableReturnsFalse(){

        List<Product> buyItems = new ArrayList<>();
        buyItems.add(new Product("id1", "X", 3, 10.0));
        buyItems.add(new Product("id2", "Y", 3, 20.0));
        buyItems.add(new Product("id3", "Z", 3, 30.0));

        List<Product> getItems = new ArrayList<>();
        getItems.add(new Product("id4", "A", 3, 10.0));
        getItems.add(new Product("id5", "B", 3, 20.0));
        getItems.add(new Product("id6", "C", 3, 30.0));

        CouponType bxGy = new BxGy(buyItems, getItems, 2, 1, 1);

        List<Product> cartProducts = new ArrayList<>();
        cartProducts.add(new Product("id1", "X", 3, 10.0));
        cartProducts.add(new Product("id3", "Z", 1, 30.0));

        Cart cart = new Cart(cartProducts);

        assertFalse(bxGy.isApplicable(cart));
    }

    @Test
    @DisplayName("apply should get the discount amount for the given cart when repetition limit is 1")
    public void testApplyReturnsDiscountAmountWithRepetitionLimit1(){

        List<Product> buyItems = new ArrayList<>();
        buyItems.add(new Product("id1", "X", 3, 10.0));
        buyItems.add(new Product("id2", "Y", 3, 20.0));
        buyItems.add(new Product("id3", "Z", 3, 30.0));

        List<Product> getItems = new ArrayList<>();
        getItems.add(new Product("id4", "A", 3, 10.0));
        getItems.add(new Product("id5", "B", 3, 20.0));
        getItems.add(new Product("id6", "C", 3, 30.0));

        CouponType bxGy = new BxGy(buyItems, getItems, 2, 1, 1);

        List<Product> cartProducts = new ArrayList<>();
        cartProducts.add(new Product("id1", "X", 1, 10.0));
        cartProducts.add(new Product("id3", "Z", 1, 30.0));
        cartProducts.add(new Product("id6", "C", 3, 30.0));

        Cart cart = new Cart(cartProducts);

        assertEquals(30, bxGy.apply(cart));
    }

    @Test
    @DisplayName("apply should get the correct discount amount for the given cart when repetition limit more than 1")
    public void testApplyReturnsDiscountAmountWithRepetitionLimitGreaterThan1(){

        List<Product> buyItems = new ArrayList<>();
        buyItems.add(new Product("id1", "X", 3, 10.0));
        buyItems.add(new Product("id2", "Y", 3, 20.0));
        buyItems.add(new Product("id3", "Z", 3, 30.0));

        List<Product> getItems = new ArrayList<>();
        getItems.add(new Product("id4", "A", 3, 10.0));
        getItems.add(new Product("id5", "B", 3, 20.0));
        getItems.add(new Product("id6", "C", 3, 30.0));

        CouponType bxGy = new BxGy(buyItems, getItems, 2, 1, 3);

        List<Product> cartProducts = new ArrayList<>();
        cartProducts.add(new Product("id1", "X", 3, 10.0));
        cartProducts.add(new Product("id3", "Z", 3, 30.0));
        cartProducts.add(new Product("id4", "A", 1, 10.0));

        Cart cart = new Cart(cartProducts);
        assertTrue(bxGy.isApplicable(cart));
        assertEquals(10, bxGy.apply(cart));
    }

    @Test
    @DisplayName("discountDetails should return the correct discount discription")
    public void testGetDiscountDetails(){

        List<Product> buyItems = new ArrayList<>();
        buyItems.add(new Product("id1", "X", 3, 10.0));
        buyItems.add(new Product("id2", "Y", 3, 20.0));
        buyItems.add(new Product("id3", "Z", 3, 30.0));

        List<Product> getItems = new ArrayList<>();
        getItems.add(new Product("id4", "A", 3, 10.0));
        getItems.add(new Product("id5", "B", 3, 20.0));
        getItems.add(new Product("id6", "C", 3, 30.0));

        CouponType bxGy = new BxGy(buyItems, getItems, 2, 1, 3);

        List<Product> cartProducts = new ArrayList<>();
        cartProducts.add(new Product("id1", "X", 3, 10.0));
        cartProducts.add(new Product("id3", "Z", 3, 30.0));
        cartProducts.add(new Product("id4", "A", 1, 10.0));

        assertEquals("On purchase of 2 items from X,Y,Z you get 1 items free from C,A,B", bxGy.discountDetails());
    }
}
