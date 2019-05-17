package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

public class MaintainEntity extends BaseEntity {

    public Long id;

    public JWXItem jwxItemID; //业务规则
    public String sparePartName;//备件名称
    public String claim;//要求
    public String content;//内容

    public Long lastTime;
    public Long nextTime;

    public Float lastDuration;//上次润滑时长
    public Float nextDuration;//下次润滑时长

    public SparePartId sparePartId;//备件编码
    public AccessoryEamId accessoryEamId;//附属设备

    public JWXItem getJwxItem() {
        if (jwxItemID == null) {
            jwxItemID = new JWXItem();
        }
        return jwxItemID;
    }

    public AccessoryEamId getAccessoryEamId() {
        if (accessoryEamId == null) {
            accessoryEamId = new AccessoryEamId();
        }
        return accessoryEamId;
    }

    public SparePartId getSparePartId() {
        if (sparePartId == null) {
            sparePartId = new SparePartId();
        }
        return sparePartId;
    }

}
