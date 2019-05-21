package com.supcon.mes.module_wxgd.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.RoleController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.controller.AcceptanceCheckController;
import com.supcon.mes.module_wxgd.controller.LubricateOilsController;
import com.supcon.mes.module_wxgd.controller.RepairStaffController;
import com.supcon.mes.module_wxgd.controller.SparePartController;
import com.supcon.mes.module_wxgd.controller.WXGDSubmitController;
import com.supcon.mes.module_wxgd.model.api.WXGDListAPI;
import com.supcon.mes.module_wxgd.model.api.WXGDStopOrActivateAPI;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDListContract;
import com.supcon.mes.module_wxgd.model.contract.WXGDStopOrActivateContract;
import com.supcon.mes.module_wxgd.model.dto.LubricateOilsEntityDto;
import com.supcon.mes.module_wxgd.model.dto.RepairStaffDto;
import com.supcon.mes.module_wxgd.model.dto.SparePartEntityDto;
import com.supcon.mes.module_wxgd.model.event.ListEvent;
import com.supcon.mes.module_wxgd.model.event.LubricateOilsEvent;
import com.supcon.mes.module_wxgd.model.event.RepairStaffEvent;
import com.supcon.mes.module_wxgd.model.event.SparePartEvent;
import com.supcon.mes.module_wxgd.model.event.VersionRefreshEvent;
import com.supcon.mes.module_wxgd.presenter.WXGDListPresenter;
import com.supcon.mes.module_wxgd.presenter.WXGDStopOrActivatePresenter;
import com.supcon.mes.module_wxgd.util.WXGDMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/5
 * ------------- Description -------------
 * 待执行
 */
