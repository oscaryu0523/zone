package com.example.controller;

import com.example.entity.Article;
import com.example.dto.ArticleInfoResponse;
import com.example.dto.PageBean;
import com.example.dto.Result;
import com.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping
    public Result add(@RequestBody @Validated Article article){
        articleService.add(article);
        return Result.success();
    }
    @PutMapping
    public Result update(@RequestBody @Validated Article article){
        System.out.println("文章更新執行");
        System.out.println(article.getContent());
        articleService.update(article);
        return Result.success();
    }
//    查看管理員發表的文章列表
    @GetMapping("/list")
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state

    ){
        PageBean<Article> page = articleService.list(pageNum,pageSize,categoryId,state,true, null);
        return Result.success(page);
    }
    //    查看所有文章列表
    @GetMapping("/listAll")
    public Result<PageBean<Article>> listAll(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String keyword

    ){
        PageBean<Article> page = articleService.list(pageNum,pageSize,categoryId,state, false,keyword);
        return Result.success(page);
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id){
        articleService.delete(id);
        return Result.success();
    }
    @GetMapping
    public Result<ArticleInfoResponse> articleInfo(@RequestParam Integer id){
        ArticleInfoResponse articleInfoResponse = articleService.articleInfo(id);
        return Result.success(articleInfoResponse);
    }


}
