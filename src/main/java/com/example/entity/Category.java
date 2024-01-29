package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
@Data
public class Category {
    @NotNull(groups= Update.class)
    private Integer id; // 主鍵ID
    @NotEmpty(groups={Add.class, Update.class})
    private String categoryName; // 分類名稱
    @NotEmpty(groups={Add.class, Update.class})
    private String categoryAlias; // 分類別名
    private Integer createUser; // 創建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 創建時間
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 更新時間

    public interface Add {

    }
    public interface Update{

    }
}
