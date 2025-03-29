package com.fufunode.service;

import com.fufunode.pojo.dto.UploadDTO;
import com.fufunode.result.Result;

public interface UploadService {

    // 上传头像
    Result upload(UploadDTO uploadDTO);
}
