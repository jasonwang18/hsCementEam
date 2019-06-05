package com.supcon.mes.module_score.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_score.model.bean.ScoreListEntity;
import com.supcon.mes.module_score.model.contract.ScoreListContract;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreListPresenter extends ScoreListContract.Presenter {
    @Override
    public void getScoreList(Map<String, Object> param, int page) {
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        if (param.containsKey(Constant.BAPQuery.SCORE_TIME_START) || param.containsKey(Constant.BAPQuery.SCORE_TIME_STOP)) {
            Map<String, Object> timeParam = new HashMap<>();
            timeParam.put(Constant.BAPQuery.SCORE_TIME_START, param.get(Constant.BAPQuery.SCORE_TIME_START));
            timeParam.put(Constant.BAPQuery.SCORE_TIME_STOP, param.get(Constant.BAPQuery.SCORE_TIME_STOP));
            List<BaseSubcondEntity> subcondEntities = BAPQueryParamsHelper.crateSubcondEntity(timeParam);
            fastQuery.subconds.addAll(subcondEntities);
        }

        if (param.containsKey(Constant.BAPQuery.EAM_CODE) || param.containsKey(Constant.BAPQuery.EAM_NAME)) {
            Map<String, Object> eamParam = new HashMap<>();
            eamParam.put(Constant.BAPQuery.EAM_CODE, param.get(Constant.BAPQuery.EAM_CODE));
            eamParam.put(Constant.BAPQuery.EAM_NAME, param.get(Constant.BAPQuery.EAM_NAME));
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(eamParam, "EAM_BaseInfo,EAM_ID,BEAM_SCORE_HEADS,BEAM_ID");
            fastQuery.subconds.add(joinSubcondEntity);
        }
        fastQuery.modelAlias = "scoreHead";

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("page.pageNo", page);
        pageQueryParams.put("page.pageSize", 500);
        pageQueryParams.put("page.maxPageSize", 500);
        mCompositeSubscription.add(ScoreHttpClient.getScoreList(fastQuery, pageQueryParams)
                .onErrorReturn(throwable -> {
                    ScoreListEntity scoreListEntity = new ScoreListEntity();
                    scoreListEntity.errMsg = throwable.toString();
                    return scoreListEntity;
                }).subscribe(scoreListEntity -> {
                    if (TextUtils.isEmpty(scoreListEntity.errMsg)) {
                        getView().getScoreListSuccess(scoreListEntity);
                    } else {
                        getView().getScoreListFailed(scoreListEntity.errMsg);
                    }
                }));
    }
}
