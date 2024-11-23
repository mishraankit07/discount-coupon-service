package com.example.demo.coupon_type;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import java.util.*;
import java.util.stream.Collectors;

public class BxGy implements CouponType {

    private Set<Product> buyItems;
    private Set<Product> getItems;
    private final int buyItemCount;
    private final int getItemCount;
    private final int repetitionLimit;

    public BxGy(List<Product> buyItems, List<Product> getItems, int buyItemCount, int getItemCount, int repetitionLimit){
        this.buyItems = new HashSet<>();
        this.buyItems.addAll(buyItems);

        this.getItems = new HashSet<>();
        this.getItems.addAll(getItems);

        this.buyItemCount = buyItemCount;
        this.getItemCount = getItemCount;
        this.repetitionLimit = repetitionLimit;
    }

    @Override
    public boolean isApplicable(Cart cart) {

        int buyItemsInCart = 0;
        int getItemsInCart = 0;

        for(Product product: cart.products()){
            if(buyItems.contains(product)){
                buyItemsInCart = buyItemsInCart + product.units();
            }

            else if(getItems.contains(product)){
                getItemsInCart = getItemsInCart + product.units();
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
                    buyItemsInCart = buyItemsInCart + product.units();
                }

                else if(getItems.contains(product)){
                    productsToGiveForFree.add(product);
                    getItemsInCart = getItemsInCart + product.units();
                }
            }

            int numberOfRepetitions = buyItemsInCart / buyItemCount;
            int repetitionsToConsider = Math.min(numberOfRepetitions, repetitionLimit);
            int numberOfItemsToGiveForFree = repetitionsToConsider * Math.min(getItemCount, getItemsInCart);

            int idx = 0;
            while(numberOfItemsToGiveForFree > 0 && idx < productsToGiveForFree.size()){
                if(numberOfItemsToGiveForFree >= productsToGiveForFree.get(idx).units()){
                    discountAmount = discountAmount + productsToGiveForFree.get(idx).units() * productsToGiveForFree.get(idx).perUnitPrice();
                    numberOfItemsToGiveForFree -= productsToGiveForFree.get(idx).units();
                }

                else{
                    discountAmount = discountAmount + numberOfItemsToGiveForFree * productsToGiveForFree.get(idx).perUnitPrice();
                    numberOfItemsToGiveForFree = 0;
                }

                idx++;
            }

        }

        return discountAmount;
    }

    @Override
    public String discountDetails() {
        return "On purchase of " + buyItemCount + "from " + buyItems.stream().map(Product::name).collect(Collectors.joining(","))
                + " you get " + getItemCount + " free from "
                + getItems.stream().map(Product::name).collect(Collectors.joining(","));
    }
}
