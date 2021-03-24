package com.lfx.job.executor.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import java.util.List;

/**
 * @ClassName CarInfoDetail
 * @Description TODO
 * @Author lfx
 * @Date 2021/3/9 12:54
 * @Version 1.0
 */
@Data
public class CarInfoDetail {
    private String desc;
    @JSONField(name="MakeDescriptionChinese")
    private String makeDescriptionChinese;
    @JSONField(name="NewMakeDescriptionChinese")
    private String newMakeDescriptionChinese;
    @JSONField(name="FamilyDescriptionChinese")
    private String familyDescriptionChinese;
    private String info;
    private List<CarInfoDetailInfo> infos;
    private Integer id;
    @JSONField(name="NewPrice")
    private Double newPrice;
    private Integer year;
    private Integer month;
    @JSONField(name="lastyear")
    private Integer lastYear;
    private Integer monthLast;
    private String letter;

}
