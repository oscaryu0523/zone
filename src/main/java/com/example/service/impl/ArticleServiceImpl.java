package com.example.service.impl;

import com.example.entity.Article;
import com.example.entity.PageBean;
import com.example.mapper.ArticleMapper;
import com.example.service.ArticleService;
import com.example.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.experimental.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    //新增文章
    @Override
    public void add(Article article) {
        Map<String, Object> map= ThreadLocalUtil.get();
        Integer id=(Integer)map.get("id");
        article.setCreateUser(id);
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
//        1.創建PageBean對象
        PageBean<Article> page = new PageBean<>();

//        2.開啟分頁查詢
        PageHelper.startPage(pageNum,pageSize);

//        3.調用mapper查詢
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer)map.get("id");
        List<Article> list= articleMapper.list(userId,categoryId,state);
        //Page中提供了方法，可以獲取PageHelper分頁查詢後 得到的總紀錄條樹和當前頁數據

        Page<Article> p =(Page<Article>) list;

        //把數據填充到pageBean中
        page.setTotal(p.getTotal());
        page.setItems(p.getResult());
        return page;
    }
}
