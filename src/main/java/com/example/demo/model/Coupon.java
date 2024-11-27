package com.example.demo.model;

import com.example.demo.model.coupon_type.CouponType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Coupon {

    private String id;

    @JsonProperty("type")
    private CouponType couponType;

    public Coupon(){

    }

    public Coupon(String id, CouponType couponType){
        this.id = id;
        this.couponType = couponType;
    }

    public String discountDetails(){
        return couponType.discountDetails();
    }

    public String getCouponType(){
        return couponType.getName();
    }

    public String getId(){
        return id;
    }

    public boolean isApplicable(Cart cart){
        return couponType.isApplicable(cart);
    }

    public double getDiscount(Cart cart){
        return couponType.apply(cart);
    }

    public void setCouponType(CouponType couponType){
        this.couponType = couponType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coupon coupon)) return false;
        return Objects.equals(id, coupon.id) && Objects.equals(couponType, coupon.couponType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, couponType);
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id='" + id + '\'' +
                ", couponType=" + couponType +
                '}';
    }
}
