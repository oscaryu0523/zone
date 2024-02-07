package com.example.entity;

import lombok.Data;

@Data
public class ArticleInfoResponse {
    Article article;
    String username;
    String nickname;
}
