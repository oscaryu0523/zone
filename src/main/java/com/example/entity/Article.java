package com.example.entity;


import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Article {
    private Integer id; // 主鍵ID
    private String title; // 文章標題
    private String content; // 文章內容
    private String coverImg; // 封面圖像
    private String state; // 發布狀態 已發布|草稿
    private Integer categoryId; // 文章分類id
    private Integer createUser; // 創建人ID
    private LocalDateTime createTime; // 創建時間
    private LocalDateTime updateTime; // 更新時間
}
