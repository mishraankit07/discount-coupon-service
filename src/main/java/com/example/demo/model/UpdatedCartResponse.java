package com.example.demo.model;

import java.util.List;

public record UpdatedCartResponse(List<Product> items, double totalPrice, double totalDiscount, double finalPrice){

}