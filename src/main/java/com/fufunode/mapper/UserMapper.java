package com.fufunode.mapper;

import com.fufunode.pojo.dto.UserPageQueryDTO;
import com.fufunode.pojo.entity.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    // 用户分页查询
    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);

    // 根据id查询用户状态
    @Select("select `status` from t_user where id = #{id}")
    Boolean getStatusById(Long id);

    // 用户启用、禁用
    @Update("update t_user set `status` = 1 where id = #{id}")
    void statusChangActive(Long id);

    // 用户禁用
    @Update("update t_user set `status` = 0 where id = #{id}")
    void statusChangDisabled(Long id);

    // 新增用户
    Long add(User user);

    // 根据id查询用户
    @Select("select * from t_user where id = #{id}")
    User getUserById(Long id);

    // 根据电话查询用户
    User getUserByPhone(Long id,String phone);

    // 修改用户信息
    Long modify(User user);

    // 根据id更新最近修改时间
    @Update("update t_user set update_time = #{now} where id = #{id}")
    void updateTimeById(Long id, LocalDateTime now);
}
