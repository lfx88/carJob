package com.lfx.job.executor.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName ResCarSeriesDto
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/24 11:10
 * @Version 1.0
 */
@Data
public class ResCarSeriesDto {
    private String letter;
    private List<ResCarSeriesDetailDto> data;

}
