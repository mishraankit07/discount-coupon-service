package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Coupon;
import com.example.demo.model.CouponOnCartResponse;
import com.example.demo.model.UpdatedCartResponse;
import com.example.demo.repository.CouponRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.example.demo.utils.RedisUtils.generateKey;

@Service
@EnableScheduling
@EnableAsync
public class CouponManagerService {

    // id -> coupon map
    private ConcurrentHashMap<String, Coupon> coupons;
    private CouponRepository couponRepository;
    private ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger("CouponManagementService");

    public CouponManagerService(CouponRepository couponRepository, ObjectMapper objectMapper) {
        coupons = new ConcurrentHashMap<>();
        this.couponRepository = couponRepository;
        this.objectMapper = objectMapper;
    }

    public void clearCoupons(){
        coupons.clear();
    }

    public void createCoupon(Coupon newCoupon){
        coupons.put(newCoupon.getId(), newCoupon);

        try {
            couponRepository.set(generateKey(newCoupon.getId()), objectMapper.writeValueAsString(newCoupon));
        }catch (Exception e){
            logger.error("failed to write coupon with id:" + newCoupon.getId() + " to redis");
        }
    }

    public List<Coupon> getAllCoupons(){
        return coupons.values().stream().toList();
    }

    public Optional<Coupon> getCouponById(String id){
        return Optional.ofNullable(coupons.get(id));
    }

    public boolean updateCouponById(String id, Coupon updatedCoupon){

        Optional<Coupon> maybeCoupon = Optional.ofNullable(coupons.get(id));

        if(maybeCoupon.isEmpty()){
            return false;
        }

        coupons.put(updatedCoupon.getId(), updatedCoupon);
        try {
            couponRepository.set(generateKey(updatedCoupon.getId()), objectMapper.writeValueAsString(updatedCoupon));
        }catch (Exception e){
            logger.error("failed to update coupon with id:" + id + " to redis");
        }
        return true; // set only returns true if coupon is not present in it
    }

    public boolean deleteCouponById(String id){
        Optional<Coupon> maybeCoupon = getCouponById(id);

        if(maybeCoupon.isEmpty()){
            return false;
        }

        coupons.remove(id);
        couponRepository.delete(generateKey(id));
        return true;
    }

    public List<CouponOnCartResponse> getAllApplicableCouponsResponse(Cart cart){
        return coupons.values().stream().filter(coupon -> coupon.isApplicable(cart))
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

    @PostConstruct
    public void loadAllCoupons(){
        List<Coupon> loadedCoupons = couponRepository.getAllCoupons();
        coupons = loadedCoupons
                .stream().collect(Collectors.toMap(Coupon::getId, coupon -> coupon, (v1, v2) -> v1, ConcurrentHashMap::new));

        logger.info("loaded:" + coupons.size() + " coupons from redis on startup");
    }
}