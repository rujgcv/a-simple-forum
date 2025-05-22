package com.fufunode.exception;

import java.io.IOException;

/*
 * @description: 文件上传异常
 * @auther: wll
 * @date: 2025/5/22 21:22
 * @param:
 * @return:
 **/
public class FileUploadException extends RuntimeException {

    public FileUploadException() {
        super();
    }

    public FileUploadException(String message) {
        super(message);
    }
    public FileUploadException(String message,Exception e) {
        super(message,e);
    }
}
