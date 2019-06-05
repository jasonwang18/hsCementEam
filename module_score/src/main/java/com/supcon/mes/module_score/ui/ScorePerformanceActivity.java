package com.supcon.mes.module_score.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomTitleBar;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.IntentRouter;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.api.ScorePerformanceAPI;
import com.supcon.mes.module_score.model.bean.ScoreEntity;
import com.supcon.mes.module_score.model.bean.ScorePerformanceEntity;
import com.supcon.mes.module_score.model.contract.ScorePerformanceContract;
import com.supcon.mes.module_score.presenter.ScorePerformancePresenter;
import com.supcon.mes.module_score.ui.adapter.ScorePerformanceAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 设备评分
 */
@Router(value = Constant.Router.SCORE_PERFORMANCE)
@Presenter(value = ScorePerformancePresenter.class)
public class ScorePerformanceActivity extends BaseRefreshRecyclerActivity implements ScorePerformanceContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;

    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("scoreTitle")
    TextView scoreTitle;
    @BindByTag("eamCode")
    CustomVerticalTextView eamCode;
    @BindByTag("eamName")
    CustomVerticalTextView eamName;
    @BindByTag("eamDept")
    CustomVerticalTextView eamDept;
    @BindByTag("eamScore")
    CustomVerticalTextView eamScore;

    private ScorePerformanceAdapter scorePerformanceAdapter;
    private ScoreEntity scoreEntity;
    private int scoreId = -1;
    private boolean isEdit;
    private NFCHelper nfcHelper;

    @Override
    protected IListAdapter createAdapter() {
        scorePerformanceAdapter = new ScorePerformanceAdapter(this);
        return scorePerformanceAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_score_performance;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        scoreEntity = (ScoreEntity) getIntent().getSerializableExtra(Constant.IntentKey.SCORE_ENTITY);
        isEdit = getIntent().getBooleanExtra(Constant.IntentKey.isEdit, false);
        if (scoreEntity != null) {
            scoreId = scoreEntity.id;
        }
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
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        titleText.setText("评分绩效");
        scorePerformanceAdapter.setEditable(isEdit);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.drawable.sl_top_submit);
    }

    @Override
    protected void initData() {
        super.initData();
        Spanned title = HtmlParser.buildSpannedText(String.format(getString(R.string.device_style7), "红狮现场设备评分表",
                scoreEntity != null ? scoreEntity.scoreTableNo : ""), new HtmlTagHandler());
        scoreTitle.setText(title);
        if (scoreEntity != null) {
            eamCode.setContent(Util.strFormat(scoreEntity.getBeamId().code));
            eamName.setContent(Util.strFormat(scoreEntity.getBeamId().name));
            eamDept.setContent(Util.strFormat(scoreEntity.getBeamId().getUseDept().name));
            eamScore.setContent(Util.strFormat2(scoreEntity.scoreNum));
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(ScorePerformanceAPI.class).getScoreList(scoreId);
            }
        });
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onBackPressed();
                    }
                });
        eamCode.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                IntentRouter.go(ScorePerformanceActivity.this, Constant.Router.EAM);
            }
        });
        eamName.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                IntentRouter.go(ScorePerformanceActivity.this, Constant.Router.EAM);
            }
        });
    }

    /**
     * @param
     * @description NFC事件
     * @author zhangwenshuai1
     * @date 2018/6/28
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(NFCEvent nfcEvent) {
        if (isEdit) {
            LogUtil.d("NFC_TAG", nfcEvent.getNfc());
            Map<String, Object> nfcJson = GsonUtil.gsonToMaps(nfcEvent.getNfc());
            if (nfcJson.get("textRecord") == null) {
                ToastUtils.show(context, "标签内容空！");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(Constant.IntentKey.EAM_CODE, (String) nfcJson.get("textRecord"));
            IntentRouter.go(this, Constant.Router.EAM, bundle);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNFC(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity != null) {
            eamCode.setContent(Util.strFormat(commonSearchEvent.commonSearchEntity.getSearchId()));
            eamName.setContent(Util.strFormat(commonSearchEvent.commonSearchEntity.getSearchName()));
            eamDept.setContent(Util.strFormat(commonSearchEvent.commonSearchEntity.getSearchProperty()));
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void getScoreListSuccess(List entity) {
        Flowable.fromIterable(((List<ScorePerformanceEntity>) entity))
                .filter(scorePerformanceTitleEntity -> {
                    if (scorePerformanceTitleEntity.viewType == ListType.TITLE.value()) {
                        return true;
                    }
                    return false;
                })
                .subscribe(scorePerformanceTitleEntity -> {
                    if (scorePerformanceTitleEntity.scoreStandard.contains("设备运转率")) {
                        if (scoreEntity != null) {
                            scorePerformanceTitleEntity.setTotalScore(scoreEntity.operationRate);
                        }
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("高质量运行")) {
                        if (scoreEntity != null) {
                            scorePerformanceTitleEntity.setTotalScore(scoreEntity.highQualityOperation);
                        }
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("安全防护")) {
                        if (scoreEntity != null) {
                            scorePerformanceTitleEntity.setTotalScore(scoreEntity.security);
                        }
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("外观标识")) {
                        if (scoreEntity != null) {
                            scorePerformanceTitleEntity.setTotalScore(scoreEntity.appearanceLogo);
                        }
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("设备卫生")) {
                        if (scoreEntity != null) {
                            scorePerformanceTitleEntity.setTotalScore(scoreEntity.beamHeath);
                        }
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("档案管理")) {
                        if (scoreEntity != null) {
                            scorePerformanceTitleEntity.setTotalScore(scoreEntity.beamEstimate);
                        }
                    }
                    refreshListController.refreshComplete(entity);
                });
    }

    @Override
    public void getScoreListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
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
