package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * EamType 设备类型
 * created by zhangwenshuai1 2018/8/13
 */
public class EamType extends BaseEntity implements CommonSearchEntity{
    public long id;
    public String code;
    public String name;

    public Department useDept;

    public Department getUseDept() {
        if (useDept == null) {
            useDept = new Department();
        }
        return useDept;
    }

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
}
