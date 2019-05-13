package com.supcon.mes.module_warn.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnListEntity;
import com.supcon.mes.module_warn.model.bean.MaintenanceWarnListEntity;
import com.supcon.mes.module_warn.model.bean.SparePartWarnListEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2016/3/23.
 */
@ApiFactory(name = "EarlyWarnHttpClient")
public interface ApiService {

    // 维保
    @GET("/BEAM/baseInfo/jWXItem/data-dg1531171100751.action")
    Flowable<MaintenanceWarnListEntity> getMaintenance(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity,@QueryMap Map<String, Object> pageQueryMap);

    //润滑
    @GET("/BEAM/baseInfo/jWXItem/data-dg1530747504994.action")
    Flowable<LubricationWarnListEntity> getLubrication(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity,@QueryMap Map<String, Object> pageQueryMap);

    //备件
    @GET("/BEAM/baseInfo/sparePart/data-dg1535424823416.action")
    Flowable<SparePartWarnListEntity> getSparePart(@Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity, @QueryMap Map<String, Object> pageQueryMap);

}
