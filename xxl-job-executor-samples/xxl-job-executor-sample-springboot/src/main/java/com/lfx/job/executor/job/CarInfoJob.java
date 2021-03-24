package com.lfx.job.executor.job;

import com.alibaba.fastjson.JSON;
import com.lfx.job.executor.Utils.Result;
import com.lfx.job.executor.dao.CarCacheDao;
import com.lfx.job.executor.dto.MakeDescriptionChinese;
import com.lfx.job.executor.dto.MakeDescriptionChineseDetail;
import com.lfx.job.executor.dto.ReqCarSeriesDto;
import com.lfx.job.executor.entity.CarCache;
import com.lfx.job.executor.enums.CarTypeEnum;
import com.lfx.job.executor.service.service.CarService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CarINfo
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/24 11:08
 * @Version 1.0
 */
@Component
@Slf4j
public class CarInfoJob {
    @Autowired
    private CarService carService;
    @Autowired
    private CarCacheDao carCacheDao;
    @XxlJob("CARSERIES")
    public void carSeries(){
        //传入[{‘makeDescriptionChinese':"车辆品牌的名字"}]
        String command = XxlJobHelper.getJobParam();
        List<ReqCarSeriesDto> reqCarSeriesDtos =new ArrayList<>();
        if(command!=null && !command.equals("")){
            reqCarSeriesDtos = JSON.parseArray(command, ReqCarSeriesDto.class);
        }else{
            List<ReqCarSeriesDto> reqCarSeriesDtos1 =new ArrayList<>();
            //获取全部的
            CarCache cache =new CarCache();
            cache.setType(CarTypeEnum.LICENSEPLATE.getCode());
            CarCache query = carCacheDao.query(cache);
            Result result =Result.error("错误");
            if(query==null){
                result = carService.makeDescriptionChinese();
                if(!result.getCode().equals("200")){
                    log.info(result.getMsg().toString());
                    return;
                }
                cache.setType(CarTypeEnum.LICENSEPLATE.getCode());
                query = carCacheDao.query(cache);
            }
            String makeDescriptionChinese = query.getCache();
            List<MakeDescriptionChinese> makeDescriptionChineses=JSON.parseArray(makeDescriptionChinese,MakeDescriptionChinese.class);
            makeDescriptionChineses.forEach(item -> {
                List<MakeDescriptionChineseDetail> data = item.getData();
                data.forEach(value -> {
                    ReqCarSeriesDto reqCarSeriesDto=new ReqCarSeriesDto();
                    reqCarSeriesDto.setMakeDescriptionChinese(value.getMakeDescriptionChinese());
                    reqCarSeriesDtos1.add(reqCarSeriesDto);
                });
            });
            reqCarSeriesDtos.addAll(reqCarSeriesDtos1);
        }
        reqCarSeriesDtos.forEach(reqCarSeriesDto -> {
            carService.carSeries(reqCarSeriesDto.getMakeDescriptionChinese());
        });
    }
}
