package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.model.coupon_type.BxGy;
import com.example.demo.model.coupon_type.CartWise;
import com.example.demo.model.coupon_type.CouponType;
import com.example.demo.model.coupon_type.ProductWise;
import com.example.demo.repository.CouponRepository;
import com.example.demo.repository.RedisCouponRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class CouponManagerServiceTest {

    private static CouponManagerService couponManagerService;
    private static CouponRepository couponRepository;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup(){
        objectMapper = mock(ObjectMapper.class);
        couponRepository = mock(RedisCouponRepository.class);
        couponManagerService = new CouponManagerService(couponRepository, objectMapper);
    }

    @BeforeEach
    public void clear(){
        couponManagerService.clearCoupons();
    }

    @Test
    @DisplayName("create coupon should successfully create a new coupon and add to the available coupons")
    public void testCreateCoupon(){
        CouponType couponType = new CartWise(200, 10);
        Coupon coupon = new Coupon("123", couponType);
        couponManagerService.createCoupon(coupon);
        assertEquals(1, couponManagerService.getAllCoupons().size());
    }

    @Test
    @DisplayName("getCouponById should return the coupon if it is available else should return empty optional")
    public void testGetCouponById(){

        CouponType couponType = new CartWise(200, 10);
        Coupon coupon = new Coupon("123", couponType);
        couponManagerService.createCoupon(coupon);

        assertTrue(couponManagerService.getCouponById("123").isPresent());
        assertFalse(couponManagerService.getCouponById("111").isPresent());
    }

    @Test
    @DisplayName("updateCouponById should return true if coupon can be updated, else it should return false")
    public void testUpdateCouponById(){

        CouponType couponType = new CartWise(200, 10);
        Coupon coupon = new Coupon("123", couponType);
        couponManagerService.createCoupon(coupon);

        CouponType updatedCouponType = new CartWise(400, 20);
        Coupon updatedCoupon = new Coupon("456", updatedCouponType);

        assertTrue(couponManagerService.updateCouponById("123", updatedCoupon));
        assertFalse(couponManagerService.updateCouponById("111", updatedCoupon));
    }

    @Test
    @DisplayName("deleteCouponById should return true if coupon can be deleted, else it should return false")
    public void testDeleteCouponById(){

        CouponType couponType = new CartWise(200, 10);
        Coupon coupon = new Coupon("123", couponType);
        Coupon coupon2 = new Coupon("456", couponType);

        couponManagerService.createCoupon(coupon);
        couponManagerService.createCoupon(coupon2);

        assertTrue(couponManagerService.deleteCouponById("123"));
        assertEquals(1, couponManagerService.getAllCoupons().size());

        assertFalse(couponManagerService.deleteCouponById("111"));
    }

    @Test
    @DisplayName("getAllApplicableCouponsResponse should return the response with discounts if a coupon is applicable on a cart")
    public void testGetAllApplicableCouponsResponse(){

        Product p1 = new Product("1", "pepsi", 6, 50);
        Product p2 = new Product("2", "coke", 3, 10);
        Product p3 = new Product("3", "limca", 2, 25);

        Cart cart = new Cart(List.of(p1, p2, p3));

        CouponType cartWise = new CartWise(200, 10);
        ProductWise productWise = new ProductWise();
        productWise.addProductDiscount(new Product("4", "chips", 1, 24), 10);

        CouponType bxGy = new BxGy(List.of(p1, p2), List.of(p3), 2, 1, 3);

        couponManagerService.createCoupon(new Coupon("1", cartWise));
        couponManagerService.createCoupon(new Coupon("2", productWise));
        couponManagerService.createCoupon(new Coupon("3", bxGy));

        List<CouponOnCartResponse> response = couponManagerService.getAllApplicableCouponsResponse(cart);

        CouponOnCartResponse cartWiseCouponAfterDiscountResponse = response.stream().filter(couponOnCartResponse -> couponOnCartResponse.couponId().equals("1")).findFirst().get();
        CouponOnCartResponse bxGyCouponAfterDiscountResponse = response.stream().filter(couponOnCartResponse -> couponOnCartResponse.couponId().equals("3")).findFirst().get();
        Optional<CouponOnCartResponse> productWiseCouponAfterDiscountResponse = response.stream().filter(couponOnCartResponse -> couponOnCartResponse.couponId().equals("2")).findFirst();

        assertEquals(38.0, cartWiseCouponAfterDiscountResponse.discountAmount());
        assertEquals(50.0, bxGyCouponAfterDiscountResponse.discountAmount());
        assertTrue(productWiseCouponAfterDiscountResponse.isEmpty());
    }

    @Test
    @DisplayName("applyCouponWithId should return empty if coupon is not present")
    public void testApplyCouponWithIdReturnsEmpty(){

        Product p1 = new Product("1", "pepsi", 6, 50);
        Cart cart = new Cart(List.of(p1));
        assertTrue(couponManagerService.applyCouponWithId("2", cart).isEmpty());
    }

    @Test
    @DisplayName("applyCouponWithId should return the updatedCart if the coupon to be applied is present")
    public void testApplyCouponWithIdReturnsResult(){

        Product p1 = new Product("1", "pepsi", 6, 50);
        Product p2 = new Product("2", "coke", 3, 30);
        Product p3 = new Product("3", "limca", 2, 25);

        Cart cart = new Cart(List.of(p1, p2, p3));
        CouponType bxGy = new BxGy(List.of(p1, p2), List.of(p3), 2, 1, 3);

        couponManagerService.createCoupon(new Coupon("coup1", bxGy));

        UpdatedCartResponse updatedCartResponse = couponManagerService.applyCouponWithId("coup1", cart).get();

        Product buyProduct1 = updatedCartResponse.items().stream().filter(product -> product.getId().contentEquals("1")).findFirst().get();
        Product buyProduct2 = updatedCartResponse.items().stream().filter(product -> product.getId().contentEquals("2")).findFirst().get();
        Product getProduct1 = updatedCartResponse.items().stream().filter(product -> product.getId().contentEquals("3")).findFirst().get();

        assertEquals(0.0, buyProduct1.getDiscount());
        assertEquals(0.0, buyProduct2.getDiscount());
        assertEquals(50.0, getProduct1.getDiscount());
        assertEquals(50.0, updatedCartResponse.totalDiscount());
    }
}
