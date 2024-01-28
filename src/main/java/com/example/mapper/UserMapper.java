package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

@Mapper
public interface UserMapper {
    //根據用戶名查詢用戶
    @Select("select * from user where username = #{username}")
    User findByUserName(String username);

    //添加用戶
    @Insert("insert into user(username, password, create_time, update_time)" +
            " values(#{username}, #{hashPassword}, now(), now())")
    void add(String username, String hashPassword);

    //更新用戶資訊
    @Update("update user set nickname = #{nickname}, email = #{email}, update_time = #{updateTime} where id = #{id}")
    void update(User user);
    @Update("update user set user_pic = #{avatarUrl}, update_time = now() where id = #{id}")
    void updateAvatar(String avatarUrl, Integer id);
    @Update("update user set password = #{newPwd}, update_time = now() where id = #{id}")
    void updatePwd(String newPwd,Integer id);
}
