package com.fufunode.controller;

import com.fufunode.pojo.dto.SmsCodeDTO;
import com.fufunode.result.Result;
import com.fufunode.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    // 验证码发送
    @PostMapping("send")
    public Result sendSmsCode(@RequestBody SmsCodeDTO smsCodeDTO){
        log.info("验证码发送:{}",smsCodeDTO);
        String phone = smsCodeDTO.getPhone();
        return smsService.send(phone);
    }
}
