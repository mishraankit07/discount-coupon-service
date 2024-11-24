package com.example.demo.model.coupon_type;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CartWise implements CouponType {

    private int percentageDiscount;
    private double amount;

    @JsonCreator
    public CartWise(@JsonProperty("amount") double amount, @JsonProperty("percentageDiscount") int percentageDiscount){
        this.amount = amount;
        this.percentageDiscount = percentageDiscount;
    }

    @Override
    public String getName() {
        return "cart-wise";
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getTotal() >= amount;
    }

    private double calculateProductDiscount(Product product){
        return ((1.0 * percentageDiscount)/100)*(product.getUnits() * product.getPerUnitPrice());
    }

    @Override
    public double apply(Cart cart) {

        double totalDiscount = 0.0;

        if(isApplicable(cart)){
            for(int idx = 0; idx < cart.products().size(); idx++){
                Product product = cart.products().get(idx);
                double productDiscount = calculateProductDiscount(product);
                product.setDiscount(productDiscount);
                totalDiscount += productDiscount;
            }
        }

        return totalDiscount;
    }

    @Override
    public String discountDetails() {
        return "Get discount of " + percentageDiscount + " for purchase greater than " + amount;
    }
}
