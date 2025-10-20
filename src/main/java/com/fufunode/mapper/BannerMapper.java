package com.fufunode.mapper;

import com.fufunode.pojo.dto.BannerDTO;
import com.fufunode.pojo.entity.Banner;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BannerMapper {
    // 获取所有数据
    @Select("select * from t_banner")
    List<Banner> getAll();

    // 新增轮播图
    Integer add(BannerDTO bannerDTO);

    // 根据id查询轮播图
    @Select("select * from t_banner where id = #{id}")
    Banner getById(Long id);

    // 删除轮播图
    @Delete("delete from t_banner where id = #{id}")
    Integer del(Long id);

    // 修改轮播图
    @Update("update t_banner set link = #{link} where id = #{id}")
    void modify(BannerDTO bannerDTO);
}
