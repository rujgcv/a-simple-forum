package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.constant.PasswordConstant;
import com.fufunode.context.BaseContext;
import com.fufunode.enums.Role;
import com.fufunode.mapper.UserMapper;
import com.fufunode.pojo.dto.UserDTO;
import com.fufunode.pojo.dto.UserLoginDTO;
import com.fufunode.pojo.dto.UserPageQueryDTO;
import com.fufunode.pojo.entity.User;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.UserService;
import com.fufunode.utils.JwtUtil;
import com.fufunode.utils.MD5Util;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
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
    public Result add(UserDTO userDTO,Role role) {

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
        // 昵称不得超过10字
        if(userDTO.getName().length() >= 10) return Result.error(MessageConstant.NAME_TOO_LONG);
        // 密码不为空
        if(userDTO.getPassword() == null || userDTO.getPassword() == "") return Result.error(MessageConstant.PASSWORD_IS_NULL);
        // 密码长度规定
        if(userDTO.getPassword().length()>20 || userDTO.getPassword().length()<6) return Result.error(MessageConstant.PASSWORD_INVAILD);

        // 用户已存在(name重复)
        if(userMapper.getUserByName(userDTO.getName()) != null) return Result.error(MessageConstant.Name_OCCUPIED);

        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        user.setRole(role);
        // 加密密码
        user.setPassword(MD5Util.md5(user.getPassword()));

        userMapper.add(user);

        return Result.success(user.getId());
    }

    @Override
    @Transactional
    public Result modify(UserDTO userDTO) {
        // name不为空
        if(userDTO.getName() == null || userDTO.getName() == "") return Result.error(MessageConstant.USERNAME_IS_NULL);

        // 昵称不得超过10字
        if(userDTO.getName().length() >= 10) return Result.error(MessageConstant.NAME_TOO_LONG);

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

        User userByName = userMapper.getUserByName(userDTO.getName());
        // 用户已存在(name重复)
        if(userByName != null && !userByName.getName().equals(userMapper.getUserById(userDTO.getId()).getName())) return Result.error(MessageConstant.Name_OCCUPIED);

        User user = User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .phone(userDTO.getPhone())
                .updateTime(LocalDateTime.now())
                .build();

        userMapper.modify(user);
        return Result.success(user.getId());
    }

    @Override
    @Transactional
    public Result delById(Long id) {
        userMapper.delById(id);
        return Result.success();
    }

    @Override
    @Transactional
    public Result delBatch(List<Long> ids) {
        if(ids.size() == 0) return Result.error(MessageConstant.DELETE_USER_IS_NULL);
        userMapper.delBatch(ids);
        return Result.success();
    }

    @Override
    public Result login(UserLoginDTO userLoginDTO) {
        // 校验
        if (StringUtils.isBlank(userLoginDTO.getName())) {
            return Result.error(MessageConstant.USERNAME_IS_NULL);
        }
        if (StringUtils.isBlank(userLoginDTO.getPassword())) {
            return Result.error(MessageConstant.PASSWORD_IS_NULL);
        }
        if (userLoginDTO.getRole() == null) {
            return Result.error(MessageConstant.Role_INVALID);
        }

        userLoginDTO.setPassword(MD5Util.md5(userLoginDTO.getPassword()));
        // 查询用户是否存在
        if(userMapper.loginValid(userLoginDTO) == null){
            return Result.error(MessageConstant.ACCOUNT_NOT_EXISTS);
        }

        if(userLoginDTO.getName().length() == 11){
            userLoginDTO.setPhone(userLoginDTO.getName());
            userLoginDTO.setName(null);
        }

        // 查询用户
        User user = userMapper.getUser(userLoginDTO);
        if(user == null){
            return Result.error(MessageConstant.LOGIN_FAILED);
        }
        // 查询用户状态
        if(!user.isStatus()) {
            return Result.error(MessageConstant.USER_DISABLE);
        }

        // 存储id到线程
        BaseContext.setCurrentId(user.getId());

        // 生成Token
        String token = JwtUtil.getToken(user.getId(),user.getName(), user.getRole().name());

        // 返回Token和用户信息
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        return Result.success(data);
    }

    @Override
    public Result getUserInfo() {
        Long id = BaseContext.getCurrentId();
        if(id == null) return Result.error(MessageConstant.ACCOUNT_NOT_EXISTS);
        User user = userMapper.getUserById(id);
        return Result.success(user);
    }
}
