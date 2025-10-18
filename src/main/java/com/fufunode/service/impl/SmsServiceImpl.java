package com.fufunode.service.impl;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fufunode.constant.MessageConstant;
import com.fufunode.result.Result;
import com.fufunode.service.SmsService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private Client smsClient; // 注入配置好的Client

    @Value("${aliyun.sms.scheme-name:测试方案}")
    private String schemeName;

    @Value("${aliyun.sms.sign-name:速通互联验证码}")
    private String signName;

    @Value("${aliyun.sms.template-code:100001}")
    private String templateCode;

    @Override
    public Result send(String phone) {

        // 电话不能为空
        if (StringUtils.isBlank(phone)) {
            return Result.error(MessageConstant.Phone_IS_NULL);
        }
        // 验证手机号合法
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return Result.error(MessageConstant.PHONE_INVALID);
        }

        String limitKey = "sms:limit:" + phone;
        if (redisTemplate.hasKey(limitKey)) {
            return Result.error("请求过于频繁，请稍后再试");
        }

        String code = generateVerificationCode();

        try {
            SendSmsVerifyCodeRequest request = new SendSmsVerifyCodeRequest()
                    .setSchemeName(schemeName)
                    .setPhoneNumber(phone)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam("{\"code\":\""+code+"\",\"min\":\"5\"}")
                    .setCodeLength(6L)
                    .setInterval(60L)
                    .setReturnVerifyCode(true);

            SendSmsVerifyCodeResponse response = smsClient.sendSmsVerifyCode(request);

            // 根据响应判断是否成功
            if ("OK".equals(response.getBody().getCode())) {
                // 验证码写入redis
                String key = "sms:phone:" + phone;
                redisTemplate.opsForValue().set(key,code,5, TimeUnit.MINUTES);

                return Result.success("验证码发送成功");
            } else {
                return Result.error("发送失败: " + response.getBody().getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("短信发送异常: " + e.getMessage());
        }
    }

    private String generateVerificationCode(){
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }
}
