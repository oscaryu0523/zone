package com.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String KEY = "oscar";
	
	//接收業務資料,產生token並返回
    public static String genToken(Map<String, Object> claims, boolean remember) {
        long expiryTimeInMillis = remember ? 1000 * 60 * 60 * 24 * 10 : 1000 * 60 * 60 * 6; // 10天或1小時
        System.out.println(System.currentTimeMillis() + expiryTimeInMillis);
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryTimeInMillis))
                .sign(Algorithm.HMAC256(KEY));
    }

	//接收token,驗證token,並傳回業務數據
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

}
