package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.module_wxgd.ui.adapter.AcceptanceCheckAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/9/3
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.WXGD_ACCEPTANCE_LIST)
public class WXGDAcceptanceListActivity extends BaseRefreshRecyclerActivity<AcceptanceCheckEntity> {

    @BindByTag("contentView")
    protected RecyclerView contentView;

    @BindByTag("leftBtn")
    protected ImageButton leftBtn;

    @BindByTag("rightBtn")
    protected ImageButton rightBtn;

    @BindByTag("titleText")
    protected TextView titleText;

    private AcceptanceCheckAdapter mAcceptanceCheckAdapter;

    protected List<AcceptanceCheckEntity> mEntities = new ArrayList<>();
    protected boolean isEditable = false, isAdd = false;
    private long repairSum; // 工单执行次数

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_repair_staff_list;
    }

    @Override
    protected void onInit() {

        super.onInit();
        Intent intent = getIntent();
        String entityInfo = intent.getStringExtra(Constant.IntentKey.ACCEPTANCE_ENTITIES);
        if(TextUtils.isEmpty(entityInfo)){
            finish();
        }
        mEntities.addAll(GsonUtil.jsonToList(entityInfo, AcceptanceCheckEntity.class));

        isEditable = intent.getBooleanExtra(Constant.IntentKey.IS_EDITABLE, false);
        isAdd  =  intent.getBooleanExtra(Constant.IntentKey.IS_ADD, false);
        if (isAdd) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.IntentKey.COMMON_SAERCH_MODE, Constant.CommonSearchMode.STAFF);
            IntentRouter.go(context, Constant.Router.COMMON_SEARCH, bundle);
        }
        repairSum = intent.getLongExtra(Constant.IntentKey.REPAIR_SUM,1);
        mAcceptanceCheckAdapter.setRepairSum(repairSum);
        mAcceptanceCheckAdapter.setEditable(isEditable);

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
    }

    @Override
    protected IListAdapter<AcceptanceCheckEntity> createAdapter() {
        mAcceptanceCheckAdapter = new AcceptanceCheckAdapter(context, isEditable);
        return mAcceptanceCheckAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("验收列表");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));

        findViewById(R.id.includeSparePartLy).setVisibility(View.GONE);

        initEmptyView();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onBackPressed();
                    }
                });

    }

    @Override
    protected void initData() {
        super.initData();
        refreshListController.refreshComplete(mEntities);
    }

    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,"暂无数据"));
    }


}
