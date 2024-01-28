package com.example.entity;



import lombok.Data;

import java.time.LocalDateTime;
@Data
public class User {
    private Integer id; // 主鍵ID
    private String username; // 用戶名
    private String password; // 密碼
    private String nickname; // 昵稱
    private String email; // 郵箱
    private String userPic; // 用戶頭像地址
    private LocalDateTime createTime; // 創建時間
    private LocalDateTime updateTime; // 更新時間
}

