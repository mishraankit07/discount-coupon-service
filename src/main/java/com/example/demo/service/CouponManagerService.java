package com.example.demo.service;

import com.example.demo.model.*;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.OptionalInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CouponManagerService {

    private List<Coupon> couponList;

    public CouponManagerService() {
        couponList = new ArrayList<>();
    }

    public void clearCoupons(){
        couponList.clear();
    }

    public void createCoupon(Coupon newCoupon){
        couponList.add(newCoupon);
    }

    public List<Coupon> getAllCoupons(){
        return couponList;
    }

    public Optional<Coupon> getCouponById(String id){
        return couponList.stream()
                          .filter((coupon -> coupon.getId().contentEquals(id))).findFirst();
    }

    public boolean updateCouponById(String id, Coupon updatedCoupon){

        OptionalInt indexOpt = IntStream.range(0, couponList.size())
                .filter(i -> couponList.get(i).getId().contentEquals(id)).findFirst();

        if(indexOpt.isEmpty()){
            return false;
        }

        couponList.set(indexOpt.getAsInt(), updatedCoupon);
        return true;
    }

    public boolean deleteCouponById(String id){
        Optional<Coupon> maybeCoupon = getCouponById(id);

        if(maybeCoupon.isEmpty()){
            return false;
        }

        couponList.remove(maybeCoupon.get());
        return true;
    }

    public List<CouponOnCartResponse> getAllApplicableCouponsResponse(Cart cart){
        return couponList.stream().filter(coupon -> coupon.isApplicable(cart))
                                  .map(coupon -> new CouponOnCartResponse(coupon.getId(), coupon.getCouponType(), coupon.getDiscount(cart))).collect(Collectors.toList());

    }

    public Optional<UpdatedCartResponse> applyCouponWithId(String couponId, Cart cart){
        Optional<Coupon> maybeCoupon = getCouponById(couponId);

        double totalPrice = cart.getTotal();

        if(maybeCoupon.isPresent()){
            Coupon toApplyCoupon = maybeCoupon.get();
            double discountAmount = toApplyCoupon.getDiscount(cart);
            double finalPrice = totalPrice - discountAmount;
            return Optional.of(new UpdatedCartResponse(cart.products(), totalPrice, discountAmount, finalPrice));
        }

        return Optional.empty();
    }
}
