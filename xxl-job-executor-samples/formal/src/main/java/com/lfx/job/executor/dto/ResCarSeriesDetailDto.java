package com.lfx.job.executor.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName ReqCarSeriesDetailDto
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/25 10:32
 * @Version 1.0
 */
@Data
public class ResCarSeriesDetailDto {
    @JSONField(name = "MF")
    private String MF;
    @JSONField(name = "MakeDescriptionChinese")
    private String MakeDescriptionChinese;
    @JSONField(name = "FamilyDescriptionChinese")
    private String FamilyDescriptionChinese;
    @JSONField(name = "NewMakeDescriptionChinese")
    private String NewMakeDescriptionChinese;
}
