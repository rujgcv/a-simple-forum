package com.fufunode.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PostVO implements Serializable {
    private Long id;
    private String title;
    private String username;
    private String tabName;
    private String description;
    private boolean status;
    private String banReason;
    @JsonIgnore
    private String imgStr;
    private List<String> imgList;
    // 贴子评论数量
    private Long commentNum;
    // 贴子喜欢数量
    private Long likeNum;
    // 贴子收藏数量
    private Long favoriteNum;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
