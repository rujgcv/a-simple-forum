package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.mapper.UploadMapper;
import com.fufunode.pojo.dto.UploadDTO;
import com.fufunode.result.Result;
import com.fufunode.service.UploadService;
import com.fufunode.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UploadMapper uploadMapper;

    @Override
    @Transactional
    public Result uploadAvatar(UploadDTO uploadDTO) {
        String path = null;
        Long id = uploadDTO.getId();
        try {
            path = UploadUtil.uploadImage(uploadDTO.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (path == null || path == "") {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }else {
            uploadMapper.addAvatar(id,path);
            return Result.success();
        }
    }

    @Override
    @Transactional
    public Result uploadTabImg(UploadDTO uploadDTO) {
        String path = null;
        Long id = uploadDTO.getId();
        try {
            path = UploadUtil.uploadImage(uploadDTO.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (path == null || path == "") {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }else {
            uploadMapper.addTabImg(id,path);
            return Result.success();
        }
    }
}
