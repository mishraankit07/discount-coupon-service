package com.example.demo.model;

import java.util.Objects;

public class Product {

    private String id;
    private String name;
    private int units;
    private double perUnitPrice;
    private double discount;

    public Product(String id, String name, int units, double perUnitPrice){
        this.id = id;
        this.name = name;
        this.units = units;
        this.perUnitPrice = perUnitPrice;
        this.discount = 0;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getUnits(){
        return units;
    }

    public double getPerUnitPrice() {
        return perUnitPrice;
    }

    public void setDiscount(double discount){
        this.discount = discount;
    }

    public double getDiscount(){
        return discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Double.compare(perUnitPrice, product.perUnitPrice) == 0 && Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, perUnitPrice);
    }
}