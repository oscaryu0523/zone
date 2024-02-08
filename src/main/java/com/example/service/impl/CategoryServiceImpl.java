package com.example.service.impl;

import com.example.entity.Article;
import com.example.entity.Category;
import com.example.mapper.ArticleMapper;
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
    @Autowired
    private ArticleMapper articleMapper;
    @Override
    public void add(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        category.setCreateUser(id);
//        檢查該分類是否已經存在
        List<Category> list = categoryMapper.list(null);
        boolean exists=list.stream()
                        .anyMatch(c -> c.getCategoryName().equals(category.getCategoryName()));
//        不存在才可以新增
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
//        檢查該分類是否有文章
        List<Article> list = articleMapper.list(null, id, null, null);
        Category category = categoryMapper.findById(id);
        boolean exists=list.stream()
                        .anyMatch(c -> c.getCategoryId()==id);
//        不存在才可以刪除
        if(!exists) {
            categoryMapper.delete(id);
        }else{
            throw new RuntimeException(category.getCategoryName()+"的文章已經存在，無法刪除");
        }
    }
}
