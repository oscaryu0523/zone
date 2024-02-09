package com.example.service.impl;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Map;

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
    public void register(String username, String password, String nickname, String email) {
        if(password == null){
            throw new IllegalArgumentException("密碼不能為空");
        }
        //加密
        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        //添加
        userMapper.add(username,hashPassword,nickname, email);


    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        userMapper.updateAvatar(avatarUrl,id);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        userMapper.updatePwd(DigestUtils.md5DigestAsHex(newPwd.getBytes()),id);
    }


}
