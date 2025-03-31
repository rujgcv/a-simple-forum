package com.fufunode.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * 生成MD5哈希值（32位小写）
     * @param input 原始字符串
     * @return MD5哈希后的字符串
     */
    public static String md5(String input) {
        try {
            // 创建MessageDigest实例，指定算法为MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 将输入字符串转换为字节数组（UTF-8编码）
            byte[] inputBytes = input.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            // 计算哈希值
            byte[] hashBytes = md.digest(inputBytes);
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不可用", e);
        }
    }
}