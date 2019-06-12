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
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
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
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
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
import com.supcon.mes.module_score.model.api.ScoreEamPerformanceAPI;
import com.supcon.mes.module_score.model.api.ScoreStaffPerformanceAPI;
import com.supcon.mes.module_score.model.api.ScoreSubmitAPI;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;
import com.supcon.mes.module_score.model.contract.ScoreEamPerformanceContract;
import com.supcon.mes.module_score.model.contract.ScoreStaffPerformanceContract;
import com.supcon.mes.module_score.model.contract.ScoreSubmitContract;
import com.supcon.mes.module_score.presenter.ScoreEamPerformancePresenter;
import com.supcon.mes.module_score.presenter.ScoreStaffPerformancePresenter;
import com.supcon.mes.module_score.presenter.ScoreSubmitPresenter;
import com.supcon.mes.module_score.ui.adapter.ScoreEamPerformanceAdapter;
import com.supcon.mes.module_score.ui.adapter.ScoreStaffEamAdapter;
import com.supcon.mes.module_score.ui.adapter.ScoreStaffPerformanceAdapter;
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
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/29
 * ------------- Description -------------
 * 个人评分
 */
@Router(value = Constant.Router.SCORE_STAFF_PERFORMANCE)
@Presenter(value = {ScoreStaffPerformancePresenter.class, ScoreSubmitPresenter.class})
public class ScoreStaffPerformanceActivity extends BaseRefreshActivity implements ScoreStaffPerformanceContract.View, ScoreSubmitContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;

    @BindByTag("recyclerView")
    RecyclerView recyclerView;

    @BindByTag("recyclerViewEam")
    RecyclerView recyclerViewEam;

    @BindByTag("scoreStaff")
    CustomVerticalTextView scoreStaff;
    @BindByTag("staffScore")
    CustomVerticalTextView staffScore;
    @BindByTag("scoreTime")
    CustomTextView scoreTime;

    private ScoreStaffPerformanceAdapter scoreStaffPerformanceAdapter;
    private ScoreStaffEntity scoreStaffEntity;
    private int scoreId = -1;
    private boolean isEdit;
    private ScoreStaffEamAdapter scoreStaffEamAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_score_staff_performance;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        scoreStaffEntity = (ScoreStaffEntity) getIntent().getSerializableExtra(Constant.IntentKey.SCORE_ENTITY);
        isEdit = getIntent().getBooleanExtra(Constant.IntentKey.isEdit, false);
        if (scoreStaffEntity != null) {
            scoreId = scoreStaffEntity.id;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewEam.setLayoutManager(new LinearLayoutManager(context));
        scoreStaffPerformanceAdapter = new ScoreStaffPerformanceAdapter(this);
        scoreStaffEamAdapter = new ScoreStaffEamAdapter(this);
        recyclerView.setAdapter(scoreStaffPerformanceAdapter);
        recyclerViewEam.setAdapter(scoreStaffEamAdapter);
        scoreStaffPerformanceAdapter.setEditable(isEdit);

        rightBtn.setImageResource(R.drawable.sl_top_submit);
        scoreStaff.setEditable(isEdit);
        if (isEdit) {
            rightBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        Spanned title = HtmlParser.buildSpannedText(String.format(getString(R.string.device_style7), "巡检工评分表", ""), new HtmlTagHandler());
        titleText.setText(title);
        if (scoreStaffEntity == null) {
            scoreStaffEntity = new ScoreStaffEntity();
            scoreStaffEntity.patrolWorker = new Staff();
            scoreStaffEntity.patrolWorker.name = EamApplication.getAccountInfo().staffName;
            scoreStaffEntity.patrolWorker.code = EamApplication.getAccountInfo().staffCode;
            scoreStaffEntity.patrolWorker.id = EamApplication.getAccountInfo().staffId;
        }
        scoreStaff.setContent(scoreStaffEntity.getPatrolWorker().name);
        staffScore.setContent(Util.strFormat2(scoreStaffEntity.score));
        scoreStaffPerformanceAdapter.updateTotal(scoreStaffEntity.score);
        scoreStaffEntity.scoreData = (scoreStaffEntity.scoreData != null) ? scoreStaffEntity.scoreData : System.currentTimeMillis();
        scoreTime.setContent(DateUtil.dateFormat(scoreStaffEntity.scoreData));

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(ScoreStaffPerformanceAPI.class).getStaffScore(scoreId);
                presenterRouter.create(ScoreStaffPerformanceAPI.class).getDutyEam(scoreStaffEntity.getPatrolWorker().id);
            }
        });
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        RxView.clicks(rightBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> new CustomDialog(context)
                        .twoButtonAlertDialog("确定提交设备评分吗?")
                        .bindView(R.id.redBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, v12 -> {
                            if (scoreStaffEntity.patrolWorker == null) {
                                ToastUtils.show(this, "请选择巡检工!");
                                return;
                            }
                            onLoading("正在处理中...");
                            doSubmit();
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show());

        scoreStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    scoreStaffEntity.patrolWorker = null;
                }
                IntentRouter.go(ScoreStaffPerformanceActivity.this, Constant.Router.STAFF);
            }
        });

        scoreStaffPerformanceAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                ScoreStaffPerformanceEntity scoreStaffPerformanceEntity = (ScoreStaffPerformanceEntity) obj;
                scoreStaffEntity.score = scoreStaffPerformanceEntity.scoreNum;
                staffScore.setContent(Util.strFormat2(scoreStaffEntity.score));
                scoreStaffPerformanceAdapter.updateTotal(scoreStaffEntity.score);
            }
        });
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity != null) {
            if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff) {
                CommonSearchStaff searchStaff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
                scoreStaff.setContent(Util.strFormat(searchStaff.name));
                Staff staff = new Staff();
                staff.id = searchStaff.id;
                staff.name = searchStaff.name;
                staff.code = searchStaff.code;
                scoreStaffEntity.patrolWorker = staff;
                refreshController.refreshBegin();
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void getStaffScoreSuccess(List entity) {
        if (entity.size() > 0) {
            scoreStaffPerformanceAdapter.setList(entity);
            scoreStaffPerformanceAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setAdapter((BaseListDataRecyclerViewAdapter) EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        }
        refreshController.refreshComplete();
    }

    @Override
    public void getStaffScoreFailed(String errorMsg) {
        refreshController.refreshComplete();
        SnackbarHelper.showError(rootView, errorMsg);
        recyclerView.setAdapter((BaseListDataRecyclerViewAdapter) EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
    }

    @SuppressLint("CheckResult")
    @Override
    public void getDutyEamSuccess(CommonListEntity entity) {
        if (entity.result != null && entity.result.size() > 0) {
            ScoreDutyEamEntity scoreDutyEamEntity = (ScoreDutyEamEntity) entity.result.get(0);
            ScoreDutyEamEntity scoreDutyEamEntityTitle = new ScoreDutyEamEntity();
            scoreDutyEamEntityTitle.name = "现场管理";
            scoreDutyEamEntityTitle.avgScore = scoreDutyEamEntity.avgScore;
            scoreDutyEamEntityTitle.viewType = ListType.TITLE.value();
            entity.result.add(0, scoreDutyEamEntityTitle);
            scoreStaffEamAdapter.setList(entity.result);
            scoreStaffEamAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getDutyEamFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
    }

    private void doSubmit() {
//        ScoreMapManager.dataChange(scoreStaffPerformanceAdapter.getList(), scoreEamEntity);
//        Map map = ScoreMapManager.createMap(scoreEamEntity);
//
//        List list1 = ScoreMapManager.dataChange(scoreStaffPerformanceAdapter.getList(), "设备运转率");
//        map.put("dg1559647635248ModelCode", "BEAM_1.0.0_scorePerformance_OperateRate");
//        map.put("dg1559647635248ListJson", list1.toString());
//        map.put("dgLists['dg1559647635248']", list1.toString());
//
//        List list2 = ScoreMapManager.dataChange(scoreStaffPerformanceAdapter.getList(), "高质量运行");
//        map.put("dg1559291491380ModelCode", "BEAM_1.0.0_scorePerformance_HighQualityRun");
//        map.put("dg1559291491380ListJson", list2.toString());
//        map.put("dgLists['dg1559291491380']", list2.toString());
//
//        List list3 = ScoreMapManager.dataChange(scoreStaffPerformanceAdapter.getList(), "安全防护");
//        map.put("dg1559560003995ModelCode", "BEAM_1.0.0_scorePerformance_Security");
//        map.put("dg1559560003995ListJson", list3.toString());
//        map.put("dgLists['dg1559560003995']", list3.toString());
//
//        List list4 = ScoreMapManager.dataChange(scoreStaffPerformanceAdapter.getList(), "外观标识");
//        map.put("dg1559619724464ModelCode", "BEAM_1.0.0_scorePerformance_OutwardMark");
//        map.put("dg1559619724464ListJson", list4.toString());
//        map.put("dgLists['dg1559619724464']", list4.toString());
//
//        List list5 = ScoreMapManager.dataChange(scoreStaffPerformanceAdapter.getList(), "设备卫生");
//        map.put("dg1559631064719ModelCode", "BEAM_1.0.0_scorePerformance_Health");
//        map.put("dg1559631064719ListJson", list5.toString());
//        map.put("dgLists['dg1559631064719']", list5.toString());
//
//        List list6 = ScoreMapManager.dataChange(scoreStaffPerformanceAdapter.getList(), "档案管理");
//        map.put("dg1559636766020ModelCode", "BEAM_1.0.0_scorePerformance_OutwardMark");
//        map.put("dg1559636766020ListJson", list6.toString());
//        map.put("dgLists['dg1559636766020']", list6.toString());

//        map.put("operateType", Constant.Transition.SUBMIT);
//        presenterRouter.create(ScoreSubmitAPI.class).doSubmit(map);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
