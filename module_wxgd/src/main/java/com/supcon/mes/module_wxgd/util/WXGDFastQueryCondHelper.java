package com.supcon.mes.module_wxgd.util;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;

import java.util.Map;

/**
 * WXGDFastQueryCondHelper 维修工单快速查询helper
 * created by zhangwenshuai1 2018/9/18
 */
public class WXGDFastQueryCondHelper {

    public static FastQueryCondEntity createFastQueryCond(Map<String,Object> queryMap){
        if(queryMap.containsKey(Constant.BAPQuery.WXGD_REPAIR_TYPE) || queryMap.containsKey(Constant.BAPQuery.WXGD_PRIORITY) || queryMap.containsKey(Constant.BAPQuery.EAM_NAME)){
            return BAPQueryParamsHelper.createJoinFastQueryCond(queryMap);
        }
        return BAPQueryParamsHelper.createSingleFastQueryCond(queryMap);
    }

}
