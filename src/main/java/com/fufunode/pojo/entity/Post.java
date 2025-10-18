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
@NoArgsConstructor
@AllArgsConstructor
public class Post implements Serializable {
    public static final boolean POST_ACTIVE = true; // 贴子启用
    public static final boolean POST_DISABLED = false; // 贴子禁用
    private Long id;
    private String title;
    private Long userId;
    private Long tabId;
    private String description;
    private boolean status;
    private String banReason;
    private String imgList;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
