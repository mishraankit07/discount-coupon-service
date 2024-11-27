package com.example.demo.utils;

public class RedisUtils {

    public static final String initialKeyIdentifier = "coupon";
    public static final String splitter = "#";

    public static String generateKey(String id){
        return initialKeyIdentifier + splitter + id;
    }
}
