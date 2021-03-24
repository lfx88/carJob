package com.lfx.job.executor.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.lfx.job.executor.Utils.Result;
import com.lfx.job.executor.dao.CarCacheDao;
import com.lfx.job.executor.dao.CarInfoDao;
import com.lfx.job.executor.dto.MakeDescriptionChinese;
import com.lfx.job.executor.dto.MakeDescriptionChineseDetail;
import com.lfx.job.executor.dto.ResCarSeriesDetailDto;
import com.lfx.job.executor.dto.ResCarSeriesDto;
import com.lfx.job.executor.entity.CarCache;
import com.lfx.job.executor.entity.CarInfo;
import com.lfx.job.executor.entity.CarInfoDetail;
import com.lfx.job.executor.entity.CarInfoDetailInfo;
import com.lfx.job.executor.entity.CarMakeDescriptionChinese;
import com.lfx.job.executor.entity.CarSeries;
import com.lfx.job.executor.enums.CarTypeEnum;
import com.lfx.job.executor.service.service.CarService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName CarServiceImpl
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/23 13:18
 * @Version 1.0
 */
@Service
@Slf4j
public class CarServiceImpl implements CarService {
    @Autowired
    private CarInfoDao carInfoDao;
    @Autowired
    private CarCacheDao carCacheDao;
    @Override
    @XxlJob("LICENSEPLATE")
    public Result makeDescriptionChinese() {
        List<CarMakeDescriptionChinese> query = carInfoDao.query();
        //根据拼音分组
        Map<String, List<CarMakeDescriptionChinese>> collect = query.stream().collect(Collectors.groupingBy(CarMakeDescriptionChinese::getPinyin));
        Set<Map.Entry<String, List<CarMakeDescriptionChinese>>> entries = collect.entrySet();
        List<MakeDescriptionChinese> makeDescriptionChineses=new ArrayList<>();
        entries.forEach(entry->{
            MakeDescriptionChinese makeDescriptionChinese=new MakeDescriptionChinese();
            List<MakeDescriptionChineseDetail> makeDescriptionChineseDetails =new ArrayList<>();
            List<CarMakeDescriptionChinese> value = entry.getValue();
            String key = entry.getKey();
            value.forEach(item->{
                MakeDescriptionChineseDetail makeDescriptionChineseDetail=new MakeDescriptionChineseDetail();
                makeDescriptionChineseDetail.setId(item.getId());
                makeDescriptionChineseDetail.setMakeDescriptionChinese(item.getMakeDescriptionChinese());
                makeDescriptionChineseDetails.add(makeDescriptionChineseDetail);
            });
            makeDescriptionChinese.setLetter(key);
            makeDescriptionChinese.setData(makeDescriptionChineseDetails);
            makeDescriptionChineses.add(makeDescriptionChinese);
        });
        StringBuffer s=new StringBuffer(JSON.toJSONString(makeDescriptionChineses));
        //存入缓存 或者修改缓存
        CarCache carCache=new CarCache();
        carCache.setType(CarTypeEnum.LICENSEPLATE.getCode());
        CarCache carCacheQuery = carCacheDao.query(carCache);
        carCache.setCache(s.toString());
        carCache.setUpdateTime(new Date());
        carCache.setWeight(1);
        if(carCacheQuery==null){
            carCacheDao.add(carCache);
        }else{
            carCache.setId(carCacheQuery.getId());
            carCacheDao.update(carCache);
        }
        log.info(s.toString());
        return (Result.success(makeDescriptionChineses));
    }


