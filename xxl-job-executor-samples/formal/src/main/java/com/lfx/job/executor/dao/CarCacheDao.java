package com.lfx.job.executor.dao;

import com.lfx.job.executor.entity.CarCache;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarCacheDao {
    public void add(CarCache carCache);
    public CarCache query(CarCache carCache);
    public void update(CarCache carCache);

    void deleteType(Integer code);

    void insertAll(List<CarCache> list);
}
