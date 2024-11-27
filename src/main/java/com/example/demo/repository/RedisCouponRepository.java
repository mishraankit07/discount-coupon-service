package com.example.demo.repository;

import com.example.demo.model.Coupon;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RedisCouponRepository implements CouponRepository {

    private RedisTemplate<String, String> redisTemplate;
    private ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(RedisCouponRepository.class);

    public RedisCouponRepository(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper){
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Coupon get(String key) throws JsonProcessingException {
        String responseString = redisTemplate.opsForValue().get(key);
        return objectMapper.readValue(responseString, Coupon.class);
    }

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        Set<String> couponKeys = redisTemplate.keys("*");

        if(couponKeys == null || couponKeys.isEmpty()){
            return new ArrayList<>();
        }

        List<Coupon> result = couponKeys.stream().map(key -> {
            try{
                return get(key);
            } catch (Exception e){
                logger.error("failed to load coupon with key:" + key + " from redis");
                return null;
            }
        }).filter(Objects::nonNull).toList();

        return result;
    }
}
