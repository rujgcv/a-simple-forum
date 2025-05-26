package com.fufunode.service;

import com.fufunode.pojo.dto.PostPageQueryDTO;
import com.fufunode.pojo.dto.PostStatusDTO;
import com.fufunode.result.PageResult;
import com.fufunode.result.Result;

public interface PostService {
    // 分页查询
    PageResult pageQuery(PostPageQueryDTO postPageQueryDTO);

    // 状态启用、禁用
    Result statusChange(PostStatusDTO postStatusDTO);
}
