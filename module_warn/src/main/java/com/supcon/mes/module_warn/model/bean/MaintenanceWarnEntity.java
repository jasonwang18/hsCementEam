package com.supcon.mes.module_warn.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/23
 * ------------- Description -------------
 */
public class MaintenanceWarnEntity extends BaseEntity {
    public WXGDEam eamID;
    public String content;
    public String claim;
    public ValueEntity generateWorkState;
    public ValueEntity periodUnit;
    public Long lastTime;
    public Long nextTime;
    public Long advanceTime;

    public WXGDEam getEamID() {
        if (eamID==null) {
            eamID = new WXGDEam();
        }
        return eamID;
    }
}
