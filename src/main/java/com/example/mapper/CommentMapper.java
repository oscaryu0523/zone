package com.example.mapper;

import com.example.dto.CommentResponse;
import com.example.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment(article_id, create_id, content, create_time, update_time) " +
            "values(#{articleId},#{createId},#{content},now(),now())")
    void add(Comment comment);
    @Select("select c.id, c.create_id, u.nickname, u.user_pic, c.content, c.update_time from " +
            "comment c left join user u on c.create_id = u.id " +
            "where c.article_id=#{id}")
    List<CommentResponse> findById(Integer id);
    @Update("update comment set content = #{content}, update_time = now() where id = #{id}")
    void update(Integer id, String content);
}
