package com.example.service;

import com.example.entity.User;

import java.util.Map;

public interface UserService {
    //根據用戶名稱查詢用戶
    User findByUserName(String username);
    //註冊
    void register(String username, String password, String nickname, String email);
    //更新
    void update(User user);
    //更新頭像
    void updateAvatar(String avatarUrl);

    void updatePwd(String newPwd);


}

