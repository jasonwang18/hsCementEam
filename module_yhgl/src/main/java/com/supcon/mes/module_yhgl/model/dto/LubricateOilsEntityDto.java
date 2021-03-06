package com.supcon.mes.module_yhgl.model.dto;

import com.supcon.common.com_http.BaseEntity;

/**
 * LubricateOilsEntityDto 润滑油实体传输对象
 * created by zhangwenshuai1 2018/9/5
 */
public class LubricateOilsEntityDto extends BaseEntity {

    public String id;
    public IdDto lubricate;
    public IdDto lubricatePart;
    public IdDto jwxItemID;
    public String checkbox;
    public IdDto oilType;//加换油
    public String oilQuantity;
    public String remark;
    public String lubricatingPart;
}
