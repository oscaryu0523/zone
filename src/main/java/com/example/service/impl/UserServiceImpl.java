package com.example.service.impl;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.utils.Mail;
import com.example.utils.ResetPasswordTokenUtil;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Mail mail;
    @Autowired
    private ResetPasswordTokenUtil resetPasswordTokenUtil;
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
    public void updatePwd(String newPwd,Integer id) {
        if(id==null) {
            Map<String, Object> map = ThreadLocalUtil.get();
            id = (Integer) map.get("id");
        }
        userMapper.updatePwd(DigestUtils.md5DigestAsHex(newPwd.getBytes()),id);
    }

    @Override
    public User checkEmail(String email) throws MessagingException {
        User user=userMapper.findByEmail(email);
        if(user != null){
//            發送mail通知，給修改密碼的連接 並設定連接的有效時間
            // 創建一個Multipart對象，用於添加HTML內容
            Multipart multipart = new MimeMultipart();

            // 創建一個MimeBodyPart對象，並設置HTML內容
            MimeBodyPart htmlPart = new MimeBodyPart();
//            定義日期格式
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            生成令牌
            String token = UUID.randomUUID().toString();
//            將令牌存到redis
            resetPasswordTokenUtil.saveResetPasswordToken(token,email);

//            郵件內容
            String htmlContent =
                    "<html>"
                    + "<head>"
                    + "<title>墨語閣 忘記密碼確認</title>"
                    + "</head>"
                    + "<body>"
                    + "<h2>您好，"+user.getUsername()+"：</h2>"
                    + "<p>我們在 "+formatter.format(new Date())+" 收到了您的密碼重置請求郵件。要重置您的密碼，請點擊以下連結</p>"
                    + "<a href='http://localhost:1080/reset?token="+token+"'>重置密碼連結</a>"
                    + "<p>為了安全考慮，該連結將在30分鐘後失效。如果您無法在該時間段內重置密碼，請回到登錄頁面，點擊「忘記密碼」連結。</p>"
                    + "<p>如果有其他問題，請聯繫客服支持。謝謝您。</p>"
                    + "<p>墨語閣客服郵箱：z5314888@gamil.com</p>"
                    + "<p>墨語閣 余銘修敬上</p>"
                    + "</body>"
                    + "</html>";

            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");

            // 將HTML內容添加到Multipart中
            multipart.addBodyPart(htmlPart);

            mail.sendMail(email,"墨語閣 忘記密碼確認",multipart);

        }else{
            throw new RuntimeException("該信箱尚未註冊");
        }
        return userMapper.findByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.findByEmail(email);
    }

}
