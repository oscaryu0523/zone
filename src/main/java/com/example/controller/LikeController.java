package com.example.controller;

import com.example.dto.Result;
import com.example.entity.Like;
import com.example.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;
    @PostMapping
    public Result like(@RequestBody Like like){
        likeService.like(like);
        return Result.success();
    }

}

