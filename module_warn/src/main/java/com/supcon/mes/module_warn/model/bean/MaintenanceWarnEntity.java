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
    public Long id;
    public WXGDEam eamID;
    public String content;
    public String claim;
    public ValueEntity generateWorkState;
    public ValueEntity periodUnit;
    public Long lastTime;
    public Long nextTime;
    public Float currentDuration;//当前运行时长
    public Float lastDuration;//上次维保时长
    public Float nextDuration;//下次维保时长
    public ValueEntity periodType;//类型

    public boolean isCheck;

    public WXGDEam getEamID() {
        if (eamID==null) {
            eamID = new WXGDEam();
        }
        return eamID;
    }
    //是否润滑时长
    public boolean isDuration() {
        if (periodType != null && periodType.id.equals("BEAM014/02")) {
            return true;
        }
        return false;
    }
}
