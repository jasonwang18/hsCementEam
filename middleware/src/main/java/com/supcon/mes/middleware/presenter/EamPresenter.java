package com.supcon.mes.middleware.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.model.contract.EamContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EamPresenter extends EamContract.Presenter {
    @Override
    public void getEam(Map<String, Object> params, int page) {
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        if (params.containsKey(Constant.BAPQuery.EAM_CODE)) {
            Map<String, Object> codeParam = new HashMap();
            codeParam.put(Constant.BAPQuery.EAM_EXACT_CODE, params.get(Constant.BAPQuery.EAM_CODE));
            List<BaseSubcondEntity> baseSubcondEntities = BAPQueryParamsHelper.crateSubcondEntity(codeParam);
            fastQuery.subconds.addAll(baseSubcondEntities);
        }
        if (params.containsKey(Constant.BAPQuery.EAM_AREANAME)) {
            Map<String, Object> areaParam = new HashMap();
            areaParam.put(Constant.BAPQuery.EAM_AREANAME, params.get(Constant.BAPQuery.EAM_AREANAME));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(areaParam, "BEAM_AREAS,ID,EAM_BaseInfo,INSTALL_PLACE");
            fastQuery.subconds.add(joinSubcondEntity);
        }
        fastQuery.modelAlias = "baseInfo";
        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", 20);
        pageQueryParams.put("page.maxPageSize", 500);
        mCompositeSubscription.add(MiddlewareHttpClient.getEam(fastQuery, pageQueryParams)
                .onErrorReturn(throwable -> {
                    CommonListEntity<EamType> commonListEntity = new CommonListEntity();
                    commonListEntity.errMsg = throwable.toString();
                    return commonListEntity;
                }).subscribe(commonListEntity -> {
                    if (TextUtils.isEmpty(commonListEntity.errMsg)) {
                        getView().getEamSuccess(commonListEntity);
                    } else {
                        getView().getEamFailed(commonListEntity.errMsg);
                    }
                }));
    }
}
