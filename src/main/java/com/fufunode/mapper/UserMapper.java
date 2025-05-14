package com.fufunode.mapper;

import com.fufunode.pojo.dto.UserLoginDTO;
import com.fufunode.pojo.dto.UserPageQueryDTO;
import com.fufunode.pojo.entity.User;
import com.fufunode.result.Result;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

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

    // 删除用户
    @Delete("delete from t_user where id = #{id}")
    void delById(Long id);

    // 批量删除用户
    void delBatch(List<Long> ids);

    // 根据name获取用户
    @Select("select * from t_user where name = #{name}")
    User getUserByName(String name);

    // 登录验证用户是否存在(name可能是昵称也可能是电话)
    @Select("select id from t_user where name = #{name} or phone = #{name}")
    User loginValid(UserLoginDTO userLoginDTO);

    // 获取用户
    User getUser(UserLoginDTO userLoginDTO);

    // 根据一组id获取头像
    List<String> getAvatars(List<Long> ids);
}
