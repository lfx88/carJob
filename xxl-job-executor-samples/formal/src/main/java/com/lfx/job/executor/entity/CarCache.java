package com.lfx.job.executor.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName Cache
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/23 15:41
 * @Version 1.0
 */
@Data
public class CarCache {
    private Integer id;
    private String cache;
    private Integer type;
    private Date updateTime;
    private String key;
    private Integer weight;
    private Integer version=0;
}
