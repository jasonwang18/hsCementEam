package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * LubricateOilsEntity 润滑油实体
 * created by zhangwenshuai1 2018/8/15
 */
public class RefLubricateEntity extends BaseEntity {

    public Long id;
    public LubricateOil lubricateOil;
    public Integer timesNum;//次数
    public SystemCodeEntity oilType;//加换油
    public String remark;//备注
    public String claim;
    public String lubricatePart;
    public String version;
    public String unitName;//单位
    public Float sum;//用量

    public SparePartId sparePartId;//备件编码
    public AccessoryEamId accessoryEamId;//附属设备

    public SystemCodeEntity getOilType() {
        if (oilType == null) {
            oilType = new SystemCodeEntity();
        }
        return oilType;
    }

    public LubricateOil getLubricateOil() {
        if (lubricateOil == null) {
            lubricateOil = new LubricateOil();
        }
        return lubricateOil;
    }
}
