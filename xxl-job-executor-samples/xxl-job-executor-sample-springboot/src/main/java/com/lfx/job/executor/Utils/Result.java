package com.lfx.job.executor.Utils;

import lombok.Data;

/**
 * @ClassName Result
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/23 15:09
 * @Version 1.0
 */
@Data
public class Result<T> {
    private String code;
    private T msg;
    private String url;
    public Result(T msg){
        this.msg=msg;
    }

    public Result(String code,T msg){
        this.code=code;
        this.msg=msg;
    }

    public static Result success(Object msg){
        return new Result("200",msg);
    }
    public static Result error(Object msg){
        return new Result("500",msg);
    }
}
