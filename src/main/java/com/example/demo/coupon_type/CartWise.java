package com.example.demo.coupon_type;

import com.example.demo.model.Cart;

public class CartWise implements CouponType {

    private final int percentageDiscount;
    private final double amount;

    public CartWise(double amount, int percentageDiscount){
        this.amount = amount;
        this.percentageDiscount = percentageDiscount;
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getTotal() >= amount;
    }

    @Override
    public double apply(Cart cart) {

        double totalDiscount = 0.0;

        if(isApplicable(cart)){
            totalDiscount = ((1.0 * percentageDiscount)/100) * cart.getTotal();
        }

        return totalDiscount;
    }

    @Override
    public String discountDetails() {
        return "Get discount of " + percentageDiscount + " for purchase greater than " + amount;
    }
}
