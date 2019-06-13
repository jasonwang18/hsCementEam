package com.supcon.mes.module_warn.ui.util;

import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnEntity;
import com.supcon.mes.module_warn.model.bean.MaintenanceWarnEntity;
import com.supcon.mes.module_warn.model.bean.SparePartWarnEntity;

public class WXGDWarnManager {

    public static WXGDEntity lubri(LubricationWarnEntity lubricationWarnEntity) {
        WXGDEntity wxgdEntity = new WXGDEntity();
        return wxgdEntity;
    }

    public static WXGDEntity mainten(MaintenanceWarnEntity maintenanceWarnEntity) {
        WXGDEntity wxgdEntity = new WXGDEntity();
        return wxgdEntity;
    }

    public static WXGDEntity spare(SparePartWarnEntity sparePartWarnEntity) {
        WXGDEntity wxgdEntity = new WXGDEntity();
        return wxgdEntity;
    }
}
