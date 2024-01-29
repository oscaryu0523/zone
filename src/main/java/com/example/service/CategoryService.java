package com.example.service;

import com.example.entity.Category;

import java.util.List;

public interface CategoryService {
    //新增分類
    void add(Category category);
    //列表查詢
    List<Category> list();
    //根據id查詢分類信息
    Category findById(Integer id);
    //根據id更新分類信息
    void update(Category category);
    //根據id刪除分類信息
    void delete(Integer id);
}
