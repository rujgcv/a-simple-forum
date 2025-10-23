package com.fufunode.mapper;

import com.fufunode.pojo.dto.PedingTabPageQueryDTO;
import com.fufunode.pojo.entity.PedingTab;
import com.fufunode.result.PageResult;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PedingMapper {

    // 新增待审核贴吧
    Long add(PedingTab pedingTab);

    // 待审核贴吧分页查询
    Page<PedingTab> pageQuery();

    // 根据id获取待审核贴吧
    @Select("select id, name, create_time, ban_reason, type_id, description, img_url, user_id," +
            "(select name from t_type where id = type_id) type " +
            "from t_tab_peding where id = #{id}")
    PedingTab getById(Long id);
}
