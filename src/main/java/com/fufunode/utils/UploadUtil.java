package com.fufunode.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSBuilder;
import com.aliyun.oss.OSSClientBuilder;
import com.fufunode.config.OSSConfig;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UploadUtil {

    // 上传文件
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

    // 删除文件
    public static boolean delete(String filepath) {
        // OSS客户端对象
        OSS ossClient = new OSSClientBuilder().build(
                OSSConfig.getEndpoint(),
                OSSConfig.getAccessKeyId(),
                OSSConfig.getAccessKeySecret()
        );

        String bucketName = OSSConfig.getBucketName();
        filepath = filepath.replace("https://tb-fufu.oss-cn-hangzhou.aliyuncs.com/","");

        try {
            // 删除文件。
            ossClient.deleteObject(bucketName, filepath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return true;
    }

    // 批量删除文件
    public static boolean deleteBatch(List<String> filepaths) {
        // OSS客户端对象
        OSS ossClient = new OSSClientBuilder().build(
                OSSConfig.getEndpoint(),
                OSSConfig.getAccessKeyId(),
                OSSConfig.getAccessKeySecret()
        );

        String bucketName = OSSConfig.getBucketName();

        try {
            for(String filepath: filepaths){
                filepath = filepath.replace("https://tb-fufu.oss-cn-hangzhou.aliyuncs.com/","");
                // 删除文件。
                ossClient.deleteObject(bucketName, filepath);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return true;
    }
}
