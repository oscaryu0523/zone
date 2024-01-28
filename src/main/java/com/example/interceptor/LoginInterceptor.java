package com.example.interceptor;

import com.example.entity.Result;
import com.example.utils.JwtUtil;
import com.example.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);

            //把業務數據存儲到ThreadLocal中
            ThreadLocalUtil.set(claims);

            //放行
            return true;
        } catch (Exception e) {
            //不放行
            response.setStatus(401);
            return false;
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception{
        //清空ThreadLocal中的數據
        ThreadLocalUtil.remove();
    }
}

