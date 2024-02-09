package com.example.config;

import com.example.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登錄接口 和 註冊接口 不攔截
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/user/login","/user/register")//登入、註冊不攔截
                .excludePathPatterns("/article/listAll","/category/listAll")//文章列表頁面不攔截
                .excludePathPatterns("/article/search");//文章明細頁面不攔截

    }

}
