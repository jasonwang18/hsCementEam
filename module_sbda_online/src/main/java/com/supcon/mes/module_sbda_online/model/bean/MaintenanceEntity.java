package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class MaintenanceEntity extends BaseEntity {
    public long id;

    public String claim;//要求
    public String content;//内容
    public String remark;//备注

    public Long lastTime;//上次润滑时间
    public Long nextTime;//下次润滑时间

    public Float lastDuration;//上次润滑时长
    public Float nextDuration;//下次润滑时长

    public ValueEntity periodType;//类型

    //是否润滑时长
    public boolean isDuration() {
        if (periodType.id.equals("BEAM014/02")) {
            return true;
        }
        return false;
    }
}
