package com.example.service;

import com.example.entity.Article;
import com.example.dto.ArticleInfoResponse;
import com.example.dto.PageBean;

public interface ArticleService {
    //新增文章
    void add(Article article);
    //條件分頁列表查詢
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state, boolean isUser, String keyword);

    void update(Article article);

    void delete(Integer id);

    ArticleInfoResponse articleInfo(Integer articleId, Integer userId);
}
