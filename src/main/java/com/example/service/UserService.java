package com.example.service;

import com.example.entity.User;

public interface UserService {
    //根據用戶名稱查詢用戶
    User findByUserName(String username);
    //註冊
    void register(String username, String password);

}
