package com.supcon.mes.module_olxj.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.api.DeviceDCSParamQueryAPI;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.contract.DeviceDCSParamQueryContract;
import com.supcon.mes.middleware.presenter.DeviceDCSParamQueryPresenter;
import com.supcon.mes.module_olxj.ui.adapter.DeviceDCSParamAdapter;

/**
 * Created by wangshizhan on 2019/5/28
 * Email:wangshizhan@supcom.com
 */
@Presenter(DeviceDCSParamQueryPresenter.class)
public class DeviceDCSParamController extends BaseViewController implements DeviceDCSParamQueryContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private DeviceDCSParamAdapter mDeviceDCSParamAdapter;

    private Long eamId;

    public DeviceDCSParamController(View rootView, Long eamId) {
        this(rootView);
        this.eamId = eamId;
    }

    public DeviceDCSParamController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void initView() {
        super.initView();
        mDeviceDCSParamAdapter = new DeviceDCSParamAdapter(context);
        contentView.setLayoutManager(new LinearLayoutManager(context));  //线性布局
//        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));  //列表项之间间隔
        contentView.setAdapter(mDeviceDCSParamAdapter);
    }


    @Override
    public void initData() {
        super.initData();

        if(eamId!=null)
            presenterRouter.create(DeviceDCSParamQueryAPI.class).getDeviceDCSParams(eamId);
    }

    @Override
    public void getDeviceDCSParamsSuccess(CommonListEntity entity) {
        mDeviceDCSParamAdapter.setList(entity.result);
        mDeviceDCSParamAdapter.notifyDataSetChanged();

//        if(entity.result!=null) {
//            ViewGroup.LayoutParams lp = contentView.getLayoutParams();
//            lp.height = DisplayUtil.dip2px(20 * entity.result.size(), context);
//            contentView.setLayoutParams(lp);
//        }
    }

    @Override
    public void getDeviceDCSParamsFailed(String errorMsg) {
        LogUtil.e("errorMsg:"+errorMsg);

    }
}
