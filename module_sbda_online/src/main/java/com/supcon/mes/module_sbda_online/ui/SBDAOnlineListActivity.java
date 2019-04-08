package com.supcon.mes.module_sbda_online.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda_online.IntentRouter;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.api.SBDAOnlineListAPI;
import com.supcon.mes.module_sbda_online.model.api.ScreenAPI;
import com.supcon.mes.module_sbda_online.model.bean.SBDAOnlineEntity;
import com.supcon.mes.module_sbda_online.model.bean.SBDAOnlineListEntity;
import com.supcon.mes.module_sbda_online.model.bean.ScreenEntity;
import com.supcon.mes.module_sbda_online.model.contract.SBDAOnlineListContract;
import com.supcon.mes.module_sbda_online.presenter.SBDAOnlineListPresenter;
import com.supcon.mes.module_sbda_online.presenter.ScreenPresenter;
import com.supcon.mes.module_sbda_online.screen.FilterHelper;
import com.supcon.mes.module_sbda_online.ui.adapter.SBDAOnlineListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.supcon.mes.module_sbda_online.presenter.ScreenPresenter.AREA;
import static com.supcon.mes.module_sbda_online.presenter.ScreenPresenter.TYPE;


/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/3/30.
 */
@Router(Constant.Router.SBDA_ONLINE_LIST)
@Presenter(value = {SBDAOnlineListPresenter.class, ScreenPresenter.class})
public class SBDAOnlineListActivity extends BaseRefreshRecyclerActivity<SBDAOnlineEntity> implements SBDAOnlineListContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("customSearchView")
    CustomSearchView titleSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;

    @BindByTag("listTypeFilter")
    CustomFilterView listTypeFilter;

    @BindByTag("listAreaFilter")
    CustomFilterView listAreaFilter;

    @BindByTag("listStatusFilter")
    CustomFilterView listStatusFilter;

    private final Map<String, Object> queryParam = new HashMap<>();

    private SBDAOnlineListAdapter mSBDAListAdapter;
    private String selecStr;

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        //设置搜索框默认提示语
        titleSearchView.setHint("请输入设备编码");
        searchTitleBar.setBackgroundResource(R.color.gradient_start);
        searchTitleBar.disableRightBtn();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(5));

        initFilterView();
    }

    private void initFilterView() {
        presenterRouter.create(ScreenAPI.class).screenPart(listAreaFilter, AREA);
        presenterRouter.create(ScreenAPI.class).screenPart(listTypeFilter, TYPE);
        listStatusFilter.setData(FilterHelper.createStateFilter());

    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sbda_online_list;
    }

    @Override
    public void getSearchSBDASuccess(SBDAOnlineListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getSearchSBDAFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }

    @Override
    protected IListAdapter<SBDAOnlineEntity> createAdapter() {
        mSBDAListAdapter = new SBDAOnlineListAdapter(this);
        return mSBDAListAdapter;
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshPageListener((page) -> {

            if (queryParam.containsKey(Constant.BAPQuery.EAM_CODE)) {
                queryParam.remove(Constant.BAPQuery.EAM_CODE);
                if (TextUtils.isEmpty(selecStr)) {
                    refreshListController.refreshBegin();
                }
            }
            if (!TextUtils.isEmpty(selecStr)) {
                queryParam.put(Constant.BAPQuery.EAM_CODE, selecStr);
                refreshListController.refreshBegin();

            }
            presenterRouter.create(SBDAOnlineListAPI.class).getSearchSBDA(queryParam, page);
        });
        //点击触发事件跳转界面
        mSBDAListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            SBDAOnlineEntity sbdaOnlineEntity = (SBDAOnlineEntity) obj;
            Bundle bundle = new Bundle();
            bundle.putLong(Constant.IntentKey.SBDA_ONLINE_EAMID, sbdaOnlineEntity.id);
            bundle.putString(Constant.IntentKey.SBDA_ONLINE_EAMCODE, sbdaOnlineEntity.code);
            IntentRouter.go(context, Constant.Router.SBDA_ONLINE_VIEW, bundle);
        });
        leftBtn.setOnClickListener(v -> onBackPressed());

        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        doSearchTableNo(charSequence.toString());
                    }
                });
        KeyExpandHelper.doActionSearch(titleSearchView.editText(), true, () ->
                doSearchTableNo(titleSearchView.getInput()));

        listTypeFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(Constant.BAPQuery.EAM_TYPE, ((ScreenEntity) filterBean).code != null ? ((ScreenEntity) filterBean).code : "");
            doRefresh();
        });
        listStatusFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(Constant.BAPQuery.EAM_STATE, ((ScreenEntity) filterBean).layRec != null ? ((ScreenEntity) filterBean).layRec : "");
            doRefresh();
        });
        listAreaFilter.setFilterSelectChangedListener(filterBean -> {
            queryParam.put(Constant.BAPQuery.EAM_AREA, ((ScreenEntity) filterBean).layRec != null ? ((ScreenEntity) filterBean).layRec : "");
            doRefresh();
        });

    }

    public void doSearchTableNo(String search) {
        selecStr = search;
        refreshListController.refreshBegin();
    }

    /**
     * 进行过滤查询
     */
    private void doRefresh() {
        refreshListController.refreshBegin();
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
