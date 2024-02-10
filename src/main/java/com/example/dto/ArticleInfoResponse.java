package com.example.dto;

import com.example.entity.Article;
import lombok.Data;

import java.util.List;

@Data
public class ArticleInfoResponse {
    Article article;
    List<CommentResponse> commentResponseList;
    String username;
    String nickname;
    Integer goodLike;
    Integer badLike;
    Integer likeTypeId;
}
