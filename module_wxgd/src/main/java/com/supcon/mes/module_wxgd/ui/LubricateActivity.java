package com.supcon.mes.module_wxgd.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.listener.OnTitleSearchExpandListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.LubricateOil;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.api.LubricateAPI;
import com.supcon.mes.module_wxgd.model.bean.LubricateListEntity;
import com.supcon.mes.module_wxgd.model.contract.LubricateContract;
import com.supcon.mes.module_wxgd.presenter.LubricatePresenter;
import com.supcon.mes.module_wxgd.ui.adapter.LubricateAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName LubricateActivity
 * @date 2018/9/5
 * ------------- Description -------------
 * 润滑油选择
 */
@Presenter(LubricatePresenter.class)
@Router(Constant.Router.LUBRICATE)
public class LubricateActivity extends BaseRefreshRecyclerActivity<LubricateOil> implements LubricateContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customSearchView")
    CustomSearchView customSearchView;


    private LubricateAdapter lubricateAdapter;
    private Map<String, Object> queryParam = new HashMap<>();

    @Override
    protected IListAdapter createAdapter() {
        lubricateAdapter = new LubricateAdapter(this);
        return lubricateAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_recycle;
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitleBar.setTitleText("润滑油选择列表");
        searchTitleBar.disableRightBtn();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setLoadMoreEnable(true);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));
        contentView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "列表为空"));
        customSearchView.setHint("搜索");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> {
            back();
        });
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(LubricateAPI.class).listLubricate(pageIndex, queryParam);
            }
        });
        lubricateAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                LubricateOil lubricateOil = lubricateAdapter.getItem(position);
                EventBus.getDefault().post(lubricateOil);
                LubricateActivity.this.finish();
            }
        });
        searchTitleBar.setOnExpandListener(new OnTitleSearchExpandListener() {
            @Override
            public void onTitleSearchExpand(boolean isExpand) {
                if (isExpand) {
                    customSearchView.setHint("润滑油名称或规格");
                    customSearchView.setInputTextColor(R.color.hintColor);
                } else {
                    customSearchView.setHint("搜索");
                    customSearchView.setInputTextColor(R.color.black);
                }
            }
        });
        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        doSearch(charSequence.toString().trim());
                    }
                });
    }

    /**
     * @param
     * @return
     * @description 搜索
     * @author zhangwenshuai1 2018/9/19
     */
    private void doSearch(String searchContent) {
        if (queryParam.containsKey(Constant.BAPQuery.NAME)){
            queryParam.remove(Constant.BAPQuery.NAME);
        }
        if (queryParam.containsKey(Constant.BAPQuery.SPECIFY)){
            queryParam.remove(Constant.BAPQuery.SPECIFY);
        }
        if (Util.isContainChinese(searchContent)){
            queryParam.put(Constant.BAPQuery.NAME,searchContent);
        }else {
            queryParam.put(Constant.BAPQuery.SPECIFY,searchContent);
        }
        refreshListController.refreshBegin();
    }

    @Override
    public void listLubricateSuccess(LubricateListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listLubricateFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete();
    }
}
