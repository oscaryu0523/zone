package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Category {
    private Integer id; // 主鍵ID
    private String categoryName; // 分類名稱
    private String categoryAlias; // 分類別名
    private Integer createUser; // 創建人ID
    private LocalDateTime createTime; // 創建時間
    private LocalDateTime updateTime; // 更新時間
}
