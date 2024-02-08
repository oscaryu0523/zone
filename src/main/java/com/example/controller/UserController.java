package com.example.controller;

import com.example.dto.Result;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import com.example.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("username",user.getUsername());
            String token = JwtUtil.genToken(map);
            //把token存儲到redis中，令牌1小時候失效
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);


            return Result.success(token);
        }

        return Result.error("密碼錯誤");
    }
    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String)map.get("username");
        //根據用戶名查詢用戶
        User user = userService.findByUserName(username);
        return Result.success(user);
    }
    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token){
//        校驗參數
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd =  params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的參數");
        }

        //原密碼是否正確
        //調用userService根據用戶名拿到原密碼，在和old_pwd比對
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String)map.get("username");

        User user = userService.findByUserName(username);

        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(oldPwd.getBytes()))){
            return Result.error("原密碼錯誤");
        }

        //確認new_pwd是否跟re_pwd依樣

        if(!newPwd.equals(rePwd)){
            return Result.error("兩次填寫的新密碼不一樣");
        }

        //調用service完成更新
        userService.updatePwd(newPwd);

        //刪除redis中對應的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);

        return Result.success();
    }
}
