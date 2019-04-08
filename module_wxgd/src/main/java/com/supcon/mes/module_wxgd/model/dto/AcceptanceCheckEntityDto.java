package com.supcon.mes.module_wxgd.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * AcceptanceCheckEntityDto 验收实体传输对象
 * created by zhangwenshuai1 2018/9/5
 */
public class AcceptanceCheckEntityDto extends BaseEntity {

    public String id;
    public String version;
    public String timesNum;
    public String rowIndex;
    public String checkTime;
    public StaffDto checkStaff;
    public SystemCodeEntity checkResult;
    public String remark;
}
