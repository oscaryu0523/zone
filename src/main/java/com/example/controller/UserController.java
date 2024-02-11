package com.example.controller;

import com.example.dto.Result;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import com.example.utils.Mail;
import com.example.utils.ResetPasswordTokenUtil;
import com.example.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
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
    @Autowired
    private ResetPasswordTokenUtil resetPasswordTokenUtil;

    @PostMapping("/register")
    public Result register(
            @Pattern(regexp = "^\\S{2,10}$", message = "暱稱必須是2到10位的非空白字符") String nickname
            ,@Email String email
            ,@Pattern(regexp = "^\\S{5,16}$", message = "用戶名必須是5到16位的非空白字符") String username
            ,@Pattern(regexp = "^\\S{5,16}$", message = "密碼必須是5到16位的非空白字符") String password){


            //查詢用戶
            User user = userService.findByUserName(username);
            if (user == null) {
                //用戶名不存在即可註冊
                User user1 = new User();
                userService.register(username, password,nickname,email);
                return Result.success();
            } else {
                //用戶名已存在
                return Result.error("用戶名已被占用");
            }

    }
    @PostMapping("/login")
    public Result login(
            @Pattern(regexp = "^\\S{5,16}$", message = "用戶名必須是5到16位的非空白字符") String username
            ,@Pattern(regexp = "^\\S{5,16}$", message = "密碼必須是5到16位的非空白字符") String password
            ,boolean remember
    ){
        System.out.println("remember="+remember);
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
            String token = JwtUtil.genToken(map,remember);
            //把token存儲到redis中，令牌1小時候失效
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            long duration = remember ? 24 * 10 : 6; // 10天或1小時
            operations.set(token, token, duration, TimeUnit.HOURS);


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
        userService.updatePwd(newPwd,null);

        //刪除redis中對應的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);

        return Result.success();
    }
//    忘記密碼接口
    @GetMapping("/checkEmail")
    public Result checkEmail(@RequestParam @Email String email) throws MessagingException {
        User user=userService.checkEmail(email);
        return Result.success();
    }

//    取得token對應的email接口
    @GetMapping("/getEmail")
    public Result getEmail(@RequestParam String token){
        String email = resetPasswordTokenUtil.getUserEmailByToken(token);
        return Result.success(email);
    }

    @PatchMapping("/resetPwd")
    public Result resetPwd(@RequestBody Map<String, String> params){
        System.out.println("進入重置密碼");
//        校驗參數
        String token = params.get("token");
        String email = params.get("email");
        String newPwd = params.get("new_pwd");
        String rePwd =  params.get("re_pwd");

        if(!StringUtils.hasLength(token)||!StringUtils.hasLength(email) ||
                !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的參數");
        }

        // 從Redis檢查token是否存在且未過期
        String storedEmail = resetPasswordTokenUtil.getUserEmailByToken(token);
        if(storedEmail == null){
            return Result.error("令牌不存在或已過期。");
        }
//      確認token對應的email是否和請求中的email一致
        if(!email.equals(storedEmail)){
            return Result.error("令牌無效或與郵箱不匹配。");
        }

//        根據email取得用戶資訊
        User user=userService.getUserByEmail(email);

        //確認new_pwd是否跟re_pwd依樣

        if(!newPwd.equals(rePwd)){
            return Result.error("兩次填寫的新密碼不一樣");
        }

        //調用service完成更新
        userService.updatePwd(newPwd, user.getId());

        //刪除redis中對應的token
        resetPasswordTokenUtil.deleteResetPasswordToken(token);

        return Result.success();
    }
}
