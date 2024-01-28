package com.example;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    @Test
    public void testGen(){
        // 創建一個HashMap來存儲JWT的聲明（claims）
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",1); // 添加聲明：用戶ID
        claims.put("username","張三"); // 添加聲明：用戶名
//        生成jwt的代碼
        String token = JWT.create()
                .withClaim("user", claims)// 添加自定義聲明
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))//添加過期時間，這裡設置為當前時間後12小時
                .sign(Algorithm.HMAC256("oscar"));//指定算法和密鑰來簽名JWT，這裡使用的是ECDSA256算法

        System.out.println(token);
    }
    @Test
    public void testParse(){
        //定義字符串，模擬用戶傳遞過來的token
        String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8teS4iSJ9LCJleHAiOjE3MDY0NzE4Mjl9" +
                ".kwbvwLUi1q3X7__20k99HHBL6D2cXWoGHfrpC5rKLkk";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("oscar")).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);//驗證token，生成一個解析後的jwt對象
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));

    }
}
