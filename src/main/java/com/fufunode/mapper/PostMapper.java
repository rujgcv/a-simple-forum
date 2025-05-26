package com.fufunode.mapper;

import com.fufunode.pojo.dto.PostPageQueryDTO;
import com.fufunode.pojo.dto.PostStatusDTO;
import com.fufunode.pojo.vo.PostVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostMapper {
    // 分页查询
    Page<PostVO> pageQuery(PostPageQueryDTO postPageQueryDTO);

    // 状态启用、禁用
    @Update("update t_post set `status` = #{status} , ban_reason = #{banReason} , update_time = CURRENT_TIMESTAMP " +
            "where id = #{id};")
    void statusChange(PostStatusDTO postStatusDTO);
}
