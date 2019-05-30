package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.mbap.view.CustomTitleBar;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.IntentRouter;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.CompleteAPI;
import com.supcon.mes.module_warn.model.api.DailyLubricationWarnAPI;
import com.supcon.mes.module_warn.model.api.LubricationWarnAPI;
import com.supcon.mes.module_warn.model.bean.DelayEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnListEntity;
import com.supcon.mes.module_warn.model.contract.CompleteContract;
import com.supcon.mes.module_warn.model.contract.DailyLubricationWarnContract;
import com.supcon.mes.module_warn.model.contract.LubricationWarnContract;
import com.supcon.mes.module_warn.model.event.TabEvent;
import com.supcon.mes.module_warn.presenter.CompletePresenter;
import com.supcon.mes.module_warn.presenter.DailyLubricationWarnPresenter;
import com.supcon.mes.module_warn.presenter.LubricationWarnPresenter;
import com.supcon.mes.module_warn.ui.adapter.DailyLubricationWarnAdapter;
import com.supcon.mes.module_warn.ui.fragment.DailyLubricateReceiveTaskFragment;
import com.supcon.mes.module_warn.ui.fragment.DailyLubricateTaskFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 日常润滑
 */
@Router(Constant.Router.DAILY_LUBRICATION_EARLY_WARN)
public class DailyLubricationWarnActivity extends BaseFragmentActivity {
    @BindByTag("titleBar")
    CustomTitleBar titleBar;
    @BindByTag("tablayout")
    TabLayout tablayout;
    @BindByTag("sbdaViewPager")
    NoScrollViewPager sbdaViewPager;

    private NFCHelper nfcHelper;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_daily_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        nfcHelper = NFCHelper.getInstance();
        if (nfcHelper != null) {
            nfcHelper.setup(this);
            nfcHelper.setOnNFCListener(new NFCHelper.OnNFCListener() {
                @Override
                public void onNFCReceived(String nfc) {
                    LogUtil.d("NFC Received : " + nfc);
                    EventBus.getDefault().post(new NFCEvent(nfc));
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcHelper != null)
            nfcHelper.onResumeNFC(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleBar.setTitle("日常润滑任务");
        tablayout.setupWithViewPager(sbdaViewPager);

        DailyLubricationWarnAdapter dailyLubricationWarnAdapter = new DailyLubricationWarnAdapter(getSupportFragmentManager());
        dailyLubricationWarnAdapter.addFragment(DailyLubricateTaskFragment.newInstance(), "日常润滑任务领取");
        dailyLubricationWarnAdapter.addFragment(DailyLubricateReceiveTaskFragment.newInstance(), "日常润滑任务执行");
        sbdaViewPager.setAdapter(dailyLubricationWarnAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnTitleBarListener(new CustomTitleBar.OnTitleBarListener() {
            @Override
            public void onLeftBtnClick() {
                back();
            }

            @Override
            public void onRightBtnClick() {

            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tabChange(TabEvent tabEvent) {
        tablayout.getTabAt(1).select();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (nfcHelper != null)
            nfcHelper.dealNFCTag(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcHelper != null)
            nfcHelper.onPauseNFC(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (nfcHelper != null) {
            nfcHelper.release();
        }
    }
}
