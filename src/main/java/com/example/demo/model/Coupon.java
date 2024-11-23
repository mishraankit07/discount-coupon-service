package com.example.demo.model;

import com.example.demo.coupon_type.CouponType;

import java.time.LocalDateTime;

public class Coupon {

    private String id;

    private CouponType couponType;

    private LocalDateTime expiryTime;

    public Coupon(String id, CouponType couponType, LocalDateTime expiryTime){
        this.id = id;
        this.couponType = couponType;
        this.expiryTime = expiryTime;
    }

    public String discountDetails(){
        return couponType.discountDetails();
    }

    public boolean isExpired(){
        return true;
    }
}
