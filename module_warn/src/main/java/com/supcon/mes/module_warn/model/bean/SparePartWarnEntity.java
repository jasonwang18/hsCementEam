package com.supcon.mes.module_warn.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.ValueEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/23
 * ------------- Description -------------
 */
public class SparePartWarnEntity extends BaseEntity {
    public WXGDEam eamID;
    public String spareMemo;
    public ValueEntity sparePartStatus;
    public ValueEntity periodUnit;
    public Good productID;//备件
    public Long lastTime;
    public Long nextTime;
    public Long advanceTime;

    public boolean isCheck;

    public WXGDEam getEamID() {
        if (eamID==null) {
            eamID = new WXGDEam();
        }
        return eamID;
    }

    public Good getProductID() {
        if (productID==null) {
            productID = new Good();
        }
        return productID;
    }
}
