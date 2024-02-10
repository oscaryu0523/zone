package com.example.service.impl;

import com.example.dto.CommentResponse;
import com.example.entity.Article;
import com.example.dto.ArticleInfoResponse;
import com.example.dto.PageBean;
import com.example.entity.Like;
import com.example.entity.User;
import com.example.mapper.ArticleMapper;
import com.example.mapper.CommentMapper;
import com.example.mapper.LikeMapper;
import com.example.mapper.UserMapper;
import com.example.service.ArticleService;
import com.example.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private LikeMapper likeMapper;
    //新增文章
    @Override
    public void add(Article article) {
        Map<String, Object> map= ThreadLocalUtil.get();
        Integer id=(Integer)map.get("id");
        article.setCreateUser(id);
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state, boolean isUser, String keyword) {
//        1.創建PageBean對象
        PageBean<Article> page = new PageBean<>();

//        2.開啟分頁查詢
        PageHelper.startPage(pageNum,pageSize);

//        3.調用mapper查詢
        Integer userId=null;
        if(isUser) {
            Map<String, Object> map = ThreadLocalUtil.get();
            userId = (Integer) map.get("id");
        }else{
            state="已發布";
        }
        List<Article> list= articleMapper.list(userId,categoryId,state, keyword);
        //Page中提供了方法，可以獲取PageHelper分頁查詢後 得到的總紀錄條樹和當前頁數據

        Page<Article> p =(Page<Article>) list;

        //把數據填充到pageBean中
        page.setTotal(p.getTotal());
        page.setItems(p.getResult());
        return page;
    }

    @Override
    public void update(Article article) {
        Map<String, Object> map= ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        article.setCreateUser(id);
        articleMapper.update(article);
    }

    @Override
    public void delete(Integer id) {
        articleMapper.delete(id);
    }
    //文章明細查詢
    @Override
    public ArticleInfoResponse articleInfo(Integer articleId,Integer userId) {
        Integer likeTypeId = likeMapper.getLikeTypeId(articleId, "article", userId);

//        根據文章id取得該篇 文章資訊
        Article article=articleMapper.articleInfo(articleId);
//        根據創建者id取得 用戶資訊
        User user = userMapper.findById(article.getCreateUser());
//        根據文章id取得所有 文章 按讚 跟 倒讚
        Integer articleGoodLikeNum = likeMapper.getLikeNum("article", articleId, 1);
        Integer articleBadLikeNum = likeMapper.getLikeNum("article", articleId, 2);
        //        根據文章id取得 所有留言資訊
        List<CommentResponse> list = commentMapper.findById(articleId,userId);


        ArticleInfoResponse articleInfoResponse = new ArticleInfoResponse();
//        存入dto中
        articleInfoResponse.setArticle(article);
        articleInfoResponse.setUsername(user.getUsername());
        articleInfoResponse.setNickname(user.getNickname());
        articleInfoResponse.setGoodLike(articleGoodLikeNum);
        articleInfoResponse.setBadLike(articleBadLikeNum);
        articleInfoResponse.setLikeTypeId(likeTypeId);
        articleInfoResponse.setCommentResponseList(list);


        return articleInfoResponse;
    }

}
