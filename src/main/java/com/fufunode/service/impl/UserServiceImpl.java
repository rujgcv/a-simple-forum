package com.fufunode.service.impl;

import com.fufunode.enums.Role;
import com.fufunode.mapper.UserMapper;
import com.fufunode.pojo.dto.UserPageQueryDTO;
import com.fufunode.pojo.entity.User;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return Result.success();
    }
}
