package com.supcon.mes.module_score;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomTitleBar;
import com.supcon.mes.middleware.constant.Constant;

@Router(value = Constant.Router.SCORE_PERFORMANCE)
public class ScorePerformanceActivity extends BaseRefreshActivity {
    @BindByTag("titleBar")
    CustomTitleBar titleBar;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_score_performance;
    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(true);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshController.refreshComplete();
            }
        });
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
}
