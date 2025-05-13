package com.fufunode.service;

import com.fufunode.pojo.dto.TabDTO;
import com.fufunode.pojo.dto.TabPageQueryDTO;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;

public interface TabService {

    // 分页查询
    PageResult pageQuery(TabPageQueryDTO tabPageQueryDTO);

    // 贴吧禁用、启用
    Result statusChange(Long id);

    // 新增贴吧(system)
    Result addTabFromSystem(TabDTO tabDTO);

    // 修改贴吧
    Result modify(TabDTO tabDTO);
}
