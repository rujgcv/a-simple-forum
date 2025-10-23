package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.mapper.*;
import com.fufunode.pojo.dto.BannerDTO;
import com.fufunode.pojo.dto.UploadDTO;
import com.fufunode.pojo.entity.Banner;
import com.fufunode.pojo.entity.PedingTab;
import com.fufunode.pojo.entity.Tab;
import com.fufunode.pojo.entity.User;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TabMapper tabMapper;
    @Autowired
    private PedingMapper pedingMapper;
    @Autowired
    private BannerMapper bannerMapper;

    @Override
    @Transactional
    public Result uploadAvatar(UploadDTO uploadDTO) {
        String path = null;
        Long id = uploadDTO.getId();
        User user = userMapper.getUserById(id);
        String oldAvatar = user.getAvatarUrl();
        path = UploadUtil.uploadImage(uploadDTO.getFile());
        if (path == null || path.isEmpty()) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
        uploadMapper.addAvatar(id,path);

        if(oldAvatar != null && !oldAvatar.isEmpty()){
            UploadUtil.delete(oldAvatar);
        }

        return Result.success();
    }

    @Override
    @Transactional
    public Result uploadTabImg(UploadDTO uploadDTO) {
        String path = null;
        Long id = uploadDTO.getId();
        Tab tab = tabMapper.getTabById(id);
        String oldImgUrl = tab.getImgUrl();

        path = UploadUtil.uploadImage(uploadDTO.getFile());
        if (path == null || path.isEmpty()) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
        uploadMapper.addTabImg(id,path);

        if(oldImgUrl != null && !oldImgUrl.isEmpty()){
            UploadUtil.delete(oldImgUrl);
        }

        return Result.success();
    }

    @Override
    @Transactional
    public Result uploadPedingTabImg(UploadDTO uploadDTO) {
        String path = null;
        Long id = uploadDTO.getId();
        PedingTab pedingTab = pedingMapper.getById(id);
        String oldImgUrl = pedingTab.getImgUrl();

        path = UploadUtil.uploadImage(uploadDTO.getFile());
        if (path == null || path.isEmpty()) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
        uploadMapper.addPedingTabImg(id,path);

        if(oldImgUrl != null && !oldImgUrl.isEmpty()){
            UploadUtil.delete(oldImgUrl);
        }

        return Result.success();
    }

    // 上传轮播图
    @Override
    @Transactional
    public Result uploadBanner(UploadDTO uploadDTO) {
        String path = null;
        Long id = uploadDTO.getId();
        Banner banner = bannerMapper.getById(id);
        String oldImgUrl = banner.getImgUrl();
        path = UploadUtil.uploadImage(uploadDTO.getFile());
        if (path == null || path.isEmpty()) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
        uploadMapper.addBannerImg(id,path);

        if(oldImgUrl != null && !oldImgUrl.isEmpty()){
            UploadUtil.delete(oldImgUrl);
        }

        return Result.success();
    }
}
