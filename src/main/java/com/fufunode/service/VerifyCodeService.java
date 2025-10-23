package com.fufunode.service;

import java.util.Map;

public interface VerifyCodeService {
    // 调用第三方api获取图形验证码
    Map<String, Object> getCode();

    // 调用第三方api校验验证码
    Boolean verifyCode(String userCode,String md5Key);
}
