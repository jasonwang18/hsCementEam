package com.supcon.mes.module_yhgl.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;

/**
 * LubricateOilsEntityDto 润滑油实体传输对象
 * created by zhangwenshuai1 2018/9/5
 */
public class MaintainDto extends BaseEntity {
    public ValueEntity jwxItemID;
    public String sparePartName;
    public String claim;
    public String content;
    public String lastTime;
    public String nextTime;

}
