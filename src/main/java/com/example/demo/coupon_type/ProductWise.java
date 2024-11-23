package com.example.demo.coupon_type;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;

import java.util.HashMap;
import java.util.stream.Collectors;

public class ProductWise implements CouponType {

    HashMap<Product, Integer> productWisePercentageDiscount;

    public ProductWise(){
        productWisePercentageDiscount = new HashMap<>();
    }

    public void addProductDiscount(Product product, Integer percentDiscount){
        productWisePercentageDiscount.put(product, percentDiscount);
    }

    public void removeProductDiscount(Product product){
        productWisePercentageDiscount.remove(product);
    }

    @Override
    public boolean isApplicable(Cart cart) {
        for(Product product: cart.products()){
            if(productWisePercentageDiscount.containsKey(product)){
                return true;
            }
        }

        return false;
    }

    @Override
    public double apply(Cart cart) {

        double totalAmount = 0;

        for(Product product: cart.products()){
            if(productWisePercentageDiscount.containsKey(product)){
                int discountPercentage = productWisePercentageDiscount.get(product);
                double actualCost = product.units() * product.perUnitPrice();
                totalAmount += (1 - (1.0 * discountPercentage)/100)*actualCost;
            }

            else{
                totalAmount += product.units() * product.perUnitPrice();
            }
        }

        return (cart.getTotal() - totalAmount);
    }

    @Override
    public String discountDetails() {
        return "Get guaranteed discount by adding:"
                + productWisePercentageDiscount.keySet().stream().map((Product::name)).collect(Collectors.joining(","))
                + " to your cart";
    }
}