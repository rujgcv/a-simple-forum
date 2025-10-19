package com.fufunode.controller;

import com.fufunode.annotation.RequireAdmin;
import com.fufunode.pojo.dto.PostPageQueryDTO;
import com.fufunode.pojo.dto.PostStatusDTO;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    // 分页查询
    @GetMapping("/page")
    public Result<PageResult> page(PostPageQueryDTO postPageQueryDTO){
        log.info("分页查询:{}",postPageQueryDTO);
        PageResult pageResult = postService.pageQuery(postPageQueryDTO);
        return Result.success(pageResult);
    }

    // 状态启用、禁用
    @PutMapping("/status")
    @RequireAdmin
    public Result statusChange(@RequestBody PostStatusDTO postStatusDTO){
        log.info("状态启用、禁用:{}",postStatusDTO);
        return postService.statusChange(postStatusDTO);
    }

    // 删除贴子
    @DeleteMapping("/{id}")
    public Result del(@PathVariable("id") Long id){
        log.info("删除贴子:{}",id);
        return postService.del(id);
    }

    // 批量删除贴子
    @PostMapping("/delBatch")
    @RequireAdmin
    public Result delBatch(@RequestBody List<Long> ids){
        log.info("批量删除贴子:{}",ids);
        return postService.delBatch(ids);
    }
}
