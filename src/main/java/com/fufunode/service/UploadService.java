package com.fufunode.service;

import com.fufunode.pojo.dto.UploadDTO;
import com.fufunode.result.Result;

public interface UploadService {

    // 上传用户头像
    Result uploadAvatar(UploadDTO uploadDTO);

    // 上传贴吧头像
    Result uploadTabImg(UploadDTO uploadDTO);
}
