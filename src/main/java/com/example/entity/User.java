package com.example.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class User {
    @NotNull
    private Integer id; // 主鍵ID
    private String username; // 用戶名

    @JsonIgnore
    private String password; // 密碼

    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname; // 昵稱

    @Email
    @NotEmpty
    private String email; // 郵箱
    private String userPic; // 用戶頭像地址
    private LocalDateTime createTime; // 創建時間
    private LocalDateTime updateTime; // 更新時間
}

