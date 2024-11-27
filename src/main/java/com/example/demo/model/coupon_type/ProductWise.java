package com.example.demo.model.coupon_type;

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

    private double calculateProductDiscount(Product product){
        return ((1.0*productWisePercentageDiscount.getOrDefault(product, 0))/100)*product.getPerUnitPrice()*product.getUnits();
    }

    @Override
    public String getName() {
        return "product-wise";
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

        double totalDiscount = 0.0;

        for(Product product: cart.products()){
           double productDiscount = calculateProductDiscount(product);
           product.setDiscount(productDiscount);
           totalDiscount += productDiscount;
        }

        return totalDiscount;
    }

    @Override
    public String discountDetails() {
        return "Get guaranteed discount by adding:"
                + productWisePercentageDiscount.keySet().stream().map((Product::getName)).collect(Collectors.joining(","))
                + " to your cart";
    }

    @Override
    public String toString() {
        return "ProductWise{" +
                "productWisePercentageDiscount=" + productWisePercentageDiscount +
                '}';
    }
}