@Router(Constant.Router.WXGD_EXECUTE)
@Presenter(value = {WXGDStopOrActivatePresenter.class, WXGDListPresenter.class})
@Controller(value = {SparePartController.class,RepairStaffController.class,LubricateOilsController.class,AcceptanceCheckController.class})
public class WXGDExecuteActivity extends BaseRefreshActivity implements WXGDSubmitController.OnSubmitResultListener, WXGDStopOrActivateContract.View,WXGDListContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("workSource")
    TextView workSource;

    ImageView eamIc;
    @BindByTag("eamName")
    CustomTextView eamName;
    @BindByTag("eamCode")
    CustomTextView eamCode;
    @BindByTag("eamArea")
    CustomTextView eamArea;

    @BindByTag("faultInfoType")
    CustomVerticalTextView faultInfoType;
    @BindByTag("repairType")
    CustomVerticalTextView repairType;
    @BindByTag("priority")
    CustomTextView priority;
    @BindByTag("faultInfoDescribe")
    CustomVerticalTextView faultInfoDescribe;
    @BindByTag("faultInfo")
    LinearLayout faultInfo;
    @BindByTag("noFaultInfo")
    LinearLayout noFaultInfo;

    @BindByTag("content")
    CustomTextView content;
    @BindByTag("claim")
    CustomTextView claim;
    @BindByTag("period")
    CustomTextView period;
    @BindByTag("periodUnit")
    CustomTextView periodUnit;
    @BindByTag("lastExecuteTime")
    CustomVerticalDateView lastExecuteTime;
    @BindByTag("nextExecuteTime")
    CustomTextView nextExecuteTime;
    @BindByTag("lastDuration")
    CustomVerticalDateView lastDuration;
    @BindByTag("realEndDate")
    CustomVerticalDateView realEndDate;

    @BindByTag("repairGroup")
    CustomVerticalTextView repairGroup;
    @BindByTag("chargeStaff")
    CustomVerticalTextView chargeStaff;
    @BindByTag("planStartTime")
    CustomVerticalDateView planStartTime;
    @BindByTag("planEndTime")
    CustomVerticalDateView planEndTime;
    @BindByTag("acceptanceCheckListWidget")
    CustomListWidget<AcceptanceCheckEntity> acceptanceCheckListWidget;
    @BindByTag("commentInput")
    CustomEditText commentInput;
    @BindByTag("transition")
    CustomWorkFlowView transition;

    @BindByTag("stop")
    Button stop;
    @BindByTag("activate")
    Button activate;

    private RepairStaffController mRepairStaffController;
    private SparePartController mSparePartController;
    private LubricateOilsController mLubricateOilsController;
    private AcceptanceCheckController mAcceptanceCheckController;

    private WXGDEntity mWXGDEntity;//传入维修工单实体参数
    private LinkController mLinkController;
    private DatePickController mDatePickController;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private RoleController roleController;
    private String tip;
    private WXGDSubmitController wxgdSubmitController;
    private boolean isActivate = true;//当前是否是激活状态 true:激活
    private boolean saveAndActivate = false; //保存或激活状态切换进行的数据保存 true:是
    private boolean isActionSop = false;//暂停或激活  true:暂停
    private String reason;
    //表体修改前的列表数据
    private String lubricateOilsListStr;
    private String repairStaffListStr;
    private String sparePartListStr;
    //dataGrid删除数据id
    private List<Long> dgDeletedIds_sparePart = new ArrayList<>();
    private List<Long> dgDeletedIds_repairStaff = new ArrayList<>();
    private List<Long> dgDeletedIds_lubricateOils = new ArrayList<>();
    private WXGDEntity oldWxgdEntity;
    private CustomDialog customDialog;


    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_execute;
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
        mLinkController = new LinkController();
        registerController(Constant.Controller.LINK, mLinkController);
        roleController = new RoleController();
        registerController(Constant.Controller.ROLE, roleController);
        roleController.queryRoleList(EamApplication.getUserName());

        wxgdSubmitController = new WXGDSubmitController(this);
        registerController(WXGDSubmitController.class.getName(), wxgdSubmitController);
    }


    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        EventBus.getDefault().register(this);

        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(true);
        mWXGDEntity = (WXGDEntity) getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);

        oldWxgdEntity = GsonUtil.gsonToBean(mWXGDEntity.toString(), WXGDEntity.class);

        mDatePickController = new DatePickController(this);
        mDatePickController.setSecondVisible(true);
        mDatePickController.setCanceledOnTouchOutside(true);
        mDatePickController.setDividerVisible(true);

        mSparePartController = getController(SparePartController.class);
        mSparePartController.setEditable(true);
        mRepairStaffController = getController(RepairStaffController.class);
        mRepairStaffController.setEditable(true);
        mLubricateOilsController = getController(LubricateOilsController.class);
        mLubricateOilsController.setEditable(true);
        mAcceptanceCheckController = getController(AcceptanceCheckController.class);
        mAcceptanceCheckController.setEditable(false);

    }


    @Override
    protected void initView() {
        super.initView();
        eamIc = findViewById(R.id.eamIc);
        titleText.setText(mWXGDEntity.pending == null ? "" : mWXGDEntity.pending.taskDescription);
        initTableHeadView();

    }


    @Override
    protected void initData() {
        super.initData();
        String type = mWXGDEntity.workType != null ? mWXGDEntity.workType.id : "";
        if (type.equals(Constant.WxgdAction.ACTIVATE)) {
            isActivate = true;
        } else if (type.equals(Constant.WxgdAction.STOP)) {
            isActivate = false;
        }
        initTableHeadData();
        mLinkController.initPendingTransition(transition, mWXGDEntity.pending != null ? mWXGDEntity.pending.id : 0);
    }

    /**
     * @param
     * @return
     * @description 初始化表头显示
     * @author zhangwenshuai1 2018/8/16
     */
    private void initTableHeadView() {
        if (mWXGDEntity.workSource == null) {
            faultInfo.setVisibility(View.GONE);
            noFaultInfo.setVisibility(View.GONE);
            acceptanceCheckListWidget.setVisibility(View.GONE);
        } else {
            if (Constant.WxgdWorkSource.lubrication.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.maintenance.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.sparepart.equals(mWXGDEntity.workSource.id)) {
                findViewById(R.id.faultInfoTitle).setVisibility(View.GONE);
                faultInfo.setVisibility(View.GONE);
                noFaultInfo.setVisibility(View.VISIBLE);
                if (Constant.PeriodType.RUNTIME_LENGTH.equals(mWXGDEntity.jwxItem.periodType == null ? "" : mWXGDEntity.jwxItem.periodType.id)) {
                    lastDuration.setVisibility(View.VISIBLE);
                    lastExecuteTime.setVisibility(View.GONE);
                } else {
                    lastExecuteTime.setVisibility(View.VISIBLE);
                    lastDuration.setVisibility(View.GONE);
                }
                realEndDate.setEditable(true);
                realEndDate.setNecessary(true);
                acceptanceCheckListWidget.setVisibility(View.GONE);
            } else {
                findViewById(R.id.faultInfoTitle).setVisibility(View.VISIBLE);
                faultInfo.setVisibility(View.VISIBLE);
                noFaultInfo.setVisibility(View.GONE);
                acceptanceCheckListWidget.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * @param
     * @return
     * @description 初始化表头数据
     * @author zhangwenshuai1 2018/8/16
     */
    private void initTableHeadData() {
        String value = mWXGDEntity.workSource == null ? "" : mWXGDEntity.workSource.value;
        if (TextUtils.isEmpty(value)) {
            workSource.setVisibility(View.GONE);
        }
        workSource.setText(value);
        if (mWXGDEntity.eamID != null && mWXGDEntity.eamID.id!=null) {
            eamName.setValue(mWXGDEntity.eamID.name);
            eamCode.setValue(mWXGDEntity.eamID.code);
            eamArea.setValue(mWXGDEntity.eamID.installPlace == null ? "" : mWXGDEntity.eamID.installPlace.name);

            new EamPicController().initEamPic(eamIc, mWXGDEntity.eamID.id);

        }

        if (mWXGDEntity.faultInfo != null) {
            if (mWXGDEntity.workSource != null && Constant.WxgdWorkSource.lubrication.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.maintenance.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.sparepart.equals(mWXGDEntity.workSource.id)) {
                content.setValue(mWXGDEntity.content);
                claim.setValue(mWXGDEntity.claim);
                period.setValue(mWXGDEntity.period == null ? "" : String.valueOf(mWXGDEntity.period));
                periodUnit.setValue(mWXGDEntity.periodUnit == null ? "" : mWXGDEntity.periodUnit.value);
                lastExecuteTime.setDate(mWXGDEntity.lastTime == null ? "" : DateUtil.dateFormat(mWXGDEntity.lastTime, "yyyy-MM-dd HH:mm:ss"));
//                nextExecuteTime.setValue(mWXGDEntity.nextTime == null ? "" : DateUtil.dateFormat(mWXGDEntity.nextTime,"yyyy-MM-dd HH:mm:ss"));
                lastDuration.setDate(mWXGDEntity.lastDuration == null ? "" : String.valueOf(mWXGDEntity.lastDuration));
                realEndDate.setDate(mWXGDEntity.realEndDate == null ? "" : DateUtil.dateFormat(mWXGDEntity.realEndDate, "yyyy-MM-dd HH:mm:ss"));
            } else {
                if (mWXGDEntity.faultInfo != null) {
                    faultInfoType.setValue(mWXGDEntity.faultInfo.faultInfoType == null ? "" : mWXGDEntity.faultInfo.faultInfoType.value);
                    repairType.setValue(mWXGDEntity.faultInfo.repairType == null ? "" : mWXGDEntity.faultInfo.repairType.value);
                    priority.setValue(mWXGDEntity.faultInfo.priority == null ? "" : mWXGDEntity.faultInfo.priority.value);
                    faultInfoDescribe.setValue(mWXGDEntity.faultInfo.describe);
                }
            }
        }
        chargeStaff.setValue(mWXGDEntity.chargeStaff != null ? mWXGDEntity.chargeStaff.name : "");
        repairGroup.setValue(mWXGDEntity.repairGroup != null ? mWXGDEntity.repairGroup.name : "");
        planStartTime.setDate(mWXGDEntity.planStartDate == null ? "" : sdf.format(mWXGDEntity.planStartDate));
        planEndTime.setDate(mWXGDEntity.planEndDate == null ? "" : sdf.format(mWXGDEntity.planEndDate));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        initData();
    }

    //更新备件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshSparePart(SparePartEvent sparePartEvent) {
        mSparePartController.updateSparePartEntities(sparePartEvent.getList());
        if (sparePartEvent.getList().size() <= 0) {
//            sparePartListWidget.clear();
            mSparePartController.clear();
        }
        dgDeletedIds_sparePart = sparePartEvent.getDgDeletedIds();
    }

    //更新维修人员
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshRepairStaff(RepairStaffEvent repairStaffEvent) {
        mRepairStaffController.updateRepairStaffEntiies(repairStaffEvent.getList());
        if (repairStaffEvent.getList().size() <= 0) {
//            repairStaffListWidget.clear();
            mRepairStaffController.clear();
        }
        dgDeletedIds_repairStaff = repairStaffEvent.getDgDeletedIds();
    }

    //更新润滑油
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshLubricateOils(LubricateOilsEvent oilsEvent) {
        mLubricateOilsController.updateLubricateOilsEntities(oilsEvent.getList());
        if (oilsEvent.getList().size() <= 0) {
//            lubricateOilsListWidget.clear();
            mLubricateOilsController.clear();
        }
        dgDeletedIds_lubricateOils = oilsEvent.getDgDeletedIds();
    }

    /**
     * @param
     * @return
     * @description 接收备件表体原始数据
     * @author zhangwenshuai1 2018/10/11
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveInitSparePartList(ListEvent listEvent) {
        if ("sparePart".equals(listEvent.getFlag()))
            sparePartListStr = listEvent.getList().toString();
    }

    /**
     * @param
     * @return
     * @description 接收维修人员表体原始数据
     * @author zhangwenshuai1 2018/10/11
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveInitRepairStaffList(ListEvent listEvent) {
        if ("repairStaff".equals(listEvent.getFlag()))
            repairStaffListStr = listEvent.getList().toString();
    }

    /**
     * @param
     * @return
     * @description 接收润滑油表体原始数据
     * @author zhangwenshuai1 2018/10/11
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveInitLubricateOilsList(ListEvent listEvent) {
        if ("lubricateOils".equals(listEvent.getFlag()))
            lubricateOilsListStr = listEvent.getList().toString();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reSubmit(LoginEvent loginEvent) {
        SnackbarHelper.showMessage(content, "登陆成功，请重新操作!");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void versionRefresh(VersionRefreshEvent versionRefreshEvent){
        refreshController.refreshBegin();
    }

    @Override
    protected void initListener() {
        super.initListener();

        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String,Object> queryParam = new HashMap<>();
                queryParam.put(Constant.BAPQuery.TABLE_NO,mWXGDEntity.tableNo);
                presenterRouter.create(WXGDListAPI.class).listWxgds(1,queryParam);
            }
        });

        leftBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        realEndDate.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    mWXGDEntity.realEndDate = null;
                } else {
                    mDatePickController.listener((year, month, day, hour, minute, second) -> {
                        String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        mWXGDEntity.realEndDate = DateUtil.dateFormat(dateStr, "yyyy-MM-dd HH:mm:ss");
                        realEndDate.setDate(dateStr);

                    }).show(mWXGDEntity.realEndDate == null ? new Date().getTime() : mWXGDEntity.realEndDate);
                }
            }
        });


        transition.setOnChildViewClickListener((childView, action, obj) -> {
            WorkFlowVar workFlowVar = (WorkFlowVar) obj;
            switch (action) {
                case 1:
                case 2:
                    if (!isActivate) {
                        if (!Constant.Transition.REJECT_CN.equals(workFlowVar.dec)) {
                            ToastUtils.show(WXGDExecuteActivity.this, "此工单已被暂停,不允许提交,请先激活!");
                            return;
                        }
                        mWXGDEntity.workType.id = Constant.WxgdAction.ACTIVATE;
                    }
                    if (!Constant.Transition.REJECT_CN.equals(workFlowVar.dec) && doCheckSparePartUsing()) {
                        ToastUtils.show(context, "存在领用中备件，单据不可提交", 2500);
                        return;
                    }
                    // 润滑、维保、备件更换 判断实际结束时间必填
                    if (!Constant.Transition.REJECT_CN.equals(workFlowVar.dec)){
                        if (Constant.WxgdWorkSource.lubrication.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.maintenance.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.sparepart.equals(mWXGDEntity.workSource.id)){
                            if (mWXGDEntity.realEndDate == null){
                                SnackbarHelper.showError(rootView,"请填写实际结束时间");
                                return;
                            }
                        }
                    }

                    onLoading("正在处理中...");
                    tip = "处理成功";
                    doSubmit(workFlowVar);
                    break;
                case 0:
                    onLoading("正在保存中...");
                    tip = "保存成功";
                    doSave();
                    break;

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isActivate) {
                    ToastUtils.show(WXGDExecuteActivity.this, "此工单已暂停,无需再次暂停!");
                    return;
                }
                isActionSop = true;
                int screenWidth = DisplayUtil.getScreenWidth(WXGDExecuteActivity.this);
                int i = DisplayUtil.dip2px(40, WXGDExecuteActivity.this);
                customDialog = new CustomDialog(context).layout(R.layout.item_dialog, screenWidth - i, WRAP_CONTENT)
                        .bindView(R.id.blueBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindTextChangeListener(R.id.reason, new OnTextListener() {
                            @Override
                            public void onText(String text) {
                                reason = text.trim();
                            }
                        })
                        .bindClickListener(R.id.blueBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v12) {
                                if (TextUtils.isEmpty(reason)) {
                                    ToastUtils.show(WXGDExecuteActivity.this, "请输入暂停原因!");
                                    return;
                                }
                                customDialog.dismiss();
                                saveAndActivate = true;
                                onLoading("正在处理中...");
                                doAction(isActionSop, reason);
                            }
                        }, false)
                        .bindClickListener(R.id.grayBtn, null, true);

                customDialog.show();
            }

        });
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActivate) {
                    ToastUtils.show(WXGDExecuteActivity.this, "此工单已激活,无需再次激活!");
                    return;
                }
                reason = "";
                isActionSop = false;
                new CustomDialog(context)
                        .twoButtonAlertDialog("确定激活吗?")
                        .bindView(R.id.redBtn, "确定")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v12) {
                                saveAndActivate = true;
                                onLoading("正在处理中...");
                                doAction(isActionSop, reason);
                            }
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show();
            }
        });
    }

    /**
     * @description 判断是否存在领用中的备件
     * @param
     * @return  
     * @author zhangwenshuai1 2018/10/11
     *
     */
    private boolean doCheckSparePartUsing() {
        List<SparePartEntity> sparePartEntityList = mSparePartController.getSparePartEntities();
        for (SparePartEntity sparePartEntity : sparePartEntityList) {
            if (sparePartEntity.useState != null && Constant.SparePartUseStatus.USEING.equals(sparePartEntity.useState.id)){
                return true;
            }
        }
        return false;
    }

    //保存
    private void doSave() {
        Map<String, Object> map = WXGDMapManager.createMap(mWXGDEntity);
        map.put("operateType", Constant.Transition.SAVE);
        if (isActionSop){
            map.put("workRecord.pauseReason", reason);
        }

        doSubmit(map);
    }

    //提交
    private void doSubmit(WorkFlowVar workFlowVar) {
//        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
//        workFlowEntity.dec = workFlowVar.dec;
//        workFlowEntity.type = "normal";
//        workFlowEntity.outcome = workFlowVar.outCome;
//        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
//        workFlowEntities.add(workFlowEntity);
        Map<String, Object> map = WXGDMapManager.createMap(mWXGDEntity);
        map.put("operateType", Constant.Transition.SUBMIT);
        map.put("workFlowVar.outcomeMapJson", workFlowVar.outcomeMapJson.toString());
        map.put("workFlowVar.outcome", workFlowVar.outCome);
        map.put("workFlowVarStatus","");
        if (Constant.Transition.REJECT_CN.equals(workFlowVar.dec)) {
            map.put("workFlowVarStatus", Constant.Transition.REJECT);
        }
        if (Constant.Transition.CANCEL_CN.equals(workFlowVar.dec)) {
            map.put("workFlowVarStatus", Constant.Transition.CANCEL);
        }

        doSubmit(map);
    }

    //请求接口
    private void doSubmit(Map<String, Object> map) {
//        RoleEntity roleEntity = roleController.getRoleEntity(0);
//        map.put("workRecord.createPositionId", EamApplication.getAccountInfo().positionId);
        map.put("viewselect", "workExecuteEdit");
        map.put("datagridKey", "BEAM2_workList_workRecord_workExecuteEdit_datagrids");
        map.put("viewCode", "BEAM2_1.0.0_workList_workExecuteEdit");
        map.put("taskDescription", "BEAM2_1.0.0.work.task1838");
        map.put("workRecord.repairSum", mWXGDEntity.repairSum);
        map.put("workRecord.workType.id", (mWXGDEntity.workType != null && mWXGDEntity.workType.id != null) ? mWXGDEntity.workType.id : null);
        map.put("workFlowVar.comment", commentInput.getInput());

        List<SparePartEntityDto> sparePartEntityDtos = WXGDMapManager.translateSparePartDto(mSparePartController.getSparePartEntities());
        List<RepairStaffDto> repairStaffDtos = WXGDMapManager.translateStaffDto(mRepairStaffController.getRepairStaffEntities());
        List<LubricateOilsEntityDto> lubricateOilsEntityDtos = WXGDMapManager.translateLubricateOilsDto(mLubricateOilsController.getLubricateOilsEntities());
        if (Constant.Transition.SUBMIT.equals(map.get("operateType")) && TextUtils.isEmpty(map.get("workFlowVarStatus").toString())){
            for (RepairStaffDto repairStaffDto : repairStaffDtos){
                if (TextUtils.isEmpty(repairStaffDto.repairStaff.id)) {
                    onLoadFailed("维修人员列表禁止存在空人员信息");
                    return;
                }
            }
            for (SparePartEntityDto sparePartEntityDto : sparePartEntityDtos){
                if (TextUtils.isEmpty(sparePartEntityDto.productID.id)) {
                    onLoadFailed("备件列表禁止存在空备件信息");
                    return;
                }
            }
            for (LubricateOilsEntityDto lubricateOilsEntityDto : lubricateOilsEntityDtos){
                if (TextUtils.isEmpty(lubricateOilsEntityDto.lubricate.id)) {
                    onLoadFailed("润滑油列表禁止存在空润滑油信息");
                    return;
                }
            }
        }
        map = WXGDMapManager.dgDeleted(map, dgDeletedIds_sparePart, "dg1531695921597");
        map.put("dg1531695921597ModelCode", "BEAM2_1.0.0_workList_SparePart");
        map.put("dg1531695921597ListJson", sparePartEntityDtos.toString());
        map.put("dgLists['dg1531695921597']", sparePartEntityDtos.toString());

        map = WXGDMapManager.dgDeleted(map, dgDeletedIds_repairStaff, "dg1531695921738");
        map.put("dg1531695921738ModelCode", "BEAM2_1.0.0_workList_RepairStaff");
        map.put("dg1531695921738ListJson", repairStaffDtos);
        map.put("dgLists['dg1531695921738']", repairStaffDtos);

        map = WXGDMapManager.dgDeleted(map, dgDeletedIds_lubricateOils, "dg1531695921784");
        map.put("dg1531695921784ModelCode", "BEAM2_1.0.0_workList_LubricateOil");
        map.put("dg1531695921784ListJson", lubricateOilsEntityDtos);
        map.put("dgLists['dg1531695921784']", lubricateOilsEntityDtos);

        map.put("dg1531695921847ModelCode", "BEAM2_1.0.0_workList_AccceptanceCheck");
        map.put("dg1531695921847ListJson", new LinkedList());
        map.put("dgLists['dg1531695921847']", new ArrayList<>());

        wxgdSubmitController.doExecuteSubmit(map);
    }

    //暂停或激活
    private void doAction(boolean stop, String reason) {
        Map<String, Object> map = new HashMap<>();
        if (stop) {
            map.put("workType", Constant.WxgdAction.STOP);
            map.put("stopReason",reason);
        } else {
            map.put("workType", Constant.WxgdAction.ACTIVATE);
        }
        map.put("workPendingId", mWXGDEntity.pending != null ? mWXGDEntity.pending.id : "");
        map.put("workRecordId", mWXGDEntity.id);
        presenterRouter.create(WXGDStopOrActivateAPI.class).stopOrStart(map);
    }

    @Override
    public void submitSuccess(BapResultEntity bapResultEntity) {
        if (saveAndActivate) {
            //状态改变后,切换状态
            if (isActionSop) {
                tip = "暂停成功";
            } else {
                tip = "激活成功";
            }
        }
        onLoadSuccessAndExit(tip, new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                EventBus.getDefault().post(new RefreshEvent());
                back();
            }
        });
    }

    @Override
    public void submitFailed(String errorMsg) {
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
    public void stopOrStartSuccess() {
        if (mWXGDEntity.workType == null) {
            mWXGDEntity.workType = new SystemCodeEntity();
        }
        //状态改变后,切换状态
        if (isActionSop) {
            mWXGDEntity.workType.id = Constant.WxgdAction.STOP;
        } else {
            mWXGDEntity.workType.id = Constant.WxgdAction.ACTIVATE;
        }
        doSave();
    }

    @Override
    public void stopOrStartFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void listWxgdsSuccess(WXGDListEntity entity) {
        refreshController.refreshComplete();
        List<WXGDEntity> wxgdEntityList = entity.result;
        if (wxgdEntityList.size() > 0) {
            mWXGDEntity = wxgdEntityList.get(0);
            oldWxgdEntity = GsonUtil.gsonToBean(mWXGDEntity.toString(), WXGDEntity.class);
            initTableHeadData();
        }
    }

    @Override
    public void listWxgdsFailed(String errorMsg) {
        SnackbarHelper.showError(rootView,errorMsg);
    }

    @Override
    public void onBackPressed() {
        if (doCheckChange()) {
            new CustomDialog(context)
                    .twoButtonAlertDialog("单据数据已经被修改，是否要保存?")
                    .bindView(R.id.redBtn, "保存")
                    .bindView(R.id.grayBtn, "离开")
                    .bindClickListener(R.id.redBtn, v1 -> {
                        onLoading("正在保存中...");
                        doSave();
                    }, true)
                    .bindClickListener(R.id.grayBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v3) {
                            EventBus.getDefault().post(new RefreshEvent());
                            finish();
                        }
                    }, true).show();
        } else {
            EventBus.getDefault().post(new RefreshEvent());
            super.onBackPressed();
        }

    }

    public boolean doCheckChange() {

        if (!oldWxgdEntity.toString().equals(mWXGDEntity.toString())) {
            return true;
        }

        if ((!TextUtils.isEmpty(lubricateOilsListStr) && !lubricateOilsListStr.equals(mLubricateOilsController.getLubricateOilsEntities().toString()))
                || (!TextUtils.isEmpty(repairStaffListStr) && !repairStaffListStr.equals(mRepairStaffController.getRepairStaffEntities().toString()))
                || (!TextUtils.isEmpty(sparePartListStr) && !sparePartListStr.equals(mSparePartController.getSparePartEntities().toString()))) {
            return true;
        }
        return false;
    }

}
