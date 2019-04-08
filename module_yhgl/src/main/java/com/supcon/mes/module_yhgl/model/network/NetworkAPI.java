package com.supcon.mes.module_yhgl.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_yhgl.model.bean.YHListEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2016/3/23.
 */
@ApiFactory(name = "YHGLHttpClient")
public interface NetworkAPI {

     /**
     * 查询隐患管理待办
     * @return
     */
    @GET("/BEAM2/faultInfo/faultInfo/faultInfoList-pending.action?processKey=faultInfoFW")
    Flowable<YHListEntity> faultInfoListPending(@QueryMap Map<String, Object> queryParam, @Query("fastQueryCond") FastQueryCondEntity fastQueryCondEntity);

    /**
     * 审批提交隐患单
     * @param map
     * @return
     */
    @POST("/BEAM2/faultInfo/faultInfo/faultInfoView/submit.action?__pc__=dGFzazUyMHxmYXVsdEluZm9GVw__")
    Flowable<BapResultEntity> viewSubmit(@QueryMap Map<String, Object> map);

    /**
     * 编辑提交隐患单
     * @param map
     * @return
     */
    @POST("/BEAM2/faultInfo/faultInfo/faultInfoEdit/submit.action?__pc__=dGFzazM0MnxmYXVsdEluZm9GVw__")
    @Multipart
    Flowable<BapResultEntity> editSubmit(@QueryMap Map<String, Object> map, @Part List<MultipartBody.Part> partList);


    /**
     * 查询隐患管理待办
     * @return
     */
    @GET("/services/public/BEAM2/faultInfo/getFaultInfos/{pageNo}/{pageSize}?securityKey=securityKey&securityId=restfulKey&returnType=json")
    Flowable<YHListEntity> faultInfoList(@Path("pageNo") int pageNo, @Path("pageSize") int pageSize);
}
