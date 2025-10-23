package com.fufunode.pojo.dto;

import lombok.Data;

@Data
public class PedingTabPageQueryDTO {
    //页码
    private int page;
    //每页显示记录数
    private int pageSize;
}
