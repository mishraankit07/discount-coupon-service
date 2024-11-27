package com.example.demo.repository;

import com.example.demo.model.Coupon;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CouponRepository {
    public Coupon get(String key) throws JsonProcessingException;

    public void set(String key, String value);

    public void delete(String key);

    public List<Coupon> getAllCoupons();
}
