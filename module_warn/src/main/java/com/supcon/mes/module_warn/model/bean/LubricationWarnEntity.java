package com.supcon.mes.module_warn.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.LubricateOil;
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
    public Long advanceTime;
    public Long period;
    public Float sum;

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
}
