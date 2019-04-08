package com.supcon.mes.module_sbda_online.screen;

import com.supcon.mes.module_sbda_online.model.bean.ScreenEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/2
 * ------------- Description -------------
 */
public enum EamName {
    CRUSHER("破碎机", ""),
    RAWMILL("生料磨", "01"),
    ROTARYCELLAR("回转窖", "02"),
    CEMENTMILL("水泥磨", "03"),
    ROLLERPRESS("辊压机", "04");

    private String name;
    private String layRec;

    EamName(String name, String layRec) {
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
