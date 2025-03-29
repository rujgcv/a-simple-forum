package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.constant.PasswordConstant;
import com.fufunode.enums.Role;
import com.fufunode.mapper.UserMapper;
import com.fufunode.pojo.dto.UserDTO;
import com.fufunode.pojo.dto.UserPageQueryDTO;
import com.fufunode.pojo.entity.User;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        userPageQueryDTO.setRole(Role.user);
        PageHelper.startPage(userPageQueryDTO.getPage(),userPageQueryDTO.getPageSize());

        Page<User> page = userMapper.pageQuery(userPageQueryDTO);
        long total = page.getTotal();
        List<User> result = page.getResult();

        return new PageResult(total,result);
    }

    @Override
    @Transactional
    public Result statusChang(Long id) {
        Boolean status = userMapper.getStatusById(id);
        if(status){
            userMapper.statusChangDisabled(id);
        }else {
            userMapper.statusChangActive(id);
        }
        userMapper.updateTimeById(id,LocalDateTime.now());
        return Result.success();
    }

    @Override
    @Transactional
    public Result add(UserDTO userDTO) {

        // 电话不能重复
        if(userDTO.getPhone() != null && userDTO.getPhone() != ""){
            String phone = userDTO.getPhone().trim();
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                return Result.error(MessageConstant.PHONE_INVALID);
            }
            if (userMapper.getUserByPhone(null,phone) != null) {
                return Result.error(MessageConstant.ACCOUNT_EXISTS);
            }
        }

        // admin端新增用户默认密码
        if(userDTO.getPassword() == "" || userDTO.getPassword() == null){
            userDTO.setPassword(PasswordConstant.DEFAULT_PASSWORD);
        }

        // name不为空
        if(userDTO.getName() == null || userDTO.getName() == "") return Result.error(MessageConstant.USERNAME_IS_NULL);
        // 密码不为空
        if(userDTO.getPassword() == null || userDTO.getPassword() == "") return Result.error(MessageConstant.PASSWORD_IS_NULL);

        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        user.setRole(Role.user);

        userMapper.add(user);

        return Result.success(user.getId());
    }

    @Override
    @Transactional
    public Result modify(UserDTO userDTO) {
        // name不为空
        if(userDTO.getName() == null || userDTO.getName() == "") return Result.error(MessageConstant.USERNAME_IS_NULL);

        // 电话不能重复
        if(userDTO.getPhone() != null && userDTO.getPhone() != ""){
            String phone = userDTO.getPhone().trim();
            Long id = userDTO.getId();
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                return Result.error(MessageConstant.PHONE_INVALID);
            }
            if (userMapper.getUserByPhone(id,phone) != null) {
                return Result.error(MessageConstant.ACCOUNT_EXISTS);
            }
        }

        User user = User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .phone(userDTO.getPhone())
                .updateTime(LocalDateTime.now())
                .build();

        userMapper.modify(user);
        return Result.success(user.getId());
    }
}
