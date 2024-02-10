package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Like {
    private Integer id;
    private Integer targetId;//用於存儲被按讚的對象的ID，無論它是文章還是留言
    private String targetType;//用於區分按讚的對象是文章還是留言
    private Integer likeTypeId;// 讚類型編號  1-讚  2-倒讚
    private Integer createId;
    private LocalDateTime createTime;
}
