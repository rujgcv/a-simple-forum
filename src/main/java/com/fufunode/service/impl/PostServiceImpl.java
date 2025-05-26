package com.fufunode.service.impl;

import com.fufunode.constant.MessageConstant;
import com.fufunode.mapper.PostMapper;
import com.fufunode.pojo.dto.PostPageQueryDTO;
import com.fufunode.pojo.dto.PostStatusDTO;
import com.fufunode.pojo.entity.Post;
import com.fufunode.pojo.vo.PostVO;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;
import com.fufunode.service.PostService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.List;

@Service
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
            List<String> imgList = Arrays.stream(p.getImgStr().split(";")).toList();
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
}
