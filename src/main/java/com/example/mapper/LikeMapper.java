package com.example.mapper;

import com.example.entity.Like;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LikeMapper {
    //    根據類型 跟 編號查出 一筆按讚資料
    @Select("select  * from likes where target_type = #{targetType} and target_id = #{targetId}")
    Like getLike(String targetType, Integer targetId);

    //    插入一筆 按讚資料
    @Insert("insert into likes (target_id, target_type, like_type_id, create_id, create_time) " +
            "values(#{targetId},#{targetType},#{likeTypeId},#{createId},now())")
    void addLike(Like like);

    //    更新一筆 按讚資料
    @Update("update likes set like_type_id = #{likeTypeId}, create_time = now() where id = #{id}")
    void updateLikeType(Integer id, Integer likeTypeId);

    //    查出按讚數
    @Select("select count(*) from likes where target_type = #{targetType} and target_id = #{targetId} and like_type_id = #{likeTypeId}")
    Integer getLikeNum(String targetType, Integer targetId, Integer likeTypeId);
//    取消按讚
    @Delete("delete from likes where id = #{id}")
    void deleteById(Integer id);

    @Select("select like_type_id from likes where target_id = #{articleId} and target_type = #{targetType} and create_id = #{userId}")
    Integer getLikeTypeId(Integer articleId, String targetType, Integer userId);
}
