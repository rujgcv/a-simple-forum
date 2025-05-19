package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.mapper.BannerMapper;
import com.fufunode.pojo.dto.BannerDTO;
import com.fufunode.pojo.entity.Banner;
import com.fufunode.result.Result;
import com.fufunode.service.BannerService;
import com.fufunode.service.UploadService;
import com.fufunode.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private UploadService uploadService;

    @Override
    public Result getAll() {
        List<Banner> banners = bannerMapper.getAll();
        return Result.success(banners);
    }

    @Override
    @Transactional
    public Result add(BannerDTO bannerDTO) {
        if(bannerDTO.getLink().isEmpty()) return Result.error(MessageConstant.BANNER_Link_IS_NULL);

        bannerMapper.add(bannerDTO);

        return Result.success(bannerDTO.getId());
    }

    @Override
    @Transactional
    public Result del(Long id) {
        Banner banner = bannerMapper.getById(id);
        String imgUrl = banner.getImgUrl();
        if(imgUrl != null && !imgUrl.isEmpty()){
            if(!UploadUtil.delete(imgUrl)){
                return Result.error(MessageConstant.IMG_DELETE_ERROR);
            }
        }
        bannerMapper.del(id);
        return Result.success();
    }

    @Override
    public Result modify(BannerDTO bannerDTO) {
        if(bannerDTO.getLink().isEmpty()) return Result.error(MessageConstant.BANNER_Link_IS_NULL);

        bannerMapper.modify(bannerDTO);

        return Result.success();
    }
}
