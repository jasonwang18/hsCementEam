package com.supcon.mes.module_wxgd.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * LubricateOilsEntityDto 润滑油实体传输对象
 * created by zhangwenshuai1 2018/9/5
 */
public class LubricateOilsEntityDto extends BaseEntity {
    public String id;
    public LubricateOilDto lubricate;
    public SystemCodeEntity oilType;//加换油
    public String oilQuantity;
    public String timesNum;
    public String rowIndex;
    public String version;
    public String sort;
    public String remark;
}
