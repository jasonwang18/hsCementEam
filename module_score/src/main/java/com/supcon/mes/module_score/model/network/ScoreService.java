package com.supcon.mes.module_score.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_score.model.bean.ScoreListEntity;
import com.supcon.mes.module_score.model.bean.ScorePerformanceListEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

@ApiFactory(name = "ScoreHttpClient")
public interface ScoreService {


    //润滑列表
    @GET("/BEAM/scorePerformance/scoreHead/beamScoreList-query.action")
    Flowable<ScoreListEntity> getScoreList(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //润滑
    @GET
    Flowable<ScorePerformanceListEntity> getScore(@Url String url, @Query("scoreHead.id") int scoreId);

}
