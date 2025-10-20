package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.mapper.BannerMapper;
import com.fufunode.pojo.dto.BannerDTO;
import com.fufunode.pojo.entity.Banner;
import com.fufunode.result.Result;
import com.fufunode.service.BannerService;
import com.fufunode.service.UploadService;
import com.fufunode.utils.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
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
        if(banner == null){
            return Result.error(MessageConstant.BANNER_IS_NULL);
        }

        if(bannerMapper.del(id) == 0){
            return Result.error(MessageConstant.DELETE_BANNER_ERR);
        }

        String imgUrl = banner.getImgUrl();
        if(imgUrl != null && !imgUrl.isEmpty()){
            if(!UploadUtil.delete(imgUrl)){
                log.error("删除轮播图图片失败,id:{},file:{}",id,imgUrl);
            }
        }

        return Result.success();
    }

    @Override
    public Result modify(BannerDTO bannerDTO) {
        if(bannerDTO.getLink().isEmpty()) return Result.error(MessageConstant.BANNER_Link_IS_NULL);

        bannerMapper.modify(bannerDTO);

        return Result.success();
    }
}
