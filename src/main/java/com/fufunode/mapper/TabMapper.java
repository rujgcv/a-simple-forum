package com.fufunode.mapper;

import com.fufunode.pojo.dto.TabPageQueryDTO;
import com.fufunode.pojo.entity.Tab;
import com.fufunode.pojo.entity.TabDetail;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TabMapper {

    // 分页查询
    Page<TabDetail> pageQuery(TabPageQueryDTO tabPageQueryDTO);

    // 根据id查询贴吧状态
    @Select("select `status` from t_tab where id = #{id}")
    Boolean getStatusById(Long id);

    // 贴吧启用
    @Update("update t_tab set `status` = 1 where id = #{id}")
    void statusChangActive(Long id);

    // 贴吧禁用
    @Update("update t_tab set `status` = 0 where id = #{id}")
    void statusChangDisabled(Long id);

    // 根据id更新最近修改时间
    @Update("update t_tab set update_time = #{now} where id = #{id}")
    void updateTimeById(Long id, LocalDateTime now);

    // 根据name获取贴吧
    @Select("select * from t_tab where name = #{name}")
    Tab getTabByName(String name);

    // 根据id获取贴吧
    @Select("select * from t_tab where id = #{id}")
    Tab getTabById(Long id);

    // 新增贴吧
    Long add(Tab tab);

    // 修改贴吧
    Long modify(Tab tab);

    // 根据id删除贴吧
    @Delete("delete from t_tab where id = #{id}")
    Integer delById(Long id);

    // 根据一组id获取图片路径
    List<String> getImgs(List<Long> ids);

    // 批量删除贴吧
    Integer delBatch(List<Long> ids);

    // 模糊查询贴吧名称
    @Select("select name from t_tab where name like concat('%',#{tabName},'%') limit 7")
    List<String> queryTabName(String tabName);
}
