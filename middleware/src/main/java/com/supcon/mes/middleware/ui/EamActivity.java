package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.EamAPI;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.contract.EamContract;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.presenter.EamPresenter;
import com.supcon.mes.middleware.ui.adapter.BaseSearchAdapter;
import com.supcon.mes.middleware.ui.view.PinyinSearchBar;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Router(value = Constant.Router.EAM)
@Presenter(value = EamPresenter.class)
public class EamActivity extends BaseRefreshRecyclerActivity<CommonSearchEntity> implements EamContract.View {
    @BindByTag("contentView")
    RecyclerView recyclerView;

    /**
     * 顶部模糊搜索栏
     */
    CustomSearchView titleSearchView;
    /**
     * 顶部搜索控件
     */
    @BindByTag("titleBar")
    CustomHorizontalSearchTitleBar titleBar;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    /**
     * 侧栏首字母导航控件
     */
    @BindByTag("pinyinSearchBar")
    PinyinSearchBar pinyinSearchBar;

    private BaseSearchAdapter mBaseSearchAdapter;

    private final Map<String, Object> queryParam = new HashMap<>();
    private String eamCode;

    @Override
    protected IListAdapter<CommonSearchEntity> createAdapter() {
        mBaseSearchAdapter = new BaseSearchAdapter(this, false);
        return mBaseSearchAdapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "没有信息哦~"));
        eamCode = getIntent().getStringExtra(Constant.IntentKey.EAM_CODE);
    }


    @Override
    protected int getLayoutID() {
        return R.layout.ac_sbda_search_contact;
    }

    @Override
    protected void initView() {
        super.initView();
        //设置状态栏背景色
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        titleSearchView = titleBar.searchView();
        titleSearchView.setHint("请输入搜索信息");

        titleBar.setTitleText("设备搜索");
        titleBar.disableRightBtn();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));
        recyclerView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(this));
        recyclerView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        recyclerView.setScrollBarSize(2);
        titleSearchView.setInput(eamCode);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        mBaseSearchAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            CommonSearchEntity commonSearchEntity = (CommonSearchEntity) obj;
            CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
            commonSearchEvent.commonSearchEntity = commonSearchEntity;
            finish();
            EventBus.getDefault().post(commonSearchEvent);
        });

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                final String blurMes = titleSearchView.editText().getText().toString().trim();
                if (queryParam.containsKey(Constant.BAPQuery.EAM_CODE)) {
                    queryParam.remove(Constant.BAPQuery.EAM_CODE);
                }
                if (!TextUtils.isEmpty(blurMes)) {
                    queryParam.put(Constant.BAPQuery.EAM_CODE, blurMes);
                }
                presenterRouter.create(EamAPI.class).getEam(queryParam, pageIndex);
            }
        });
        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(charSequence -> {
                    mBaseSearchAdapter.clear();
                    refreshListController.refreshBegin();
                });

        leftBtn.setOnClickListener(v -> back());

        pinyinSearchBar.setOnWordsChangeListener(word -> {
            final int pos = mBaseSearchAdapter.getPos(word);
            if (pos != -1) {
                recyclerView.scrollToPosition(pos);
            }
        });

        titleBar.setOnExpandListener(isExpand -> {
            if (isExpand) {
                titleSearchView.setInputTextColor(R.color.hintColor);
            } else {
                titleSearchView.setInputTextColor(R.color.black);
            }
        });
    }

    @Override
    public void getEamSuccess(CommonListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getEamFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        ToastUtils.show(context, errorMsg);
    }
}
