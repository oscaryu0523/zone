package com.example.service;

import com.example.entity.Article;
import com.example.entity.PageBean;

public interface ArticleService {
    //新增文章
    void add(Article article);
    //條件分頁列表查詢
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);
}
