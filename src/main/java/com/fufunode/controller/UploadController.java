package com.fufunode.controller;

import com.fufunode.annotation.RequireAdmin;
import com.fufunode.pojo.dto.UploadDTO;
import com.fufunode.result.Result;
import com.fufunode.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    // 上传用户头像
    @PostMapping("/userAvatar")
    public Result uploadAvatar(@RequestParam("userId") Long userId,
                       @RequestParam("file") MultipartFile file){
        UploadDTO uploadDTO = UploadDTO.builder()
                .id(userId)
                .file(file)
                .build();
        log.info("上传用户头像: {}",uploadDTO);
        return uploadService.uploadAvatar(uploadDTO);
    }

    // 上传贴吧头像
    @PostMapping("/tabImg")
    public Result uploadTabImg(@RequestParam("tabId") Long tabId,
                           @RequestParam("file") MultipartFile file) {
        UploadDTO uploadDTO = UploadDTO.builder()
                .id(tabId)
                .file(file)
                .build();
        log.info("上传贴吧头像: {}", uploadDTO);
        return uploadService.uploadTabImg(uploadDTO);
    }

    // 上传待审核贴吧头像
    @PostMapping("/pedingTabImg")
    public Result uploadPedingTabImg(@RequestParam("tabId") Long tabId,
                               @RequestParam("file") MultipartFile file) {
        UploadDTO uploadDTO = UploadDTO.builder()
                .id(tabId)
                .file(file)
                .build();
        log.info("上传待审核贴吧头像: {}", uploadDTO);
        return uploadService.uploadPedingTabImg(uploadDTO);
    }

    // 上传轮播图
    @PostMapping("/banner")
    @RequireAdmin
    public Result uploadBanner(@RequestParam("id") Long bannerId,
                               @RequestParam("file") MultipartFile file){
        UploadDTO uploadDTO = UploadDTO.builder()
                .id(bannerId)
                .file(file)
                .build();
        log.info("上传轮播图:{}",uploadDTO);
        return uploadService.uploadBanner(uploadDTO);
    }
}
