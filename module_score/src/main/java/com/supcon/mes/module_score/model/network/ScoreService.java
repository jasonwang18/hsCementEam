package com.supcon.mes.module_score.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_score.model.bean.ScoreListEntity;
import com.supcon.mes.module_score.model.bean.ScorePerformanceListEntity;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
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

    //提交
    @POST("/BEAM/scorePerformance/scoreHead/beamPerformanceEdit/submit.action?__pc__=YmVhbVNjb3JlTGlzdF9hZGRfYWRkX0JFQU1fMS4wLjBfc2NvcmVQZXJmb3JtYW5jZV9iZWFtU2NvcmVMaXN0fA__&_bapFieldPermissonModelCode_=BEAM_1.0.0_scorePerformance_ScoreHead&_bapFieldPermissonModelName_=ScoreHead")
    @Multipart
    Flowable<BapResultEntity> doSubmit(@PartMap Map<String, RequestBody> map);

}