    //车系
    public Result carSeries(String makeDescriptionChinese){
        List<CarSeries> carSeries=carInfoDao.querySeries(makeDescriptionChinese);
        log.info(JSON.toJSONString(carSeries));
        //通过NewMakeDescriptionChinese分组
        Map<String, List<CarSeries>> collect = carSeries.stream().collect(Collectors.groupingBy(CarSeries::getNewMakeDescriptionChinese));
        Set<Map.Entry<String, List<CarSeries>>> entries = collect.entrySet();
        List<ResCarSeriesDto> resCarSeriesDtoList=new ArrayList<>();
        entries.forEach(entry->{
            ResCarSeriesDto resCarSeriesDto=new ResCarSeriesDto();
            List<CarSeries> value = entry.getValue();
            String key = entry.getKey();
            resCarSeriesDto.setLetter(key);
            //去重
            Map<String, List<CarSeries>> carSeriesMap = value.stream().collect(Collectors.groupingBy(e->getKey(e)));
            List<ResCarSeriesDetailDto> resCarSeriesDetailDtoList =new ArrayList<>();
            if(carSeriesMap!=null){
                Set<Map.Entry<String, List<CarSeries>>> carSeriesSet = carSeriesMap.entrySet();
                carSeriesSet.forEach(item->{
                    ResCarSeriesDetailDto resCarSeriesDetailDto=new ResCarSeriesDetailDto();
                    List<CarSeries> carSeriesList = item.getValue();
                    String itemKey = item.getKey();
                    if(!ObjectUtils.isEmpty(carSeriesList)){
                        resCarSeriesDetailDto.setMF(itemKey);
                        resCarSeriesDetailDto.setMakeDescriptionChinese(carSeriesList.get(0).getMakeDescriptionChinese());
                        resCarSeriesDetailDto.setFamilyDescriptionChinese(carSeriesList.get(0).getFamilyDescriptionChinese());
                        resCarSeriesDetailDto.setNewMakeDescriptionChinese(key);
                    }
                    resCarSeriesDetailDtoList.add(resCarSeriesDetailDto);
                });
            }
            resCarSeriesDto.setData(resCarSeriesDetailDtoList);
            resCarSeriesDtoList.add(resCarSeriesDto);
        });
        StringBuffer stringBuffer=new StringBuffer(JSON.toJSONString(resCarSeriesDtoList));
        //存入mysql
        //存入缓存 或者修改缓存
        CarCache carCache=new CarCache();
        carCache.setType(CarTypeEnum.CARSERIES.getCode());
        carCache.setKey(makeDescriptionChinese);
        CarCache carCacheQuery = carCacheDao.query(carCache);
        carCache.setCache(stringBuffer.toString());
        carCache.setUpdateTime(new Date());
        carCache.setWeight(1);
        carCache.setKey(makeDescriptionChinese);
        if(carCacheQuery==null){
            carCacheDao.add(carCache);
        }else{
            if(carCacheQuery.getVersion()==0){
                carCache.setId(carCacheQuery.getId());
                carCacheDao.update(carCache);
            }else{
                boolean flag=true;
                //等待每隔5s调用一下
                Long start=System.currentTimeMillis()+5000;
//                2000
                while(flag){
                    //
                    System.out.println("该数据在使用"+makeDescriptionChinese);
                    if(System.currentTimeMillis()>=start){
                        //执行
                        carCache=new CarCache();
                        carCache.setType(CarTypeEnum.CARSERIES.getCode());
                        carCache.setKey(makeDescriptionChinese);
                        carCacheQuery = carCacheDao.query(carCache);
                        if(carCacheQuery.getVersion()==0){
                            carCache.setId(carCacheQuery.getId());
                            carCacheDao.update(carCache);
                            flag=false;
                        }else{
                            start=System.currentTimeMillis()+5000;
                        }
                    }

                }
            }

        }
        return (Result.success(resCarSeriesDtoList));
    }

    private String getKey(CarSeries e) {
        return e.getMakeDescriptionChinese()+" "+e.getFamilyDescriptionChinese();
    }

