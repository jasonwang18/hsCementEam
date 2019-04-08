package com.supcon.mes.module_wxgd.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

import java.math.BigDecimal;

/**
 * SparePartEntity 备件实体
 * created by zhangwenshuai1 2018/8/15
 */
public class SparePartEntity extends BaseEntity {

    public Long id;
    public Good productID;//备件
    public BigDecimal sum;  //计划领用量
    public int timesNum;//次数
    public String remark;//备注
    public BigDecimal useQuantity; // 领用量
    public SystemCodeEntity useState; // 状态
    public BigDecimal standingCrop; // 现存量
    public Long sparePartId; // 基础备件（来源备件更换到期）
    public BigDecimal actualQuantity; // 实际用量
    public boolean isDeliveried; // 是否领用出库
    public boolean isRef; // 是否参照
    public String version;
}
