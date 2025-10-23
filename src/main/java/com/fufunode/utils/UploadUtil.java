package com.fufunode.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSBuilder;
import com.aliyun.oss.OSSClientBuilder;
import com.fufunode.config.OSSConfig;
import com.fufunode.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public class UploadUtil {

    // 本地存储绝对路径（注意结尾带斜杠）
    // 基础配置（根据实际情况修改）
    private static final String ABSOLUTE_BASE = "E:\\Projects\\03-仿贴吧论坛\\project\\src\\main\\resources\\upload\\";
    private static final String SERVER_BASE_URL = "http://localhost:8080";
    private static final String RELATIVE_PATH = "/upload/";

    // 上传文件
    /*public static String uploadImage(MultipartFile file) throws IOException {
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
    }*/
    public static String uploadImage(MultipartFile file){
        // 文件校验
        if (file.isEmpty()) {
            throw new FileUploadException("文件不能为空");
        }

        String contentType = file.getContentType();
        if (!Arrays.asList("image/jpg","image/jpeg", "image/png").contains(contentType)) {
            throw new FileUploadException("仅支持PNG/JPG/JPEG格式");
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            throw new FileUploadException("文件大小超过2MB限制");
        }

        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString().replace("-","")
                + "." + ext;
        String path = ABSOLUTE_BASE + "img\\" + fileName;

        // 创建目录（如果不存在）
        new File(ABSOLUTE_BASE + "img").mkdirs();

        try {
            file.transferTo(new File(path));
            return SERVER_BASE_URL + RELATIVE_PATH + "img/" + fileName;
        } catch (IOException e) {
            throw new FileUploadException("文件上传失败", e);
        }
    }

    // 删除文件
    /*public static boolean delete(String filepath) {
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
    }*/
    public static boolean delete(String filepath){
        if(filepath == null || filepath.isEmpty()) return true;
        String absolutePath = convertUrlToAbsolutePath(filepath);

        File file = new File(absolutePath);
        if(file.exists()){
            return file.delete();
        }
        return false;
    }

    // 批量删除文件
    /*public static boolean deleteBatch(List<String> filepaths) {
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
    }*/
    public static boolean deleteBatch(List<String> filepaths){
        if (filepaths == null || filepaths.isEmpty()) {
            return true;
        }

        boolean success = true;
        for (String path : filepaths) {
            if (path != null && !path.isEmpty() && !delete(path)) {
                log.warn("文件删除失败: {}", path);
                success = false;
            }
        }
        return success;
    }

    // 相对路径转绝对路径
    private static String convertUrlToAbsolutePath(String fileUrl) {
        // 去除协议和域名部分
        String path = fileUrl.replace(SERVER_BASE_URL, "")
                .replace(RELATIVE_PATH, "")
                .replace("/", File.separator);

        return ABSOLUTE_BASE + path;
    }
}
