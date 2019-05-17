package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.LubricateOil;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

import java.math.BigDecimal;

/**
 * LubricateOilsEntity 润滑油实体
 * created by zhangwenshuai1 2018/8/15
 */
public class LubricateOilsEntity extends BaseEntity {

    public Long id;
    public LubricateOil lubricate;
    public BigDecimal oilQuantity;//数量
    public Integer timesNum;//次数
    public SystemCodeEntity oilType;//加换油
    public String remark;//备注
    public String version;

}
