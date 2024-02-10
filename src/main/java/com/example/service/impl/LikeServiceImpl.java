package com.example.service.impl;

import com.example.entity.Like;
import com.example.mapper.LikeMapper;
import com.example.service.LikeService;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeMapper likeMapper;

    //新增一筆按讚紀錄
    @Override
    public void like(Like like) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        like.setCreateId(id);
//        根據 targetType targetId 查出有沒有紀錄
        Like existLike = likeMapper.getLike(like.getTargetType(), like.getTargetId());
        //如果按讚紀錄為空 則修改
        if (existLike == null) {
//            新增一筆紀錄
            likeMapper.addLike(like);
//        如果欲按讚類型 與 存在類型不依樣
        }else if(existLike.getLikeTypeId() != like.getLikeTypeId()){
            likeMapper.updateLikeType(existLike.getId(),like.getLikeTypeId());
//        如果欲按讚類型 與 存在類型依樣
        }else if(existLike.getLikeTypeId() == like.getLikeTypeId()) {
            likeMapper.deleteById(existLike.getId());
        }

    }
}

