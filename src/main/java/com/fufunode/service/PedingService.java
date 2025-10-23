package com.fufunode.service;

import com.fufunode.pojo.dto.PedingTabDTO;
import com.fufunode.pojo.dto.PedingTabPageQueryDTO;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;

public interface PedingService {

    // 待审核贴吧分页查询
    PageResult page(PedingTabPageQueryDTO pedingTabPageQueryDTO);

    //新增待审核贴吧
    Result add(PedingTabDTO pedingTabDTO);
}
