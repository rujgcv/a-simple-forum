package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.context.BaseContext;
import com.fufunode.mapper.TabMapper;
import com.fufunode.mapper.UserMapper;
import com.fufunode.pojo.dto.TabDTO;
import com.fufunode.pojo.dto.TabPageQueryDTO;
import com.fufunode.pojo.entity.QueryTabName;
import com.fufunode.pojo.entity.Tab;
import com.fufunode.pojo.entity.TabDetail;
import com.fufunode.pojo.entity.User;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.TabService;
import com.fufunode.utils.UploadUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TabServiceImpl implements TabService {

    @Autowired
    private TabMapper tabMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult pageQuery(TabPageQueryDTO tabPageQueryDTO) {
        PageHelper.startPage(tabPageQueryDTO.getPage(),tabPageQueryDTO.getPageSize());

        // typeId==0 寻找全部类别
        if(tabPageQueryDTO.getTypeId() == 0) tabPageQueryDTO.setTypeId(null);

        if(!"admin".equals(BaseContext.getCurrentRole())){
            // 用户端只能查看启用状态的贴子
            tabPageQueryDTO.setStatus(true);
        }

        Page<TabDetail> page = tabMapper.pageQuery(tabPageQueryDTO);

        if(!"admin".equals(BaseContext.getCurrentRole())){
            // 用户端清空审批信息
            page.getResult().forEach(tab -> {
                tab.setApproveId(null);
                tab.setApproveName(null);
            });
        }

        long total = page.getTotal();
        List<TabDetail> result = page.getResult();

        return new PageResult(total,result);
    }

    @Override
    @Transactional
    public Result statusChange(Long id) {
        Boolean status = tabMapper.getStatusById(id);
        if(status){
            tabMapper.statusChangDisabled(id);
        }else {
            tabMapper.statusChangActive(id);
        }
        tabMapper.updateTimeById(id, LocalDateTime.now());
        return Result.success();
    }

    @Override
    @Transactional
    public Result addTabFromSystem(TabDTO tabDTO) {
        if(StringUtils.isBlank(tabDTO.getName())) return Result.error(MessageConstant.TAB_NAME_IS_NULL);
        if(tabDTO.getTypeId() == null || tabDTO.getTypeId() <= 0) return Result.error(MessageConstant.TAB_TYPE_IS_NULL);
        if(tabDTO.getName().length() > 20) return Result.error(MessageConstant.TAB_NAME_TOO_LONG);

        if(tabMapper.getTabByName(tabDTO.getName()) != null) return Result.error(MessageConstant.TAB_OCCUPIED);
        if(tabMapper.verifyType(tabDTO.getTypeId()) == null) return Result.error(MessageConstant.TAB_TYPE_ERR);

        Tab tab = new Tab();
        BeanUtils.copyProperties(tabDTO,tab);
        tab.setStatus(true);

        //填入审批人id,name
        Long currentId = BaseContext.getCurrentId();
        String name = userMapper.getUserById(currentId).getName();
        tab.setApproveId(currentId);
        tab.setApproveName(name);

        tabMapper.add(tab);

        return Result.success(tab.getId());
    }

    @Override
    @Transactional
    public Result modify(TabDTO tabDTO) {
        if(StringUtils.isBlank(tabDTO.getName())) return Result.error(MessageConstant.TAB_NAME_IS_NULL);
        if(tabDTO.getTypeId() == null || tabDTO.getTypeId() <= 0) return Result.error(MessageConstant.TAB_TYPE_IS_NULL);
        if(tabDTO.getName().length() > 20) return Result.error(MessageConstant.TAB_NAME_TOO_LONG);

        Tab currentTab = tabMapper.getTabById(tabDTO.getId());
        if(currentTab == null) return Result.error(MessageConstant.TAB_NON_EXIST);

        // 昵称不能重复
        Tab tabByName = tabMapper.getTabByName(tabDTO.getName());
        if(tabByName != null && !tabByName.getId().equals(tabDTO.getId())) return Result.error(MessageConstant.TAB_OCCUPIED);

        Tab tab = Tab.builder()
                .id(tabDTO.getId())
                .typeId(tabDTO.getTypeId())
                .name(tabDTO.getName())
                .description(tabDTO.getDescription())
                .updateTime(LocalDateTime.now())
                .approveId(BaseContext.getCurrentId())
                .approveName(userMapper.getUserById(BaseContext.getCurrentId()).getName())
                .build();

        tabMapper.modify(tab);

        return Result.success(tab.getId());
    }

    @Override
    public Result del(Long id) {
        Tab t = tabMapper.getTabById(id);
        if (t == null) {
            return Result.error(MessageConstant.DELETE_TAB_IS_NULL);
        }

        if (tabMapper.delById(id) == 0) {
            return Result.error(MessageConstant.DELETE_TAB_ERR);
        }

        String imgUrl = t.getImgUrl();
        if(imgUrl != null && !imgUrl.isEmpty()){
            if(!UploadUtil.delete(imgUrl)){
                log.error("删除贴吧图片失败,id:{},file:{}",id,imgUrl);
            }
        }

        return Result.success();
    }

    @Override
    public Result delBatch(List<Long> ids) {
        if(ids == null || ids.isEmpty()) return Result.error(MessageConstant.DELETE_TAB_IS_NULL);

        List<String> imgList = new ArrayList<>();
        List<String> imgs = tabMapper.getImgs(ids);
        if(imgs != null){
            for(String img : imgs){
                if(img != null && !img.isEmpty()){
                    imgList.add(img);
                }
            }
        }

        if(tabMapper.delBatch(ids) != ids.size()) return Result.error(MessageConstant.DELETE_TAB_ERR);

        if (!imgList.isEmpty()) {
            if (!UploadUtil.deleteBatch(imgList)) {
                log.warn("删除贴吧图片失败, ids: {}, files: {}", ids, imgList);  // 改为warn
            }
        }

        return Result.success();
    }

    @Override
    public Result queryTabName(String tabName) {
        List<String> names = tabMapper.queryTabName(tabName);

        List<QueryTabName> data = names.stream()
                                    .map(QueryTabName::new)
                                    .collect(Collectors.toList());
        return Result.success(data);
    }

    @Override
    public Result checkName(String tabName) {
        Tab tab = tabMapper.getTabByName(tabName);
        if(tab != null) return Result.error(MessageConstant.TAB_OCCUPIED);
        return Result.success();
    }
}
