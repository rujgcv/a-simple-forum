package com.fufunode.controller;

import com.fufunode.annotation.RequireAdmin;
import com.fufunode.enums.Role;
import com.fufunode.pojo.dto.UserDTO;
import com.fufunode.pojo.dto.UserPageQueryDTO;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;

    // 分页查询
    @GetMapping("/page")
    @RequireAdmin
    public Result<PageResult> page(UserPageQueryDTO userPageQueryDTO){
        log.info("用户分页查询:{}",userPageQueryDTO);
        userPageQueryDTO.setRole(Role.admin);
        PageResult pageResult = userService.pageQuery(userPageQueryDTO);
        return Result.success(pageResult);
    }

    // 新增管理员
    @PostMapping("/add")
    @RequireAdmin
    public Result add(@RequestBody UserDTO userDTO){
        log.info("新增用户:{}",userDTO);
        return userService.add(userDTO,Role.admin);
    }
}
