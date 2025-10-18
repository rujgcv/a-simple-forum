package com.fufunode.service;

import com.fufunode.result.Result;

public interface SmsService {

    // 验证码发送
    Result send(String phone);
}
