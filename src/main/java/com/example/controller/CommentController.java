package com.example.controller;

import com.example.dto.CommentResponse;
import com.example.entity.Comment;
import com.example.service.CommentService;
import com.example.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping
    public Result add(@RequestBody Comment comment){
        commentService.add(comment);
        return Result.success();
    }
    @PutMapping
    public Result update(
            @RequestParam Integer id,
            @RequestParam String content
    ){
        commentService.update(id,content);
        return Result.success();
    }

}
