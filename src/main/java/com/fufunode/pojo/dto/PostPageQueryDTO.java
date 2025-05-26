package com.fufunode.pojo.dto;

import lombok.Data;

@Data
public class PostPageQueryDTO {
    // 贴子标题
    private String title;
    // 发贴人
    private String username;
    // 贴吧名称
    private String tabName;
    // 内容描述
    private String description;
    //页码
    private int page;
    //每页显示记录数
    private int pageSize;
}
