package com.fufunode.controller;

import com.fufunode.result.Result;
import com.fufunode.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/captcha")
@Slf4j
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    // 生成图形验证码
    @GetMapping("/generate")
    public Result generateCaptcha() {
        log.info("生成图形验证码:{}");
        return captchaService.generateCaptcha();
    }
}
