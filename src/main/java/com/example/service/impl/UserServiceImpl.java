package com.example.service.impl;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findByUserName(String username) {
        User user=userMapper.findByUserName(username);
        return user;
    }

    @Override
    public void register(String username, String password) {
        if(password == null){
            throw new IllegalArgumentException("密碼不能為空");
        }
        //加密
        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        //添加
        userMapper.add(username,hashPassword);


    }
}
