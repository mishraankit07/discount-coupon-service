package com.example.demo.model;

import java.util.List;

public record Cart(List<Product> products) {

    public double getTotal(){

        double cartAmount = 0.0;

        for(Product product: products){
            cartAmount += product.getUnits() * product.getPerUnitPrice();
        }

        return cartAmount;
    }
}
