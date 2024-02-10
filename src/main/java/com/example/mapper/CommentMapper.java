package com.example.mapper;

import com.example.dto.CommentResponse;
import com.example.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment(article_id, create_id, content, create_time, update_time) " +
            "values(#{articleId},#{createId},#{content},now(),now())")
    void add(Comment comment);
//    查詢留言資訊 join用戶表格 及 按讚表格
//    @Select("select c.id, c.create_id, u.nickname, u.user_pic, c.content, c.update_time from " +
//            "comment c left join user u on c.create_id = u.id " +
//            "where c.article_id=#{id}")

//    @Select("SELECT " +
//            "c.id, " +
//            "c.create_id AS createId, " +
//            "u.nickname, " +
//            "u.user_pic AS userPic, " +
//            "c.content, " +
//            "c.update_time AS updateTime, " +
//            "(SELECT COUNT(*) FROM likes WHERE target_id = c.id AND target_type = 'comment' AND like_type_id = 1) AS goodLike, " +
//            "(SELECT COUNT(*) FROM likes WHERE target_id = c.id AND target_type = 'comment' AND like_type_id = 2) AS badLike, " +
//            "(SELECT like_type_id FROM likes WHERE target_id = c.id AND target_type = 'comment' AND create_id = #{userId}) AS likeTypeId " +
//            "FROM comment c " +
//            "LEFT JOIN user u ON c.create_id = u.id " +
//            "WHERE c.article_id = #{articleId}")
    List<CommentResponse> findById(Integer articleId,Integer userId);
    @Update("update comment set content = #{content}, update_time = now() where id = #{id}")
    void update(Integer id, String content);
    @Delete("delete from comment where id = #{id}")
    void delete(Integer id);

}
