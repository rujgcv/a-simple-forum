package com.fufunode.mapper;

import com.fufunode.pojo.dto.UploadDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UploadMapper {

    // 上传头像
    @Update("update t_user set avatar_url = #{avatarUrl} where id = #{id}")
    void addAvatar(Long id,String avatarUrl);
}
