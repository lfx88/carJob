package com.lfx.job.executor.enums;

/**
 * @ClassName CarTypeEnum
 * @Description TODO
 * @Author lfx
 * @Date 2021/2/23 15:35
 * @Version 1.0
 */
public enum CarTypeEnum {
    LICENSEPLATE(1),
    CARSERIES(2),
    CARINFO(3);
    private Integer code;
    private CarTypeEnum(Integer code){
        this.code=code;
    }

    public Integer getCode() {
        return code;
    }

    public CarTypeEnum setCode(Integer code) {
        this.code = code;
        return this;
    }
}
