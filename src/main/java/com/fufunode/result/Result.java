package com.fufunode.result;

import lombok.Data;

import java.io.Serializable;

// 后端统一返回结果
@Data
public class Result<T> implements Serializable {

    private Integer code; // 编码: 1成功,0或其他数字失败
    private String msg; // 错误信息
    private T data; // 数据

    public static <T> Result<T> success(){
        Result<T> result = new Result<>();
        result.code=1;
        return result;
    }

    public static <T> Result<T> error(String msg){
        Result result = new Result<>();
        result.code = 0;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> success(T obj){
        Result<T> result = new Result<>();
        result.code = 1;
        result.data = obj;
        return result;
    }
}
