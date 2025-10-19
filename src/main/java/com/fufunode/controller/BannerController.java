package com.fufunode.controller;

import com.fufunode.annotation.RequireAdmin;
import com.fufunode.pojo.dto.BannerDTO;
import com.fufunode.result.Result;
import com.fufunode.service.BannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/banner")
@Slf4j
public class BannerController {

    @Autowired
    private BannerService bannerService;

    // 查询所有数据
    @GetMapping("/getAll")
    public Result getAll(){
        return bannerService.getAll();
    }

    // 新增轮播图
    @PostMapping("/add")
    @RequireAdmin
    public Result add(@RequestBody BannerDTO bannerDTO){
        log.info("新增轮播图:{}",bannerDTO);
        return bannerService.add(bannerDTO);
    }

    // 删除轮播图
    @DeleteMapping("/{id}")
    @RequireAdmin
    public Result del(@PathVariable("id") Long id){
        log.info("删除轮播图:{}",id);
        return bannerService.del(id);
    }

    // 修改轮播图
    @PostMapping("/modify")
    @RequireAdmin
    public Result modify(@RequestBody BannerDTO bannerDTO){
        log.info("修改轮播图:{}",bannerDTO);
        return bannerService.modify(bannerDTO);
    }
}
