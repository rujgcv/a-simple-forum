package com.fufunode.controller.manage;

import com.fufunode.enums.Role;
import com.fufunode.pojo.dto.UserDTO;
import com.fufunode.pojo.dto.UserLoginDTO;
import com.fufunode.pojo.dto.UserPageQueryDTO;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    // 分页查询
    @GetMapping("/page")
    public Result<PageResult> page(UserPageQueryDTO userPageQueryDTO){
        log.info("用户分页查询:{}",userPageQueryDTO);
        userPageQueryDTO.setRole(Role.user);
        PageResult pageResult = userService.pageQuery(userPageQueryDTO);
        return Result.success(pageResult);
    }

    // 用户启用、禁用
    @PutMapping("/status/{id}")
    public Result statusChang(@PathVariable("id") Long id){
        log.info("用户启用、禁用:{}",id);
        return userService.statusChang(id);
    }

    // 新增用户
    @PostMapping("/add")
    public Result add(@RequestBody UserDTO userDTO){
        log.info("新增用户:{}",userDTO);
        return userService.add(userDTO,Role.user);
    }

    // 修改用户信息
    @PostMapping("/modify")
    public Result modify(@RequestBody UserDTO userDTO){
        log.info("修改用户信息:{}",userDTO);
        return userService.modify(userDTO);
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Result delById(@PathVariable("id") Long id){
        log.info("删除用户:{}",id);
        return userService.delById(id);
    }

    // 批量删除用户
    @PostMapping("/delBatch")
    public Result batchDeleteUser(@RequestBody List<Long> ids) {
        log.info("批量删除用户:{}",ids);
        return userService.delBatch(ids);
    }

    // 用户登录
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登录:{}",userLoginDTO);
        return userService.login(userLoginDTO);
    }
}
