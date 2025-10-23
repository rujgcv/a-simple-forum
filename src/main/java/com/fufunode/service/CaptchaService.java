package com.fufunode.service;

import com.fufunode.result.Result;

public interface CaptchaService {
    // 生成图形验证码
    Result generateCaptcha();
}
