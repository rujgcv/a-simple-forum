package com.fufunode.mapper;

import com.fufunode.pojo.dto.UploadDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UploadMapper {

    // 上传头像
    @Update("update t_user set avatar_url = #{avatarUrl} where id = #{id}")
    void addAvatar(Long id,String avatarUrl);

    @Update("update t_tab set img_url = #{imgUrl} where id = #{id}")
    void addTabImg(Long id, String imgUrl);

    @Update("update t_banner set img_url = #{imgUrl} where id = #{id}")
    void addBannerImg(Long id, String imgUrl);

    @Update("update t_tab_peding set img_url = #{imgUrl} where id = #{id}")
    void addPedingTabImg(Long id, String imgUrl);
}
