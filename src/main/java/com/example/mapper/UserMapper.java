package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface UserMapper {
    //根據用戶名查詢用戶
    @Select("select * from user where username = #{username}")
    User findByUserName(String username);

    //添加用戶
    @Insert("insert into user(username, password, create_time, update_time)" +
            " values(#{username}, #{hashPassword}, now(), now())")
    void add(String username, String hashPassword);
}
