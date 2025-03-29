package com.fufunode.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSBuilder;
import com.aliyun.oss.OSSClientBuilder;
import com.fufunode.config.OSSConfig;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class UploadUtil {

    public static String uploadImage(MultipartFile file) throws IOException {
        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String ext = "." + FilenameUtils.getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + ext;
        // OSS客户端对象
        OSS ossClient = new OSSClientBuilder().build(
                OSSConfig.getEndpoint(),
                OSSConfig.getAccessKeyId(),
                OSSConfig.getAccessKeySecret()
        );

        ossClient.putObject(
            OSSConfig.getBucketName(), // 仓库名
            fileName, // 文件名
            file.getInputStream()
        );
        ossClient.shutdown();
        return OSSConfig.getDomain() + fileName;
    }
}
