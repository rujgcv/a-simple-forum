package com.fufunode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {

    private static String domain;
    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String bucketName;

    @Value("${fufu.alioss.domain}")
    public void setDomain(String domain) {
        OSSConfig.domain = domain;
    }

    @Value("${fufu.alioss.endpoint}")
    public void setEndpoint(String endpoint) {
        OSSConfig.endpoint = endpoint;
    }

    @Value("${fufu.alioss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        OSSConfig.accessKeyId = accessKeyId;
    }

    @Value("${fufu.alioss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        OSSConfig.accessKeySecret = accessKeySecret;
    }

    @Value("${fufu.alioss.bucketName}")
    public void setBucketName(String bucketName) {
        OSSConfig.bucketName = bucketName;
    }

    // 提供静态方法获取配置值
    public static String getDomain() {
        return domain;
    }

    public static String getEndpoint() {
        return endpoint;
    }

    public static String getAccessKeyId() {
        return accessKeyId;
    }

    public static String getAccessKeySecret() {
        return accessKeySecret;
    }

    public static String getBucketName() {
        return bucketName;
    }
}