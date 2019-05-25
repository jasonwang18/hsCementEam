package com.supcon.mes.module_warn.model.bean;

import com.google.gson.annotations.Expose;
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
public class LubricationWarnEntity extends BaseEntity {
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
    public Float currentDuration;//当前运行时长
    public Float lastDuration;//上次润滑时长
    public Float nextDuration;//下次润滑时长
    public ValueEntity periodType;//类型

    public Float sum;
    public SparePartId sparePartId;//备件编码
    public AccessoryEamId accessoryEamId;//附属设备

    public boolean isCheck;
    @Expose
    public int viewType = 0;

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

    //是否润滑时长
    public boolean isDuration() {
        if (periodType != null && periodType.id.equals("BEAM014/02")) {
            return true;
        }
        return false;
    }
}
