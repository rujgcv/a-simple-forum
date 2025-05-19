package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.mapper.TabMapper;
import com.fufunode.pojo.dto.TabDTO;
import com.fufunode.pojo.dto.TabPageQueryDTO;
import com.fufunode.pojo.entity.Tab;
import com.fufunode.pojo.entity.TabDetail;
import com.fufunode.pojo.entity.User;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.TabService;
import com.fufunode.utils.UploadUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TabServiceImpl implements TabService {

    @Autowired
    private TabMapper tabMapper;

    @Override
    public PageResult pageQuery(TabPageQueryDTO tabPageQueryDTO) {
        PageHelper.startPage(tabPageQueryDTO.getPage(),tabPageQueryDTO.getPageSize());

        // typeId==0 寻找全部类别
        if(tabPageQueryDTO.getTypeId() == 0) tabPageQueryDTO.setTypeId(null);

        Page<TabDetail> page = tabMapper.pageQuery(tabPageQueryDTO);
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
        if(tabDTO.getName() == "" || tabDTO.getName() == null) return Result.error(MessageConstant.TAB_NAME_IS_NULL);
        if(tabDTO.getTypeId() == 0) return Result.error(MessageConstant.TAB_TYPE_IS_NULL);
        if(tabDTO.getName().length() > 20) return Result.error(MessageConstant.TAB_NAME_TOO_LONG);

        if(tabMapper.getTabByName(tabDTO.getName()) != null) return Result.error(MessageConstant.TAB_OCCUPIED);

        Tab tab = new Tab();
        BeanUtils.copyProperties(tabDTO,tab);
        tab.setStatus(true);

        tabMapper.add(tab);

        return Result.success(tab.getId());
    }

    @Override
    @Transactional
    public Result modify(TabDTO tabDTO) {
        if(tabDTO.getName() == "" || tabDTO.getName() == null) return Result.error(MessageConstant.TAB_NAME_IS_NULL);
        if(tabDTO.getTypeId() == 0) return Result.error(MessageConstant.TAB_TYPE_IS_NULL);
        if(tabDTO.getName().length() > 20) return Result.error(MessageConstant.TAB_NAME_TOO_LONG);

        // 昵称不能重复
        Tab tabByName = tabMapper.getTabByName(tabDTO.getName());
        if(tabByName != null && !tabByName.getName().equals(tabMapper.getTabById(tabDTO.getId()).getName())) return Result.error(MessageConstant.TAB_OCCUPIED);

        // 如果修改图片，先删除之前的图片

        Tab tab = Tab.builder()
                .id(tabDTO.getId())
                .typeId(tabDTO.getTypeId())
                .name(tabDTO.getName())
                .description(tabDTO.getDescription())
                .updateTime(LocalDateTime.now())
                .build();

        tabMapper.modify(tab);

        return Result.success(tab.getId());
    }

    @Override
    public Result del(Long id) {
        Tab t = tabMapper.getTabById(id);
        String imgUrl = t.getImgUrl();
        if(imgUrl != null && !imgUrl.isEmpty()){
            if(!UploadUtil.delete(imgUrl)){
                return Result.error(MessageConstant.IMG_DELETE_ERROR);
            }
        }
        tabMapper.delById(id);
        return Result.success();
    }

    @Override
    public Result delBatch(List<Long> ids) {
        if(ids.size() == 0) return Result.error(MessageConstant.DELETE_TAB_IS_NULL);

        List<String> imgUrl = new ArrayList<>();
        List<String> imgs = tabMapper.getImgs(ids);
        for(String img : imgs){
            if(img != null && !img.isEmpty()){
                imgUrl.add(img);
            }
        }
        if(!imgUrl.isEmpty()) UploadUtil.deleteBatch(imgUrl);
        tabMapper.delBatch(ids);
        return Result.success();
    }
}
