package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.LubricateOil;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_wxgd.model.bean.LubricateOilsEntity;
import com.supcon.mes.module_wxgd.model.bean.RepairStaffEntity;
import com.supcon.mes.module_wxgd.model.bean.SparePartEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDTableInfoEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDDispatcherContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * WXGDDispatcherPresenter 维修工单派单Presenter
 * created by zhangwenshuai1 2018/8/15
 */

public class WXGDDispatcherPresenter extends WXGDDispatcherContract.Presenter {
    @Override
    public void getWxgdInfo(long pending, long tableInfoId) {}

    @Override
    public void translateRepair(long faultInfoId, String repairType) {
        mCompositeSubscription.add(
                HttpClient.translateRepair(faultInfoId, repairType)
                        .onErrorReturn(new Function<Throwable, ResultEntity>() {
                            @Override
                            public ResultEntity apply(Throwable throwable) throws Exception {
                                ResultEntity resultEntity = new ResultEntity();
                                resultEntity.success = false;
                                resultEntity.errMsg = throwable.toString();
                                return resultEntity;
                            }
                        })
                        .subscribe(new Consumer<ResultEntity>() {
                            @Override
                            public void accept(ResultEntity resultEntity) throws Exception {
                                if (resultEntity.success){
                                    getView().translateRepairSuccess(resultEntity);
                                }else {
                                    resultEntity.errMsg = "处理异常，请查看服务器日志信息!";
                                    getView().translateRepairFailed(resultEntity.errMsg);
                                }
                            }
                        })
        );
    }


}
