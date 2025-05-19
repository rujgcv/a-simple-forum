package com.fufunode.service;

import com.fufunode.pojo.dto.BannerDTO;
import com.fufunode.result.Result;

public interface BannerService {
    // 获取所有轮播图
    Result getAll();

    // 新增轮播图
    Result add(BannerDTO bannerDTO);

    // 删除轮播图
    Result del(Long id);

    // 修改轮播图
    Result modify(BannerDTO bannerDTO);
}
