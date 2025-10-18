package com.fufunode.config;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.CreateSchemeConfigRequest;
import com.aliyun.dypnsapi20170525.models.CreateVerifySchemeRequest;
import com.aliyun.dypnsapi20170525.models.CreateVerifySchemeResponse;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fufunode.constant.AppNameConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.sms.endpoint}")
    private String endpoint;

    @Bean
    public Client smsClient() throws Exception{
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        config.setEndpoint(endpoint);

        return new Client(config);
    }
}
