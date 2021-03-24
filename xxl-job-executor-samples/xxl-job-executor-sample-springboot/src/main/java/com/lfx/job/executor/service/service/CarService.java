package com.lfx.job.executor.service.service;

import com.lfx.job.executor.Utils.Result;

/**
 * @ClassName service
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/23 13:13
 * @Version 1.0
 */
public interface CarService {
    //车牌
    public Result makeDescriptionChinese();
    //车系
    public Result carSeries(String makeDescriptionChinese);


}
