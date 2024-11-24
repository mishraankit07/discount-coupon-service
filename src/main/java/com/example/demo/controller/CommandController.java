package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.Coupon;
import com.example.demo.model.CouponOnCartResponse;
import com.example.demo.model.UpdatedCartResponse;
import com.example.demo.service.CouponManagerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CommandController {

    private final CouponManagerService couponManagerService;
    private final ObjectMapper objectMapper;

    public CommandController(CouponManagerService couponManagerService, ObjectMapper objectMapper){
        this.couponManagerService = couponManagerService;
        this.objectMapper = objectMapper;
    }

    @PutMapping("/coupons/{id}")
    public ResponseEntity<String> updateCouponById(@PathVariable String id, @RequestBody Coupon newCoupon){
        if(couponManagerService.updateCouponById(id, newCoupon)){
            return ResponseEntity.ok().body("successfully updated the coupon with id:" + id + " to:" + newCoupon);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("coupon with id:" + id + " not found");
    }

    @PostMapping("/coupons")
    public ResponseEntity<String> createCoupon(@RequestBody Coupon coupon){
        couponManagerService.createCoupon(coupon);
        return ResponseEntity.ok().body("successfully created the coupon:" + coupon);
    }

    @PostMapping("/applicableCoupons")
    public ResponseEntity<String> fetchAllApplicableCouponsForCart(@RequestBody Cart cart) throws JsonProcessingException {
        List<CouponOnCartResponse> couponOnCartResponse = couponManagerService.getAllApplicableCouponsResponse(cart);
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(couponOnCartResponse));
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<String> applyCouponToCart(@PathVariable String id, @RequestBody Cart cart) throws JsonProcessingException {
        Optional<UpdatedCartResponse> couponAppliedToCartResult = couponManagerService.applyCouponWithId(id, cart);

        if(couponAppliedToCartResult.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon with id:" + id + " doesn't exist");
        }

        return ResponseEntity.ok().body(objectMapper.writeValueAsString(couponAppliedToCartResult.get()));
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<String> deleteCouponById(@PathVariable String id){
        if(couponManagerService.deleteCouponById(id)){
            return ResponseEntity.ok().body("successfully deleted the coupon with id:" + id);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("coupon with id:" + id + " not found");
    }
}
