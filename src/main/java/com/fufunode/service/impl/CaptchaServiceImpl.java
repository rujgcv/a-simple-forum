package com.fufunode.service.impl;

import com.fufunode.result.Result;
import com.fufunode.service.CaptchaService;
import com.fufunode.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Override
    public Result generateCaptcha() {
        // 调用验证码服务
        Map<String, Object> captchaData = verifyCodeService.getCode();

        String captchaId = UUID.randomUUID().toString();
        String imgurl = (String) captchaData.get("imgurl");
        String code = (String) captchaData.get("md5key");

        // 存储到Redis
        redisTemplate.opsForValue().set("login:captcha:"+captchaId,code,5, TimeUnit.MINUTES);

        Map<String, Object> result = new HashMap<>();
        result.put("imgurl", imgurl);
        result.put("captchaId", captchaId);

        return Result.success(result);
    }
}
