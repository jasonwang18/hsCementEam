package com.supcon.mes.module_warn.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.api.MaintenanceWarnAPI;
import com.supcon.mes.module_warn.model.bean.MaintenanceWarnEntity;
import com.supcon.mes.module_warn.model.bean.MaintenanceWarnListEntity;
import com.supcon.mes.module_warn.model.contract.MaintenanceWarnContract;
import com.supcon.mes.module_warn.presenter.MaintenanceWarnPresenter;
import com.supcon.mes.module_warn.ui.adapter.MaintenanceWarnAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 */
@Router(Constant.Router.MAINTENANCE_EARLY_WARN)
@Presenter(value = MaintenanceWarnPresenter.class)
public class MaintenanceWarnActivity extends BaseRefreshRecyclerActivity<MaintenanceWarnEntity> implements MaintenanceWarnContract.View {

    @BindByTag("leftBtn")
    AppCompatImageButton leftBtn;

    @BindByTag("customSearchView")
    CustomSearchView titleSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("warnRadioGroup")
    RadioGroup warnRadioGroup;

    @BindByTag("btnLayout")
    LinearLayout btnLayout;

    private String url;

    private final Map<String, Object> queryParam = new HashMap<>();
    private String selecStr;

    @Override
    protected IListAdapter<MaintenanceWarnEntity> createAdapter() {
        MaintenanceWarnAdapter maintenanceWarnAdapter = new MaintenanceWarnAdapter(this);
        return maintenanceWarnAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_early_warn_list;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(15));
        //设置搜索框默认提示语
        titleSearchView.setHint("请输入设备编码");
        searchTitleBar.setTitleText("维保预警");
        searchTitleBar.setBackgroundResource(R.color.gradient_start);
        searchTitleBar.disableRightBtn();
    }

    @Override
    protected void initData() {
        super.initData();
        url = "/BEAM/baseInfo/jWXItem/data-dg1531171100751.action";
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            if (queryParam.containsKey(Constant.BAPQuery.EAM_CODE)) {
                queryParam.remove(Constant.BAPQuery.EAM_CODE);
            }
            if (!TextUtils.isEmpty(selecStr)) {
                queryParam.put(Constant.BAPQuery.EAM_CODE, selecStr);
            }
            setRadioEnable(false);
            presenterRouter.create(MaintenanceWarnAPI.class).getMaintenance(url, queryParam, pageIndex);
        });
        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        doSearchTableNo(charSequence.toString());
                    }
                });
        KeyExpandHelper.doActionSearch(titleSearchView.editText(), true, () ->
                doSearchTableNo(titleSearchView.getInput()));

        leftBtn.setOnClickListener(v -> onBackPressed());


        warnRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.warnRadioBtn1) {
                    url = "/BEAM/baseInfo/jWXItem/data-dg1531171100751.action";
                } else if (checkedId == R.id.warnRadioBtn2) {
                    url = "/BEAM/baseInfo/jWXItem/data-dg1531171100814.action";
                }
                Flowable.timer(500, TimeUnit.MILLISECONDS)
                        .subscribe(aLong -> doRefresh());
            }
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

    @Override
    public void getMaintenanceSuccess(MaintenanceWarnListEntity entity) {
        if (entity.pageNo == 1 && entity.result.size() <= 0) {
            btnLayout.setVisibility(View.GONE);
        } else {
            btnLayout.setVisibility(View.VISIBLE);
        }
        refreshListController.refreshComplete(entity.result);
        setRadioEnable(true);
    }

    @Override
    public void getMaintenanceFailed(String errorMsg) {
        btnLayout.setVisibility(View.GONE);
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
        setRadioEnable(true);
    }

    public void setRadioEnable(boolean enable) {
        for (int i = 0; i < warnRadioGroup.getChildCount(); i++) {
            warnRadioGroup.getChildAt(i).setEnabled(enable);
        }
    }
}
