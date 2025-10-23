package com.fufunode.controller;

import com.fufunode.annotation.RequireAdmin;
import com.fufunode.pojo.dto.PedingTabDTO;
import com.fufunode.pojo.dto.PedingTabPageQueryDTO;
import com.fufunode.pojo.entity.PedingTab;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.PedingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/peding")
public class PedingController {

    @Autowired
    private PedingService pedingService;

    // 待审核贴吧分页查询
    @GetMapping("/page")
    @RequireAdmin
    public Result<PageResult> page(PedingTabPageQueryDTO pedingTabPageQueryDTO){
        log.info("待审核贴吧分页查询:{}",pedingTabPageQueryDTO);
        PageResult pageResult = pedingService.page(pedingTabPageQueryDTO);
        return Result.success(pageResult);
    }

    //新增待审核贴吧
    @PostMapping("/add")
    public Result add(@RequestBody PedingTabDTO pedingTabDTO){
        log.info("新增待审核贴吧:{}",pedingTabDTO);
        return pedingService.add(pedingTabDTO);
    }
}
