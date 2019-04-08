package com.supcon.mes.module_sbda_online.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomTitleBar;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.api.SBDAOnlineListAPI;
import com.supcon.mes.module_sbda_online.model.api.StopPoliceAPI;
import com.supcon.mes.module_sbda_online.model.bean.ScreenEntity;
import com.supcon.mes.module_sbda_online.model.bean.StopPoliceEntity;
import com.supcon.mes.module_sbda_online.model.bean.StopPoliceListEntity;
import com.supcon.mes.module_sbda_online.model.contract.StopPoliceContract;
import com.supcon.mes.module_sbda_online.presenter.StopPolicePresenter;
import com.supcon.mes.module_sbda_online.screen.FilterHelper;
import com.supcon.mes.module_sbda_online.ui.adapter.StopPoliceAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

@Router(Constant.Router.STOP_POLICE)
@Presenter(value = StopPolicePresenter.class)
public class StopPoliceListActivity extends BaseRefreshRecyclerActivity<StopPoliceEntity> implements StopPoliceContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("titleBar")
    CustomTitleBar titleBar;

    @BindByTag("listEamNameFilter")
    CustomFilterView listEamNameFilter;

    private Map<String, Object> queryParam = new HashMap<>();
    private DatePickController datePickController;

    @Override
    protected IListAdapter createAdapter() {
        StopPoliceAdapter stopPoliceAdapter = new StopPoliceAdapter(this);
        return stopPoliceAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_stop_police_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(5));

        listEamNameFilter.setData(FilterHelper.createEamNameFilter());

//        datePickController = new DatePickController(this);
//        datePickController.setCycleDisable(true);
//        datePickController.setCanceledOnTouchOutside(true);
//        datePickController.setSecondVisible(false);
//        datePickController.textSize(18);
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

        refreshListController.setOnRefreshPageListener((page) -> {
            if (!queryParam.containsKey(Constant.BAPQuery.OPEN_TIME)) {
                queryParam.put(Constant.BAPQuery.OPEN_TIME, "2019-04-01 00:00:00");
            }
            if (!queryParam.containsKey(Constant.BAPQuery.ON_OR_OFF)) {
                queryParam.put(Constant.BAPQuery.ON_OR_OFF, "BEAM020/02");
            }
            presenterRouter.create(StopPoliceAPI.class).runningGatherList(queryParam, page);
        });
        listEamNameFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(Constant.BAPQuery.EAM_NAME, !((ScreenEntity) filterBean).name.equals("设备不限") ? ((ScreenEntity) filterBean).name : "");
            doRefresh();
        });
    }

    private void doRefresh() {
        refreshListController.refreshBegin();
    }

    @Override
    public void runningGatherListSuccess(StopPoliceListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void runningGatherListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {

        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {

        refreshListController.refreshBegin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
