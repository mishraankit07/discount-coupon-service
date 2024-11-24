package com.example.demo.model.coupon_type;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class BxGy implements CouponType {

    private Set<Product> buyItems;
    private Set<Product> getItems;
    private int buyItemCount;
    private int getItemCount;
    private int repetitionLimit;

    public BxGy(@JsonProperty("buyItems") List<Product> buyItems,
                @JsonProperty("getItems") List<Product> getItems,
                @JsonProperty("buyItemCount") int buyItemCount,
                @JsonProperty("getItemCount") int getItemCount,
                @JsonProperty("repetitionLimit") int repetitionLimit){
        this.buyItems = new HashSet<>();
        this.buyItems.addAll(buyItems);

        this.getItems = new HashSet<>();
        this.getItems.addAll(getItems);

        this.buyItemCount = buyItemCount;
        this.getItemCount = getItemCount;
        this.repetitionLimit = repetitionLimit;
    }

    @Override
    public String getName() {
        return "bxgy";
    }

    @Override
    public boolean isApplicable(Cart cart) {

        int buyItemsInCart = 0;
        int getItemsInCart = 0;

        for(Product product: cart.products()){
            if(buyItems.contains(product)){
                buyItemsInCart = buyItemsInCart + product.getUnits();
            }

            else if(getItems.contains(product)){
                getItemsInCart = getItemsInCart + product.getUnits();
            }
        }

        if(buyItemsInCart >= buyItemCount && getItemsInCart > 0){
            return true;
        }

        return false;
    }

    @Override
    public double apply(Cart cart) {

        double discountAmount = 0;

        if(isApplicable(cart)){

            int buyItemsInCart = 0;
            int getItemsInCart = 0;
            List<Product> productsToGiveForFree = new ArrayList<>();

            for(Product product: cart.products()){
                if(buyItems.contains(product)){
                    buyItemsInCart = buyItemsInCart + product.getUnits();
                }

                else if(getItems.contains(product)){
                    productsToGiveForFree.add(product);
                    getItemsInCart = getItemsInCart + product.getUnits();
                }
            }

            int numberOfRepetitions = buyItemsInCart / buyItemCount;
            int repetitionsToConsider = Math.min(numberOfRepetitions, repetitionLimit);
            int numberOfItemsToGiveForFree = repetitionsToConsider * Math.min(getItemCount, getItemsInCart);

            int idx = 0;
            while(numberOfItemsToGiveForFree > 0 && idx < productsToGiveForFree.size()){

                Product product = productsToGiveForFree.get(idx);

                if(numberOfItemsToGiveForFree >= product.getUnits()){
                    product.setDiscount(product.getUnits() * product.getPerUnitPrice());
                    discountAmount = discountAmount + product.getUnits() * product.getPerUnitPrice();
                    numberOfItemsToGiveForFree -= product.getUnits();
                }

                else{
                    product.setDiscount(numberOfItemsToGiveForFree * product.getPerUnitPrice());
                    discountAmount = discountAmount + numberOfItemsToGiveForFree * product.getPerUnitPrice();
                    numberOfItemsToGiveForFree = 0;
                }

                idx++;
            }

        }

        return discountAmount;
    }

    @Override
    public String discountDetails() {
        return "On purchase of " + buyItemCount + " items from " + buyItems.stream().map(Product::getName).collect(Collectors.joining(","))
                + " you get " + getItemCount + " items free from "
                + getItems.stream().map(Product::getName).collect(Collectors.joining(","));
    }
}
