package com.supcon.mes.module_warn.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AccessoryEamId;
import com.supcon.mes.middleware.model.bean.LubricateOil;
import com.supcon.mes.middleware.model.bean.SparePartId;
import com.supcon.mes.middleware.model.bean.ValueEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/23
 * ------------- Description -------------
 */
public class DailyLubricationWarnEntity extends BaseEntity {
    public WXGDEam eamID;
    public String content;
    public String claim;
    public String lubricatePart;
    public ValueEntity generateWorkState;
    public ValueEntity periodUnit;
    public ValueEntity oilType;
    public LubricateOil lubricateOil;
    public Long lastTime;
    public Long nextTime;
    public Long advanceTime;
    public Long period;
    public Float sum;

    public SparePartId sparePartId;//备件编码
    public AccessoryEamId accessoryEamId;//附属设备

    public boolean isCheck;

    public WXGDEam getEamID() {
        if (eamID==null) {
            eamID = new WXGDEam();
        }
        return eamID;
    }

    public LubricateOil getLubricateOil() {
        if (lubricateOil==null) {
            lubricateOil = new LubricateOil();
        }
        return lubricateOil;
    }

    public ValueEntity getOilType() {
        if (oilType==null) {
            oilType = new ValueEntity();
        }
        return oilType;
    }

    public SparePartId getSparePartId() {
        if (sparePartId == null) {
            sparePartId = new SparePartId();
        }
        return sparePartId;
    }

    public AccessoryEamId getAccessoryEamId() {
        if (accessoryEamId == null) {
            accessoryEamId = new AccessoryEamId();
        }
        return accessoryEamId;
    }

}
