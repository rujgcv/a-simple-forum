package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.context.BaseContext;
import com.fufunode.mapper.PedingMapper;
import com.fufunode.mapper.TabMapper;
import com.fufunode.pojo.dto.PedingTabDTO;
import com.fufunode.pojo.dto.PedingTabPageQueryDTO;
import com.fufunode.pojo.entity.PedingTab;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.PedingService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedingServiceImpl implements PedingService {

    @Autowired
    private PedingMapper pedingMapper;
    @Autowired
    private TabMapper tabMapper;

    @Override
    public PageResult page(PedingTabPageQueryDTO pedingTabPageQueryDTO) {
        PageHelper.startPage(pedingTabPageQueryDTO.getPage(),pedingTabPageQueryDTO.getPageSize());

        Page<PedingTab> page = pedingMapper.pageQuery();
        long total = page.getTotal();
        List<PedingTab> cords = page.getResult();
        return new PageResult(total,cords);
    }

    @Override
    @Transactional
    public Result add(PedingTabDTO pedingTabDTO) {
        if(pedingTabDTO.getName() == "" || pedingTabDTO.getName() == null) return Result.error(MessageConstant.TAB_NAME_IS_NULL);
        if(pedingTabDTO.getTypeId() == 0) return Result.error(MessageConstant.TAB_TYPE_IS_NULL);
        if(pedingTabDTO.getName().length() > 20) return Result.error(MessageConstant.TAB_NAME_TOO_LONG);

        if(tabMapper.getTabByName(pedingTabDTO.getName()) != null) return Result.error(MessageConstant.TAB_OCCUPIED);
        if(tabMapper.verifyType(pedingTabDTO.getTypeId()) == null) return Result.error(MessageConstant.TAB_TYPE_ERR);

        PedingTab pedingTab = new PedingTab();
        BeanUtils.copyProperties(pedingTabDTO,pedingTab);
        pedingTab.setUserId(BaseContext.getCurrentId());

        pedingMapper.add(pedingTab);

        return Result.success(pedingTab.getId());
    }
}
