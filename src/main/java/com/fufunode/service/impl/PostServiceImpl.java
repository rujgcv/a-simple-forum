package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.context.BaseContext;
import com.fufunode.mapper.PostMapper;
import com.fufunode.pojo.dto.PostPageQueryDTO;
import com.fufunode.pojo.dto.PostStatusDTO;
import com.fufunode.pojo.entity.Post;
import com.fufunode.pojo.vo.PostVO;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.PostService;
import com.fufunode.utils.UploadUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public PageResult pageQuery(PostPageQueryDTO postPageQueryDTO) {
        PageHelper.startPage(postPageQueryDTO.getPage(), postPageQueryDTO.getPageSize());

        Page<PostVO> page = postMapper.pageQuery(postPageQueryDTO);
        long total = page.getTotal();
        List<PostVO> result = page.getResult();

        for(PostVO p : result){
            if(p.getImgStr() == null || p.getImgStr().isEmpty()){
                p.setImgList(null);
                break;
            }
            List<String> imgList = Arrays.stream(p.getImgStr()
                                    .split(";"))
                                    .filter(s -> !s.isEmpty())
                                    .toList();
            p.setImgList(imgList);
        }

        return new PageResult(total,result);
    }

    @Override
    @Transactional
    public Result statusChange(PostStatusDTO postStatusDTO) {
        if(postStatusDTO.getStatus()){
            // 当前是启用，执行禁用
            // 1.验证禁用理由
            String banReason = postStatusDTO.getBanReason();
            if(banReason == null || banReason.isEmpty()) return Result.error(MessageConstant.BANREASON_IS_NULL);
            banReason = banReason.trim();
            if(banReason.length() > 50) return Result.error(MessageConstant.BANREASON_TOO_LONG);

            // 2.更改post状态,status->false
            postStatusDTO.setStatus(Post.POST_DISABLED);

            // 3.t_post插入禁用理由
            postMapper.statusChange(postStatusDTO);

        }else {
            // 当前是禁用，执行启用
            // 1.更改post状态,status->true
            postStatusDTO.setStatus(Post.POST_ACTIVE);
            postStatusDTO.setBanReason("");

            // 2.执行启用
            postMapper.statusChange(postStatusDTO);
        }
        return Result.success();
    }

    @Override
    @Transactional
    public Result del(Long id) {

        Long currentId = BaseContext.getCurrentId();
        String currentRole = BaseContext.getCurrentRole();
        if(!"admin".equals(currentRole) && !id.equals(currentId)){
            return Result.error(MessageConstant.NO_PERMISSION);
        }

        Post post = postMapper.getById(id);
        if(post == null){
            return Result.error(MessageConstant.DELETE_POST_IS_NULL);
        }

        List<String> imgList = new ArrayList<>();
        String imgStr = post.getImgList();
        // 删除贴子图片
        if(imgStr != null && !imgStr.isEmpty()){
            imgList = Arrays.stream(imgStr
                                        .split(";"))
                                        .filter(s -> !s.isEmpty())
                                        .toList();
        }
        // 删除贴子
        if(postMapper.del(id) == 0){
            return Result.error(MessageConstant.DELETE_POST_ERR);
        }

        if(!UploadUtil.deleteBatch(imgList)){
            log.error("删除贴子图片失败,id:{},file:{}",id,imgList);
        }

        return Result.success();
    }

    @Override
    @Transactional
    public Result delBatch(List<Long> ids) {
        if(ids == null || ids.isEmpty()) return Result.error(MessageConstant.DELETE_POST_IS_NULL);

        List<String> imgList = new ArrayList<>();
        List<String> imgStrList = postMapper.getImgs(ids);
        if(imgStrList != null && !imgStrList.isEmpty()){
            // 处理图片列表
            for(String imgStr : imgStrList){
                if(imgStr != null && !imgStr.isEmpty()){
                    List<String> imgs = Arrays.stream(imgStr
                                    .split(";"))
                                    .filter(s -> !s.isEmpty())
                                    .toList();
                    imgList.addAll(imgs);
                }
            }
        }

        // 批量删除
        if(postMapper.delBatch(ids) != ids.size()){
            return Result.error(MessageConstant.DELETE_POST_ERR);
        }

        if(!imgList.isEmpty()){
            if(!UploadUtil.deleteBatch(imgList)){
                log.warn("删除贴子图片失败,id:{},file:{}",ids,imgList);
            }
        }

        return Result.success();
    }
}
