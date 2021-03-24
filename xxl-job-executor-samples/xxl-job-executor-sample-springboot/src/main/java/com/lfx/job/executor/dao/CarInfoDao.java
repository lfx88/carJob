package com.lfx.job.executor.dao;

import com.lfx.job.executor.entity.CarInfoDetail;
import com.lfx.job.executor.entity.CarMakeDescriptionChinese;
import com.lfx.job.executor.entity.CarSeries;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @ClassName CarInfoDao
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/23 13:19
 * @Version 1.0
 */
@Mapper
public interface CarInfoDao {
    //获取车品牌
    List<CarMakeDescriptionChinese> query();
    //获取车系
    List<CarSeries> querySeries(String makeDescriptionChinese);

    List<CarInfoDetail> getAll();
}
