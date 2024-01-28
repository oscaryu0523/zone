package com.example.controller;

import com.example.entity.Result;
import com.example.entity.User;
import com.example.service.UserService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password){


            //查詢用戶
            User user = userService.findByUserName(username);
            if (user == null) {
                //用戶名不存在即可註冊
                userService.register(username, password);
                return Result.success();
            } else {
                //用戶名已存在
                return Result.error("用戶名已被占用");
            }

    }
    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password){
        //根據用戶名查詢User
        User user = userService.findByUserName(username);
        //判斷是否查詢到
        if(user == null){
            return Result.error("用戶名不正確");
        }
        String loginPassword= DigestUtils.md5DigestAsHex(password.getBytes());

        if(loginPassword.equals(user.getPassword())){
            return Result.success("登入成功");
        }

        return Result.error("密碼錯誤");
    }
}
