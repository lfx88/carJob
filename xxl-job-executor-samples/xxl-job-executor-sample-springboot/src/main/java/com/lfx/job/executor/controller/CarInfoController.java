package com.lfx.job.executor.controller;

import com.lfx.job.executor.Utils.Result;
import com.lfx.job.executor.service.service.CarService;
import com.lfx.job.executor.dto.ReqCarSeriesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName CarInfoController
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/23 14:35
 * @Version 1.0
 */
@Controller
@RequestMapping("/carInfo")
public class CarInfoController {
    @Autowired
    private CarService carService;
    @GetMapping("/carInfo")
    @ResponseBody
    public Result capInfo(){
        return carService.makeDescriptionChinese();
    }
    @PostMapping("/carSeries")
    @ResponseBody
    public Result carSeries(ReqCarSeriesDto reqCarSeriesDto){
        return carService.carSeries(reqCarSeriesDto.getMakeDescriptionChinese());
    }
}
