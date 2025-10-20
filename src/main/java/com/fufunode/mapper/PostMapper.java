package com.fufunode.mapper;

import com.fufunode.pojo.dto.PostPageQueryDTO;
import com.fufunode.pojo.dto.PostStatusDTO;
import com.fufunode.pojo.entity.Post;
import com.fufunode.pojo.vo.PostVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PostMapper {
    // 分页查询
    Page<PostVO> pageQuery(PostPageQueryDTO postPageQueryDTO);

    // 状态启用、禁用
    @Update("update t_post set `status` = #{status} , ban_reason = #{banReason} , update_time = CURRENT_TIMESTAMP " +
            "where id = #{id};")
    void statusChange(PostStatusDTO postStatusDTO);

    // 根据id获取贴子
    @Select("select * from t_post where id = #{id}")
    Post getById(Long id);

    // 删除贴子
    @Delete("delete from t_post where id = #{id}")
    Integer del(Long id);

    // 根据ids获取多张图片
    List<String> getImgs(List<Long> ids);

    // 批量删除贴子
    Integer delBatch(List<Long> ids);
}
