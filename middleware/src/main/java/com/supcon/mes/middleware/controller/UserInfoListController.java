package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.UserListQueryAPI;
import com.supcon.mes.middleware.model.bean.UserInfo;
import com.supcon.mes.middleware.model.bean.UserInfoDao;
import com.supcon.mes.middleware.model.bean.UserInfoListEntity;
import com.supcon.mes.middleware.model.contract.UserListQueryContract;
import com.supcon.mes.middleware.presenter.UserInfoPresenter;
import com.supcon.mes.middleware.util.PinYinUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/9/19
 * Email:wangshizhan@supcom.com
 */
@Presenter(UserInfoPresenter.class)
public class UserInfoListController extends BasePresenterController implements UserListQueryAPI, UserListQueryContract.View{

    @Override
    public void onInit() {
        super.onInit();
        queryUserInfoList(1);
    }

    @Override
    public void queryUserInfoList(int pageNo) {
        presenterRouter.create(UserListQueryAPI.class).queryUserInfoList(pageNo);
    }

    @Override
    public void queryUserInfoListSuccess(UserInfoListEntity entity) {
        if(entity.result!=null && entity.result.size()!=0){
            EamApplication.dao().getStaffDao().deleteAll();
            EamApplication.dao().getUserInfoDao().deleteAll();
            for (UserInfo userInfo :entity.result){
//                EamApplication.dao().getStaffDao().insertOrReplace(userInfo.staff);
                userInfo.namePinyin = PinYinUtils.getPinyin(userInfo.name);
                userInfo.host = EamApplication.getIp();
                userInfo.staffId = userInfo.staff.id;
                userInfo.staffName= userInfo.staff.name;
                userInfo.staffCode = userInfo.staff.code;
//                EamApplication.dao().getUserInfoDao().insertOrReplace(userInfo);
//
//                UserInfo test =  EamApplication.dao().getUserInfoDao().queryBuilder().where(UserInfoDao.Properties.Id.eq(userInfo.id)).unique();
//                LogUtil.w(test.toString());
                EamApplication.dao().getStaffDao().insertOrReplace(userInfo.staff);
            }
        }

        EamApplication.dao().getUserInfoDao().insertOrReplaceInTx(entity.result);
        List<UserInfo> userInfos = EamApplication.dao().getUserInfoDao().loadAll();
        LogUtil.w(userInfos.toString());

        if(entity.hasNext){
            queryUserInfoList(entity.pageNo+1);
        }
    }

    @Override
    public void queryUserInfoListFailed(String errorMsg) {
        LogUtil.e("UserInfoListController:"+errorMsg);
    }
}
