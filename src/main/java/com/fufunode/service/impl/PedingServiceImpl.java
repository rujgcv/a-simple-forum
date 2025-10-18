package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.context.BaseContext;
import com.fufunode.mapper.PedingMapper;
import com.fufunode.mapper.TabMapper;
import com.fufunode.pojo.dto.PedingTabDTO;
import com.fufunode.pojo.entity.PedingTab;
import com.fufunode.result.Result;
import com.fufunode.service.PedingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedingServiceImpl implements PedingService {

    @Autowired
    private PedingMapper pedingMapper;
    @Autowired
    private TabMapper tabMapper;

    @Override
    @Transactional
    public Result add(PedingTabDTO pedingTabDTO) {
        if(pedingTabDTO.getName() == "" || pedingTabDTO.getName() == null) return Result.error(MessageConstant.TAB_NAME_IS_NULL);
        if(pedingTabDTO.getTypeId() == 0) return Result.error(MessageConstant.TAB_TYPE_IS_NULL);
        if(pedingTabDTO.getName().length() > 20) return Result.error(MessageConstant.TAB_NAME_TOO_LONG);

        if(tabMapper.getTabByName(pedingTabDTO.getName()) != null) return Result.error(MessageConstant.TAB_OCCUPIED);

        PedingTab pedingTab = new PedingTab();
        BeanUtils.copyProperties(pedingTabDTO,pedingTab);
        pedingTab.setUserId(BaseContext.getCurrentId());

        pedingMapper.add(pedingTab);

        return Result.success();
    }
}
