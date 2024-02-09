package com.example.service;

import com.example.dto.CommentResponse;
import com.example.entity.Comment;

import java.util.List;

public interface CommentService {
    void add(Comment comment);

    void update(Integer id, String content);

    void delete(Integer id);
}
