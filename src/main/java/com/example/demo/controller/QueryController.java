package com.example.demo.controller;

import com.example.demo.model.Coupon;
import com.example.demo.service.CouponManagerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class QueryController {

    private final CouponManagerService couponManagerService;
    private final ObjectMapper objectMapper;

    public QueryController(CouponManagerService couponManagerService, ObjectMapper objectMapper){
        this.couponManagerService = couponManagerService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/coupons/{id}")
    public ResponseEntity<String> getCouponById(@PathVariable String id) throws JsonProcessingException {
        Optional<Coupon> couponOptional = couponManagerService.getCouponById(id);
        if(couponOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("coupon with id:" + id + " doesn't exist");
        }

        return ResponseEntity.ok().body(objectMapper.writeValueAsString(couponOptional.get()));
    }

    @GetMapping("/coupons")
    public ResponseEntity<String> getAllCoupons() throws JsonProcessingException {
        List<Coupon> coupons = couponManagerService.getAllCoupons();
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(coupons));
    }
}