    /**
     * 车辆信息
     */
    @XxlJob("TEST")
    public void getCarInfo(){
        //获取全部的 按照车的名字和车的VehicleKey分组
        List<CarInfoDetail> allCarInfoDetails = getAllCarInfoDetails();
        if(ObjectUtils.isEmpty(allCarInfoDetails)){
             log.info("==============暂无数据=================");
             return;
        }
        List<CarInfoDetail> ClearCarInfoDetails=new ArrayList<>();
        //通过 名字分组 来获取这个车的开始时间和结束时间
        Map<String, List<CarInfoDetail>> collect1 = allCarInfoDetails.stream().collect(Collectors.groupingBy(e->getKey1(e)));
        //根据名字去重
        Set<Map.Entry<String, List<CarInfoDetail>>> set1 = collect1.entrySet();
        set1.forEach(stringListEntry -> {
            List<CarInfoDetail> value = stringListEntry.getValue();
            if(!ObjectUtils.isEmpty(value)){
                ClearCarInfoDetails.add(value.get(0));
            }
        });
        //根据MakeDescriptionChinese,NewMakeDescriptionChinese,FamilyDescriptionChinese分组
        Map<String, List<CarInfoDetail>> collect = ClearCarInfoDetails.stream().collect(Collectors.groupingBy(e -> getCarInfoKey(e)));
        Set<Map.Entry<String, List<CarInfoDetail>>> entries = collect.entrySet();
        Map<String,List<CarInfo>> map=new HashMap<>();
        entries.forEach(item->{
            List<CarInfo> carInfos=new ArrayList<>();
            //转下数据
            List<CarInfoDetail> value = item.getValue();
            //通过letter 分组
            if(!ObjectUtils.isEmpty(value)){
                List<CarInfoDetail> items=new ArrayList<>();
                for(CarInfoDetail carInfoDetail:value){
                    if(carInfoDetail.getLetter()!=null && !carInfoDetail.getLetter().equals("")){
                        items.add(carInfoDetail);
                    }else{
                        log.info(JSON.toJSONString(carInfoDetail)+"letter 为空 不能生成信息");
                    }
                }
                Map<String, List<CarInfoDetail>> letters=null;
                try{
                     letters = items.stream().collect(Collectors.groupingBy(CarInfoDetail::getLetter));
                }catch (Exception e){
                    log.info(JSON.toJSONString(value)+"Map错误生成失败");
                }
                if(!ObjectUtils.isEmpty(letters)){
                    Set<Map.Entry<String, List<CarInfoDetail>>> set = letters.entrySet();
                    set.forEach(i->{
                        String letter=i.getKey();
                        System.out.println("letter:"+letter);
                        List<CarInfoDetail> value1 = i.getValue();
                        CarInfo carInfo=new CarInfo();
                        carInfo.setLetter(letter);
                        List<CarInfoDetail> carInfoDetails=new ArrayList<>();
                        List<CarInfoDetailInfo> carInfoDetailInfos=new ArrayList<>();
                        value1.forEach(value11->{
                            CarInfoDetail carInfoDetail=new CarInfoDetail();
                            BeanUtils.copyProperties(value11,carInfoDetail);
                            CarInfoDetailInfo carInfoDetailInfo=new CarInfoDetailInfo();
                            carInfoDetailInfo.setDescriptionCH(value11.getDesc());
                            List<CarInfoDetail> carInfoDetails1 = collect1.get(getKey1(value11));
                            if(!ObjectUtils.isEmpty(carInfoDetails1)){
                                //开始时间
                                CarInfoDetail carInfoDetail1 = carInfoDetails1.get(0);
                                carInfoDetail.setYear(carInfoDetail1.getYear()-1);
                                if(carInfoDetail1.getMonth()==0){
                                    carInfoDetail.setMonth(carInfoDetail1.getMonth()+1);
                                }else{
                                    carInfoDetail.setMonth(carInfoDetail1.getMonth());
                                }
                                //结束时间
                                int size = carInfoDetails1.size();
                                System.out.println("长度："+size);
                                CarInfoDetail carInfoDetail2 = carInfoDetails1.get(size - 1);
                                if(!ObjectUtils.isEmpty(carInfoDetail2)){
                                    if(carInfoDetail2.getYear()!=null && !carInfoDetail2.getYear().equals("") && carInfoDetail2.getMonth()!=null && !carInfoDetail2.getMonth().equals("")){
                                        Integer endYear=carInfoDetail2.getYear()+3;
                                        Integer endMonth=carInfoDetail2.getMonth();
                                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY");
                                        Integer newYear = Integer.parseInt(simpleDateFormat.format(new Date()));
                                        if(endYear>=newYear) {
                                            endYear =newYear;
                                            endMonth= ((new Date()).getMonth()+1);
                                        }
                                        if(endMonth==0){
                                            endMonth++;
                                        }
                                        carInfoDetail.setLastYear(endYear);
                                        carInfoDetail.setMonthLast(endMonth);
                                    }
                                }else{
                                    log.info(carInfoDetail2.getId()+"_"+carInfoDetail2.getDesc()+"============"+carInfoDetail2.getYear()+"=========================空");
                                    log.info("============"+carInfoDetail2.getMonth()+"=========================空");

                                }


                            }else{
                                log.info("============"+JSON.toJSONString(carInfoDetails1)+"=========================空");
                            }
                            carInfoDetailInfos.add(carInfoDetailInfo);
                            carInfoDetail.setInfos(carInfoDetailInfos);
                            carInfoDetails.add(carInfoDetail);
                        });
                        carInfo.setData(carInfoDetails);
                        carInfos.add(carInfo);
                    });
                    map.put(item.getKey(),carInfos);
                }else{
                    log.info("letterskong");
                }

            }else{
                log.info("============"+JSON.toJSONString(item)+"=========================空");

            }

        });
        //将map存入缓存
        Set<Map.Entry<String, List<CarInfo>>> allCaches = map.entrySet();
        List<CarCache> carCaches=new ArrayList<>();
        allCaches.forEach(allCache->{
            String key = allCache.getKey();
            List<CarInfo> carInfos = allCache.getValue();
            getLetterSort(carInfos);
            StringBuffer value = new StringBuffer(JSON.toJSONString(carInfos));
            CarCache carCache=new CarCache();
            carCache.setType(CarTypeEnum.CARINFO.getCode());
            carCache.setKey(key);
            carCache.setWeight(1);
            carCache.setUpdateTime(new Date());
            carCache.setCache(value.toString());
            carCache.setVersion(0);
            log.info("===============================缓存的名字：="+key+"" +
                    "=========值：=="+value+"====================");
            carCaches.add(carCache);

        });
        //删除数据
        carCacheDao.deleteType(CarTypeEnum.CARINFO.getCode());
        //分组
        int size = carCaches.size();
        int pageNum=100;
        int max=size/pageNum;
        int yu=size%pageNum;
        if(yu!=0){
            max++;
        }
        for (int i=0;i<max;i++){
            List<CarCache> collect2 = carCaches.stream().skip(i * pageNum).limit(pageNum).collect(Collectors.toList());
            carCacheDao.insertAll(collect2);
        }
        //存入数据 插入全部
    }

    private String getKey1(CarInfoDetail e) {
        return e.getLetter()+"_"+e.getDesc();
    }

    private String getCarInfoKey(CarInfoDetail e) {
        return e.getMakeDescriptionChinese()+"_"+e.getNewMakeDescriptionChinese()+"_"+e.getFamilyDescriptionChinese();
    }

    private List<CarInfoDetail> getAllCarInfoDetails(){
        List<CarInfoDetail> items = carInfoDao.getAll();
        return items;
    }

    public void getLetterSort(List<CarInfo> carInfos){
        Collections.sort(carInfos, new Comparator<CarInfo>() {
            @Override
            public int compare(CarInfo o1, CarInfo o2) {
                String letterO2 = o2.getLetter();
                String substring ="";
                if( o2.getLetter().length()>=4){
                    substring = letterO2.substring(0, 4);
                }else {
                    substring=letterO2;
                }
                String o1Letter = o1.getLetter();
                String substringO1 ="";
                if( o1Letter.length()>=4){
                    substringO1 = o1Letter.substring(0, 4);
                }else {
                    substringO1=o1Letter;
                }
                return substring.compareTo(substringO1);
            }
        });
    }
}
