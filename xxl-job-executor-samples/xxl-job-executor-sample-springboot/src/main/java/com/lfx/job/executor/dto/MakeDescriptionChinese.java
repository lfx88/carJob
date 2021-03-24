package com.lfx.job.executor.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName makeDescriptionChinese
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/23 13:59
 * @Version 1.0
 */
@Data
public class MakeDescriptionChinese {
    private String letter;
    private List<MakeDescriptionChineseDetail> data;
}
