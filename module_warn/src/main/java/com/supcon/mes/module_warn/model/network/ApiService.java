package com.supcon.mes.module_warn.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.bean.DelayRecordListEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnListEntity;
import com.supcon.mes.module_warn.model.bean.MaintenanceWarnListEntity;
import com.supcon.mes.module_warn.model.bean.SparePartWarnListEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2016/3/23.
 */
@ApiFactory(name = "EarlyWarnHttpClient")
public interface ApiService {

    // 维保
    @GET
    Flowable<MaintenanceWarnListEntity> getMaintenance(@Url String url, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //润滑
    @GET
    Flowable<LubricationWarnListEntity> getLubrication(@Url String url, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //备件
    @GET
    Flowable<SparePartWarnListEntity> getSparePart(@Url String url, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

    //完成
    @GET("/BEAM/baseInfo/jWXItem/jwxDailyLubRecord.action")
    Flowable<DelayEntity> dailyComplete(@QueryMap Map<String, Object> pageQueryMap);


    //延期
    @GET("/BEAM/baseInfo/jWXItem/jwxDelaySetting.action")
    Flowable<DelayEntity> delayDate(@QueryMap Map<String, Object> pageQueryMap);

    //延期记录
    @GET("/BEAM/baseInfo/delayRecords/delayRecordsList-query.action")
    Flowable<DelayRecordListEntity> delayRecords(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

}
