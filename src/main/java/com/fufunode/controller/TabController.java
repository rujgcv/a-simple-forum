package com.fufunode.controller;

import com.fufunode.annotation.RequireAdmin;
import com.fufunode.enums.Role;
import com.fufunode.pojo.dto.TabDTO;
import com.fufunode.pojo.dto.TabPageQueryDTO;
import com.fufunode.pojo.dto.UserPageQueryDTO;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.TabService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tab")
@Slf4j
public class TabController {

    @Autowired
    private TabService tabService;

    // 分页查询
    @GetMapping("/page")
    public Result<PageResult> page(TabPageQueryDTO tabPageQueryDTO){
        log.info("贴吧分页查询:{}",tabPageQueryDTO);
        PageResult pageResult = tabService.pageQuery(tabPageQueryDTO);
        return Result.success(pageResult);
    }

    // 贴吧禁用、启用
    @PutMapping("/status/{id}")
    @RequireAdmin
    public Result statusChang(@PathVariable("id") Long id){
        log.info("贴吧启用、禁用:{}",id);
        return tabService.statusChange(id);
    }

    // 新增贴吧(system)
    @PostMapping("/add/system")
    @RequireAdmin
    public Result addFromSysem(@RequestBody TabDTO tabDTO){
        log.info("新增贴吧(system):{}",tabDTO);
        return tabService.addTabFromSystem(tabDTO);
    }

    // 修改贴吧
    @PostMapping("/modify")
    @RequireAdmin
    public Result modify(@RequestBody TabDTO tabDTO){
        log.info("修改贴吧:{}",tabDTO);
        return tabService.modify(tabDTO);
    }

    // 删除贴吧
    @DeleteMapping("/{id}")
    @RequireAdmin
    public Result del(@PathVariable("id") Long id){
        log.info("删除贴吧:{}",id);
        return tabService.del(id);
    }

    // 批量删除贴吧
    @PostMapping("/delBatch")
    @RequireAdmin
    public Result delBatch(@RequestBody List<Long> ids){
        log.info("批量删除贴吧:{}",ids);
        return tabService.delBatch(ids);
    }

    // 远程搜索贴吧名
    @GetMapping("/queryTabName")
    public Result queryTabName(@RequestParam String tabName){
        log.info("远程搜索贴吧名:{}",tabName);
        return tabService.queryTabName(tabName);
    }

    // 贴吧名查重
    @GetMapping("/checkname")
    public Result checkName(@RequestParam String tabName){
        log.info("贴吧名查重:{}",tabName);
        return tabService.checkName(tabName);
    }
}
