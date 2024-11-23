package com.example.demo.coupon_type;

import com.example.demo.model.Cart;

public interface CouponType {

    public boolean isApplicable(Cart cart);
    public double apply(Cart cart);
    public String discountDetails();
}
