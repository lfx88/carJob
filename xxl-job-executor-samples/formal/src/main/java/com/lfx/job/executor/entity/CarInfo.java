package com.lfx.job.executor.entity;

import lombok.Data;

import java.util.List;

/**
 * @ClassName CarInfo
 * @Description TODO
 * @Author lfx
 * @Date 2021/3/9 12:52
 * @Version 1.0
 */
@Data
public class CarInfo {
    private String letter;
    private List<CarInfoDetail> data;
}
