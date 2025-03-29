package com.fufunode.controller.upload;

import com.fufunode.pojo.dto.UploadDTO;
import com.fufunode.result.Result;
import com.fufunode.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class UploadController {

    @Autowired
    private UploadService uploadService;

    // 上传头像
    @PostMapping("/user/upload")
    public Result upload(@RequestParam("userId") Long userId,
                       @RequestParam("file") MultipartFile file){
        UploadDTO uploadDTO = UploadDTO.builder()
                .userId(userId)
                .file(file)
                .build();
        log.info("上传头像: {}",uploadDTO);
        return uploadService.upload(uploadDTO);
    }
}
