package com.lfx.job.executor.config;

import com.lfx.job.executor.Utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName FailCotroller
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/24 10:30
 * @Version 1.0
 */
@RestControllerAdvice
public class FailController {
    @ExceptionHandler(Exception.class)
    public Result Error(Exception e){
        return Result.error(e.getMessage());
    }
}
