package com.lfx.job.executor.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName MakeDescriptionChineseDetail
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/23 14:02
 * @Version 1.0
 */
@Data
public class MakeDescriptionChineseDetail {
    private Integer id;
    @JSONField(name = "MakeDescriptionChinese")
    private String MakeDescriptionChinese;
}
