package com.example.service.impl;

import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import com.example.service.CategoryService;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public void add(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        category.setCreateUser(id);
//        檢查該分類是否已經存在
        List<Category> list = categoryMapper.list(null);
        boolean exists=list.stream()
                        .anyMatch(c -> c.getCategoryName().equals(category.getCategoryName()));
        if(!exists) {
            categoryMapper.add(category);
        }else{
            throw new RuntimeException("該分類已經存在");
        }
    }


    @Override
    public List<Category> list(boolean isUser) {
        Integer id=null;
        if(isUser) {
            Map<String, Object> map = ThreadLocalUtil.get();
            id = (Integer) map.get("id");
        }
        return categoryMapper.list(id);
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.findById(id);
    }

    @Override
    public void update(Category category) {
        categoryMapper.update(category);
    }

    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }
}
