package com.fufunode.pojo.dto;

import lombok.Data;

// 状态启用、禁用
@Data
public class PostStatusDTO {
    private Long id;
    private Boolean status;
    private String banReason;
}
