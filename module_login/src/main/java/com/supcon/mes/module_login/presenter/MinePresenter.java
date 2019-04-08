package com.supcon.mes.module_login.presenter;

import android.util.Log;

import com.supcon.common.view.App;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_login.model.contract.MineContract;
import com.supcon.mes.module_login.model.network.LoginHttpClient;

/**
 * Created by wangshizhan on 2017/8/7.
 */

public class MinePresenter extends MineContract.Presenter {


    @Override
    public void logout() {
        Log.i("MinePresenter","dologout");

        //如果是离线使用
        if (SharedPreferencesUtils.getParam(App.getAppContext(), MBapConstant.SPKey.OFFLINE_ENABLE, false)) {
            getView().logoutSuccess();
            return;
        }

        mCompositeSubscription.add(
                LoginHttpClient.logout()
                        .onErrorReturn(throwable -> {
                            ResultEntity entity = new ResultEntity();
                            entity.success = false;
                            entity.errMsg = throwable.toString();
                            return entity;
                        })
                        .subscribe(resultEntity -> {
                    Log.i("MinePresenter", "onNext resultEntity:" + resultEntity);

                    if(resultEntity.success){
                        getView().logoutSuccess();
                        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.LOGIN, false);
                        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.JSESSIONID, "");
                        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.CASTGC, "");
                    }
                    else{

                        getView().logoutSuccess();
                        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.LOGIN, false);
                        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.JSESSIONID, "");
                        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.CASTGC, "");
//                        getView().logoutFailed(resultEntity.errMsg);
                    }
                })

        );


    }

}
