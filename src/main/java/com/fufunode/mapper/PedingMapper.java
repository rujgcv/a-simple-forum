package com.fufunode.mapper;

import com.fufunode.pojo.entity.PedingTab;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PedingMapper {

    // 新增待审核贴吧
    void add(PedingTab pedingTab);
}
