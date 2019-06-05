package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

public class EamEntity extends BaseEntity implements CommonSearchEntity {

    public String code;
    public String name;
    public EamType eamType;//设备类型
    public Department useDept;//部门
    public Staff dutyStaff;//负责人

    @Override
    public String getSearchId() {
        return code;
    }

    @Override
    public String getSearchName() {
        return name;
    }

    @Override
    public String getSearchProperty() {
        return getUseDept().name;
    }

    public Department getUseDept() {
        if (useDept == null) {
            useDept = new Department();
        }
        return useDept;
    }
}
