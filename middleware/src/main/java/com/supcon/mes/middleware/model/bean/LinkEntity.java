package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/7/20
 * Email:wangshizhan@supcom.com
 */
public class LinkEntity extends BaseEntity {
    public String description; //中文描述
    public String name;  //迁移线
    public String source;
    public String destination;
    public String condition;

//    {
//        "condition": "",
//            "description": "作废",
//            "destination": "cancel752",
//            "name": "Link948",
//            "source": "task338"
//    }
}
