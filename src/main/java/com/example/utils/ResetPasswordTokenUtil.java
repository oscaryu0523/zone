package com.example.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ResetPasswordTokenUtil {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //  存儲重置密碼令牌

    public void saveResetPasswordToken(String token, String userEmail) {
        String key = "resetPasswordToken:" + token;
        redisTemplate.opsForValue().set(key, userEmail, 1800, TimeUnit.SECONDS);//默認為1800秒，即30分鐘
    }

    /*   獲取與重置密碼令牌關聯的用戶電子郵件
         用戶電子郵件，如果令牌不存在或已過期，則返回null
     */
    public String getUserEmailByToken(String token) {
        String key = "resetPasswordToken:" + token;
        return redisTemplate.opsForValue().get(key);
    }

//  刪除重置密碼令牌
    public void deleteResetPasswordToken(String token) {
        String key = "resetPasswordToken:" + token;
        redisTemplate.delete(key);
    }
}
