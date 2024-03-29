package com.example.service.impl;

import com.example.dto.CommentResponse;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.example.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void add(Comment comment) {
        commentMapper.add(comment);
    }

    @Override
    public void update(Integer id, String content) {
        commentMapper.update(id, content);
    }

    @Override
    public void delete(Integer id) {
        commentMapper.delete(id);
    }


}
