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
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomTitleBar;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.NFCEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.IntentRouter;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.api.ScorePerformanceAPI;
import com.supcon.mes.module_score.model.api.ScoreSubmitAPI;
import com.supcon.mes.module_score.model.bean.ScoreEntity;
import com.supcon.mes.module_score.model.bean.ScorePerformanceEntity;
import com.supcon.mes.module_score.model.contract.ScorePerformanceContract;
import com.supcon.mes.module_score.model.contract.ScoreSubmitContract;
import com.supcon.mes.module_score.presenter.ScorePerformancePresenter;
import com.supcon.mes.module_score.presenter.ScoreSubmitPresenter;
import com.supcon.mes.module_score.ui.adapter.ScorePerformanceAdapter;
import com.supcon.mes.module_score.ui.util.ScoreMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
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
@Presenter(value = {ScorePerformancePresenter.class, ScoreSubmitPresenter.class})
public class ScorePerformanceActivity extends BaseRefreshRecyclerActivity implements ScorePerformanceContract.View, ScoreSubmitContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("eamCode")
    CustomVerticalTextView eamCode;
    @BindByTag("eamName")
    CustomVerticalTextView eamName;
    @BindByTag("eamDept")
    CustomVerticalTextView eamDept;
    @BindByTag("eamScore")
    CustomVerticalTextView eamScore;
    @BindByTag("scoreStaff")
    CustomVerticalTextView scoreStaff;
    @BindByTag("scoreTime")
    CustomVerticalTextView scoreTime;

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
        scorePerformanceAdapter.setEditable(isEdit);
        rightBtn.setImageResource(R.drawable.sl_top_submit);
        eamCode.setEditable(isEdit);
        eamName.setEditable(isEdit);
        if (isEdit) {
            rightBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        Spanned title = HtmlParser.buildSpannedText(String.format(getString(R.string.device_style7), "红狮设备评分表",
                scoreEntity != null ? "(" + scoreEntity.scoreTableNo + ")" : ""), new HtmlTagHandler());
        titleText.setText(title);
        if (scoreEntity != null) {
            eamCode.setContent(Util.strFormat(scoreEntity.getBeamId().code));
            eamName.setContent(Util.strFormat(scoreEntity.getBeamId().name));
            eamDept.setContent(Util.strFormat(scoreEntity.getBeamId().getUseDept().name));
            eamScore.setContent(Util.strFormat2(scoreEntity.scoreNum));
        } else {
            scoreEntity = new ScoreEntity();
            scoreEntity.scoreStaff = new Staff();
            scoreEntity.scoreStaff.name = EamApplication.getAccountInfo().staffName;
            scoreEntity.scoreStaff.code = EamApplication.getAccountInfo().staffCode;
            scoreEntity.scoreStaff.id = EamApplication.getAccountInfo().staffId;
        }
        scoreEntity.scoreTime = (scoreEntity != null && scoreEntity.scoreTime != null) ? scoreEntity.scoreTime : getYesterday();

        scoreStaff.setContent(scoreEntity.getScoreStaff().name);
        scoreTime.setContent(DateUtil.dateFormat((scoreEntity != null && scoreEntity.scoreTime != null) ? scoreEntity.scoreTime : getYesterday()));
        scorePerformanceAdapter.updateTotal(scoreEntity.scoreNum);
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

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> new CustomDialog(context)
                        .twoButtonAlertDialog("确定提交设备评分吗?")
                        .bindView(R.id.redBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, v12 -> {
                            if (scoreEntity.beamId == null) {
                                ToastUtils.show(ScorePerformanceActivity.this, "请选择设备进行评分!");
                                return;
                            }
                            onLoading("正在处理中...");
                            doSubmit();
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show());
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
        scorePerformanceAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                ScorePerformanceEntity scorePerformanceEntity = (ScorePerformanceEntity) obj;
                scoreEntity.scoreNum = scorePerformanceEntity.scoreNum;
                eamScore.setContent(Util.strFormat2(scoreEntity.scoreNum));
                scorePerformanceAdapter.updateTotal(scoreEntity.scoreNum);
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
            EamType eamType = (EamType) commonSearchEvent.commonSearchEntity;
            eamCode.setContent(Util.strFormat(commonSearchEvent.commonSearchEntity.getSearchId()));
            eamName.setContent(Util.strFormat(commonSearchEvent.commonSearchEntity.getSearchName()));
            eamDept.setContent(Util.strFormat(commonSearchEvent.commonSearchEntity.getSearchProperty()));
            scoreEntity.beamId = eamType;
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
                        scorePerformanceTitleEntity.setTotalScore(scoreEntity.operationRate);
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("高质量运行")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEntity.highQualityOperation);
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("安全防护")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEntity.security);
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("外观标识")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEntity.appearanceLogo);
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("设备卫生")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEntity.beamHeath);
                    } else if (scorePerformanceTitleEntity.scoreStandard.contains("档案管理")) {
                        scorePerformanceTitleEntity.setTotalScore(scoreEntity.beamEstimate);
                    }
                    refreshListController.refreshComplete(entity);
                });
    }

    @Override
    public void getScoreListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    private void doSubmit() {
        ScoreMapManager.dataChange(scorePerformanceAdapter.getList(), scoreEntity);
        Map map = ScoreMapManager.createMap(scoreEntity);

        List list1 = ScoreMapManager.dataChange(scorePerformanceAdapter.getList(), "设备运转率");
        map.put("dg1559647635248ModelCode", "BEAM_1.0.0_scorePerformance_OperateRate");
        map.put("dg1559647635248ListJson", list1.toString());
        map.put("dgLists['dg1559647635248']", list1.toString());

        List list2 = ScoreMapManager.dataChange(scorePerformanceAdapter.getList(), "高质量运行");
        map.put("dg1559291491380ModelCode", "BEAM_1.0.0_scorePerformance_HighQualityRun");
        map.put("dg1559291491380ListJson", list2.toString());
        map.put("dgLists['dg1559291491380']", list2.toString());

        List list3 = ScoreMapManager.dataChange(scorePerformanceAdapter.getList(), "安全防护");
        map.put("dg1559560003995ModelCode", "BEAM_1.0.0_scorePerformance_Security");
        map.put("dg1559560003995ListJson", list3.toString());
        map.put("dgLists['dg1559560003995']", list3.toString());

        List list4 = ScoreMapManager.dataChange(scorePerformanceAdapter.getList(), "外观标识");
        map.put("dg1559619724464ModelCode", "BEAM_1.0.0_scorePerformance_OutwardMark");
        map.put("dg1559619724464ListJson", list4.toString());
        map.put("dgLists['dg1559619724464']", list4.toString());

        List list5 = ScoreMapManager.dataChange(scorePerformanceAdapter.getList(), "设备卫生");
        map.put("dg1559631064719ModelCode", "BEAM_1.0.0_scorePerformance_Health");
        map.put("dg1559631064719ListJson", list5.toString());
        map.put("dgLists['dg1559631064719']", list5.toString());

        List list6 = ScoreMapManager.dataChange(scorePerformanceAdapter.getList(), "档案管理");
        map.put("dg1559636766020ModelCode", "BEAM_1.0.0_scorePerformance_OutwardMark");
        map.put("dg1559636766020ListJson", list6.toString());
        map.put("dgLists['dg1559636766020']", list6.toString());

        map.put("operateType", Constant.Transition.SUBMIT);
        presenterRouter.create(ScoreSubmitAPI.class).doSubmit(map);
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

    public long getYesterday() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_MONTH, -1);
        return ca.getTimeInMillis();
    }

    @Override
    public void doSubmitSuccess(BapResultEntity entity) {
        onLoadSuccessAndExit("评分成功", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                EventBus.getDefault().post(new RefreshEvent());
                finish();
            }
        });
    }

    @Override
    public void doSubmitFailed(String errorMsg) {
        if ("本数据已经被其他人修改或删除，请刷页面后重试".equals(errorMsg)) {
            loaderController.showMsgAndclose(ErrorMsgHelper.msgParse(errorMsg), false, 5000);
        } else if (errorMsg.contains("com.supcon.orchid.BEAM2.entities.BEAM2SparePart")) {
            //error : Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [com.supcon.orchid.BEAM2.entities.BEAM2SparePart#1211]
            loaderController.showMsgAndclose("请刷新备件表体数据！", false, 4000);
        } else {
            onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
        }
    }
}
