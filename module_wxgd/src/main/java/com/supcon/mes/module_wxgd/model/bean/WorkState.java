package com.supcon.mes.module_wxgd.model.bean;

import com.supcon.mes.middleware.model.bean.ScreenEntity;

public enum WorkState {
    ALL("状态不限", ""),
    DISPATCH("待派工", "BEAM049/01"),
    CONFIRM("待确认", "BEAM049/02"),
    IMPLEMENT("待执行", "BEAM049/03"),
    ACCEPTANCE("待验收", "BEAM049/04"),
    COMPLETE("已完成", "BEAM049/05");

    private String name;
    private String layRec;

    WorkState(String name, String layRec) {
        this.name = name;
        this.layRec = layRec;
    }

    public String getName() {
        return name;
    }

    public String getLayRec() {
        return layRec;
    }

    public ScreenEntity getScreenEntity() {
        ScreenEntity screenEntity = new ScreenEntity();
        screenEntity.name = name;
        screenEntity.layRec = layRec;
        return screenEntity;
    }
}
