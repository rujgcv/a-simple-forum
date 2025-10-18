package com.fufunode.service;

import com.fufunode.pojo.dto.PedingTabDTO;
import com.fufunode.result.Result;

public interface PedingService {

    //新增待审核贴吧
    Result add(PedingTabDTO pedingTabDTO);
}
