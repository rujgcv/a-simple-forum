package com.fufunode.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tab implements Serializable {
    private static final boolean TAB_ACTIVE = true; // 帖子启用
    private static final boolean TAB_DISABLED = false; // 帖子禁用
    private Long id;
    private Long typeId;
    private String type;
    private String name;
    private boolean status;
    private String description;
    private String imgUrl;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
