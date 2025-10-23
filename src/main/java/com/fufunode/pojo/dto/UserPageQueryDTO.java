package com.fufunode.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fufunode.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class UserPageQueryDTO implements Serializable {
    //用户昵称
    private String name;

    //电话
    private String phone;

    //要查找的用户权限
    private Role role;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;
}
