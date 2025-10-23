package com.fufunode.pojo.dto;

import com.fufunode.enums.Role;
import lombok.Data;

import java.io.Serializable;

@Data
public class TabPageQueryDTO implements Serializable {
    //贴吧标题
    private String name;

    //简介
    private String description;

    //类别id
    private Long typeId;
    //贴子状态
    private boolean status;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;
}
