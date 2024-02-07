package com.example.mapper;

import com.example.entity.Article;
import com.example.entity.PageBean;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface ArticleMapper {


    @Insert("insert into article(title, content, cover_img, state, category_id, create_user, create_time, update_time)" +
            " values (#{title},#{content}, #{coverImg}, #{state}, #{categoryId},#{createUser},now(),now())")
    void add(Article article);
    List<Article> list(Integer userId, Integer categoryId, String state, String keyword);
    @Update("update article set title=#{title}, content=#{content}, state=#{state}, cover_img=#{coverImg}, create_user=#{createUser},update_time=now() where id=#{id}")
    void update(Article article);
    @Delete("delete from article where id =#{id}")
    void delete(Integer id);
    @Select("select * from article where id =#{id}")
    Article articleInfo(Integer id);
}
