package com.fufunode.service;

import com.fufunode.pojo.dto.UserDTO;
import com.fufunode.pojo.dto.UserPageQueryDTO;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;

public interface UserService {
    // 用户分页查询
    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);

    // 用户启用、禁用
    Result statusChang(Long id);

    // 新增用户
    Result add(UserDTO userDTO);

    // 修改用户信息
    Result modify(UserDTO userDTO);
}
