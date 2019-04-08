package com.supcon.mes.module_wxgd.ui;

import android.os.Bundle;
import android.view.View;
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
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.beans.Transition;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.mbap.view.CustomPopTransation;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.RoleController;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntityDao;
import com.supcon.mes.middleware.model.bean.UserInfo;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.controller.AcceptanceCheckController;
import com.supcon.mes.module_wxgd.controller.LubricateOilsController;
import com.supcon.mes.module_wxgd.controller.RepairStaffController;
import com.supcon.mes.module_wxgd.controller.SparePartController;
import com.supcon.mes.module_wxgd.controller.WXGDSubmitController;
import com.supcon.mes.module_wxgd.model.api.WXGDListAPI;
import com.supcon.mes.module_wxgd.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDListContract;
import com.supcon.mes.module_wxgd.model.dto.AcceptanceCheckEntityDto;
import com.supcon.mes.module_wxgd.presenter.WXGDListPresenter;
import com.supcon.mes.module_wxgd.util.WXGDMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WXGDDispatcherActivity 验收Activity
 * created by zhangwenshuai1 2018/8/15
 */

@Router(value = Constant.Router.WXGD_ACCEPTANCE)
@Presenter(value = {WXGDListPresenter.class})
@Controller(value = {SparePartController.class,RepairStaffController.class,LubricateOilsController.class,AcceptanceCheckController.class})
public class WXGDAcceptanceActivity extends BaseRefreshActivity implements WXGDSubmitController.OnSubmitResultListener, AcceptanceCheckController.OnLastItemListener,WXGDListContract.View {

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
    @BindByTag("acceptChkStaff")
    CustomVerticalTextView acceptChkStaff;
    @BindByTag("acceptChkStaffCode")
    CustomVerticalTextView acceptChkStaffCode;
    @BindByTag("acceptChkTime")
    CustomVerticalDateView acceptChkTime;
    @BindByTag("acceptChkResult")
    CustomVerticalSpinner acceptChkResult;
    @BindByTag("commentInput")
    CustomEditText commentInput;
    @BindByTag("transition")
    CustomWorkFlowView transition;

    private AcceptanceCheckController mAcceptanceCheckController;
    private RepairStaffController mRepairStaffController;
    private SparePartController mSparePartController;
    private LubricateOilsController mLubricateOilsController;

    private WXGDEntity mWXGDEntity;//传入维修工单实体参数
    private WXGDSubmitController wxgdSubmitController;

    private LinkController mLinkController;

    private SinglePickController mSinglePickController;
    private DatePickController mDatePickController;
    private String currentAcceptChkDateTime = ""; // 当前验收时间
    private List<SystemCodeEntity> checkResultList = new ArrayList<>();
    private List<String> checkResultListStr = new ArrayList<>();
    private RoleController roleController;
    //    private Staff staff = new Staff();  //验收人员;
//    private List<AcceptanceCheckEntity> currentAcceptChkList = new ArrayList<>(); // 当前验收数据列表
    private AcceptanceCheckEntity currentAcceptChkEntity = new AcceptanceCheckEntity();  //当前验收数据

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_accptence;
    }

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        EventBus.getDefault().register(this);
        refreshController.setPullDownRefreshEnabled(true);
        refreshController.setAutoPullDownRefresh(true);
        mWXGDEntity = (WXGDEntity) getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);

        mSparePartController = getController(SparePartController.class);
        mSparePartController.setEditable(false);
        mRepairStaffController = getController(RepairStaffController.class);
        mRepairStaffController.setEditable(false);
        mLubricateOilsController = getController(LubricateOilsController.class);
        mLubricateOilsController.setEditable(false);
        mAcceptanceCheckController = getController(AcceptanceCheckController.class);
        mAcceptanceCheckController.setEditable(false);
        mAcceptanceCheckController.setOnLastItemListener(this::getLastItem);


        mLinkController = new LinkController();
        roleController = new RoleController();  //角色
        roleController.queryRoleList(EamApplication.getUserName());

        registerController(Constant.Controller.ROLE, roleController);

        mSinglePickController = new SinglePickController(this);
        mSinglePickController.setDividerVisible(true);
        mSinglePickController.setCanceledOnTouchOutside(true);

        mDatePickController = new DatePickController(this);
        mDatePickController.setSecondVisible(true);
        mDatePickController.setDividerVisible(true);
        mDatePickController.setCanceledOnTouchOutside(true);

        initCheckResult();
    }

    private void initCheckResult() {
        checkResultList = EamApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(Constant.SystemCode.CHECK_RESULT)).list();
        for (SystemCodeEntity entity : checkResultList) {
            checkResultListStr.add(entity.value);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        eamIc = findViewById(R.id.eamIc);
        titleText.setText(mWXGDEntity.pending == null ? "" : mWXGDEntity.pending.taskDescription);

        initTableHeadView();

        // 初始化工作流
        initLink();

    }

    /**
     * @param
     * @return
     * @description 初始化工作流
     * @author zhangwenshuai1 2018/9/10
     */
    private void initLink() {
        mLinkController.initPendingTransition(transition, mWXGDEntity.pending.id);
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
            workSource.setVisibility(View.GONE);
            acceptanceCheckListWidget.setVisibility(View.GONE);
        } else {
            if (Constant.WxgdWorkSource.lubrication.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.maintenance.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.sparePart.equals(mWXGDEntity.workSource.id)) {
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
                acceptanceCheckListWidget.setVisibility(View.GONE);
            } else {
                findViewById(R.id.faultInfoTitle).setVisibility(View.VISIBLE);
                faultInfo.setVisibility(View.VISIBLE);
                noFaultInfo.setVisibility(View.GONE);
                acceptanceCheckListWidget.setVisibility(View.VISIBLE);

            }
            workSource.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
        wxgdSubmitController = new WXGDSubmitController(this);
        registerController(WXGDSubmitController.class.getName(), wxgdSubmitController);
    }

    @Override
    protected void initData() {
        super.initData();
        initTableHeadData();
//        presenterRouter.create(WXGDDispatcherAPI.class).getWxgdInfo(1000, 1000);
    }

    /**
     * @param
     * @return
     * @description 初始化表头数据
     * @author zhangwenshuai1 2018/8/16
     */
    private void initTableHeadData() {
        workSource.setText(mWXGDEntity.workSource == null ? "" : mWXGDEntity.workSource.value);
        if (mWXGDEntity.eamID != null && mWXGDEntity.eamID.id!=null) {
            eamName.setValue(mWXGDEntity.eamID.name);
            eamCode.setValue(mWXGDEntity.eamID.code);
            eamArea.setValue(mWXGDEntity.eamID.installPlace == null ? "" : mWXGDEntity.eamID.installPlace.name);

            new EamPicController().initEamPic(eamIc, mWXGDEntity.eamID.id);
        }
        if (mWXGDEntity.workSource == null) {

        } else {

            if (Constant.WxgdWorkSource.lubrication.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.maintenance.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.sparePart.equals(mWXGDEntity.workSource.id)) {
                content.setValue(mWXGDEntity.content);
                claim.setValue(mWXGDEntity.claim);
                period.setValue(mWXGDEntity.period == null ? "" : String.valueOf(mWXGDEntity.period));
                periodUnit.setValue(mWXGDEntity.periodUnit == null ? "" : mWXGDEntity.periodUnit.value);
                lastExecuteTime.setDate(mWXGDEntity.lastTime == null ? "" : DateUtil.dateFormat(mWXGDEntity.lastTime, "yyyy-MM-dd HH:mm:ss"));
//                nextExecuteTime.setValue(mWXGDEntity.nextTime == null ? "" : DateUtil.dateFormat(mWXGDEntity.nextTime, "yyyy-MM-dd HH:mm:ss"));
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
        planStartTime.setDate(mWXGDEntity.planStartDate == null ? "" : DateUtil.dateTimeFormat(mWXGDEntity.planStartDate));
        planEndTime.setDate(mWXGDEntity.planEndDate == null ? "" : DateUtil.dateTimeFormat(mWXGDEntity.planEndDate));
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

        leftBtn.setOnClickListener(v -> onBackPressed());

//        sparePartListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
//            @Override
//            public void onChildViewClick(View childView, int action, Object obj) {
//                Bundle bundle = new Bundle();
//                String sparePartLists;
//                switch (action) {
//                    case CustomListWidget.ACTION_VIEW_ALL:
//                    case 0:
//                        sparePartLists = mSparePartController.getSparePartEntities().toString();
//                        bundle.putString(Constant.IntentKey.SPARE_PART_ENTITIES, sparePartLists);
//                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, false);
//                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
//                        bundle.putString(Constant.IntentKey.TABLE_STATUS, mWXGDEntity.pending.taskDescription);
//                        bundle.putString(Constant.IntentKey.TABLE_ACTION,mWXGDEntity.pending.openUrl);
//                        IntentRouter.go(context, Constant.Router.WXGD_SPARE_PART_LIST, bundle);
//                        break;
//                    case CustomListWidget.ACTION_ITEM_DELETE:
//                        break;
//                    case CustomListWidget.ACTION_EDIT:
//                        break;
//                    case CustomListWidget.ACTION_ADD:
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//        repairStaffListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
//            @Override
//            public void onChildViewClick(View childView, int action, Object obj) {
//                Bundle bundle = new Bundle();
//                String repairStaffLists;
//                switch (action) {
//                    case CustomListWidget.ACTION_VIEW_ALL:
//                    case 0:
//                        repairStaffLists = mRepairStaffController.getRepairStaffEntities().toString();
//                        bundle.putString(Constant.IntentKey.REPAIR_STAFF_ENTITIES, repairStaffLists);
//                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, false);
//                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
//                        bundle.putString(Constant.IntentKey.TABLE_STATUS, mWXGDEntity.pending.taskDescription);
//                        IntentRouter.go(context, Constant.Router.WXGD_REPAIR_STAFF_LIST, bundle);
//                        break;
//                    case CustomListWidget.ACTION_ITEM_DELETE:
//                        break;
//                    case CustomListWidget.ACTION_EDIT:
//                        break;
//                    case CustomListWidget.ACTION_ADD:
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//        lubricateOilsListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
//            @Override
//            public void onChildViewClick(View childView, int action, Object obj) {
//                Bundle bundle = new Bundle();
//                String lubricateOilsLists;
//                switch (action) {
//                    case CustomListWidget.ACTION_VIEW_ALL:
//                    case 0:
//                        lubricateOilsLists = mLubricateOilsController.getLubricateOilsEntities().toString();
//                        bundle.putString(Constant.IntentKey.LUBRICATE_OIL_ENTITIES, lubricateOilsLists);
//                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, false);
//                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
//                        bundle.putString(Constant.IntentKey.TABLE_STATUS, mWXGDEntity.pending.taskDescription);
//                        IntentRouter.go(context, Constant.Router.WXGD_LUBRICATE_OIL_LIST, bundle);
//                        break;
//                    case CustomListWidget.ACTION_ITEM_DELETE:
//                        break;
//                    case CustomListWidget.ACTION_EDIT:
//                        break;
//                    case CustomListWidget.ACTION_ADD:
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//        acceptanceCheckListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
//            @Override
//            public void onChildViewClick(View childView, int action, Object obj) {
//                Bundle bundle = new Bundle();
//                switch (action) {
//                    case CustomListWidget.ACTION_VIEW_ALL:
//                    case 0:
//                        bundle.putString(Constant.IntentKey.ACCEPTANCE_ENTITIES, mAcceptanceCheckController.getAcceptanceCheckEntities().toString());
//                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, false);
//                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
//                        IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE_LIST, bundle);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });

        acceptChkStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    currentAcceptChkEntity.checkStaff = null;
                    acceptChkStaffCode.setValue(null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.IntentKey.COMMON_SAERCH_MODE, Constant.CommonSearchMode.STAFF);
                    bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG, childView.getTag().toString());
                    IntentRouter.go(context, Constant.Router.COMMON_SEARCH, bundle);
                }
            }
        });

        acceptChkTime.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    currentAcceptChkEntity.checkTime = null;
                    currentAcceptChkDateTime = "";
                } else {
                    mDatePickController.listener((year, month, day, hour, minute, second) -> {
                        currentAcceptChkDateTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        acceptChkTime.setDate(currentAcceptChkDateTime);
                        currentAcceptChkEntity.checkTime = DateUtil.dateFormat(currentAcceptChkDateTime, "yyyy-MM-dd HH:mm:ss");
                    }).show("".equals(currentAcceptChkDateTime) ? new Date().getTime() : DateUtil.dateFormat(currentAcceptChkDateTime, "yyyy-MM-dd HH:mm:ss"));
                }
            }
        });

        acceptChkResult.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1) {
                    currentAcceptChkEntity.checkResult = null;
                } else {
                    mSinglePickController.list(checkResultListStr)
                            .listener((index, item) -> {
                                acceptChkResult.setSpinner(String.valueOf(item));

                                currentAcceptChkEntity.checkResult = checkResultList.get(checkResultListStr.indexOf(String.valueOf(item)));
                            }).show(acceptChkResult.getSpinnerValue());
                }
            }
        });

        transition.setOnChildViewClickListener((childView, action, obj) -> {
            switch (action) {
                case 0:
                    doSave();
                    break;
                case 1:
                case 2:
                    WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                    doSubmit(workFlowVar);
                    break;
                default:
                    break;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (doCheckChange()) {
            new CustomDialog(context)
                    .twoButtonAlertDialog("单据数据已经被修改，是否要保存?")
                    .bindView(R.id.grayBtn, "离开")
                    .bindView(R.id.redBtn, "保存")
                    .bindClickListener(R.id.grayBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new RefreshEvent());
                            finish();
                        }
                    }, true)
                    .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doSave();
                        }
                    }, true)
                    .show();
        } else {
            EventBus.getDefault().post(new RefreshEvent());
            finish();
        }
    }

    /**
     * @param
     * @return
     * @description 保存单据
     * @author zhangwenshuai1 2018/9/10
     */
    private void doSave() {
        try {
            onLoading("工单保存中...");
            Map map = WXGDMapManager.createMap(mWXGDEntity);
            map.put("operateType", Constant.Transition.SAVE);
            generateParamsDtoAndSubmit(map);
        } catch (Exception e) {
            onLoadFailed("保存失败");
        }
    }

    /**
     * @param
     * @param workFlowVar
     * @return
     * @description 提交单据
     * @author zhangwenshuai1 2018/9/10
     */
    private void doSubmit(WorkFlowVar workFlowVar) {
        if ("".equals(acceptChkResult.getSpinnerValue())) {
            ToastUtils.show(context, "请填写验收结论!");
            return;
        }

        try {
            onLoading("工单提交中...");
            Map<String, Object> map = WXGDMapManager.createMap(mWXGDEntity);

            //封装工作流
            map = generateWorkFlow(map, workFlowVar);

            generateParamsDtoAndSubmit(map);
        } catch (Exception e) {
            onLoadFailed("提交失败");
        }
    }

    private Map<String, Object> generateWorkFlow(Map<String, Object> map, WorkFlowVar workFlowVar) {
//        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
//        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
//        workFlowEntity.type = "normal";
//        workFlowEntity.outcome = currentTransition.outCome;
//        workFlowEntity.dec = currentTransition.transitionDesc;
//
//        workFlowEntities.add(workFlowEntity);

        map.put("operateType", Constant.Transition.SUBMIT);
        map.put("workFlowVar.outcomeMapJson", workFlowVar.outcomeMapJson.toString());
        map.put("workFlowVar.outcome", workFlowVar.outCome);
        map.put("workFlowVarStatus", "");

        return map;
    }

    /**
     * @param
     * @param map
     * @return
     * @description 封装提交传输的参数信息且提交
     * @author zhangwenshuai1 2018/9/6
     */
    private void generateParamsDtoAndSubmit(Map<String, Object> map) {

        //其他参数
//        RoleEntity roleEntity = roleController.getRoleEntity(0);
//        map.put("workRecord.createPositionId", EamApplication.getAccountInfo().positionId);
        map.put("viewselect", "workCheckEdit");
        map.put("datagridKey", "BEAM2_workList_workRecord_workCheckEdit_datagrids");
        map.put("viewCode", "BEAM2_1.0.0_workList_workCheckEdit");
        map.put("workFlowVar.comment", commentInput.getInput());

        //表体dataGrids
//        LinkedList<RepairStaffDto> repairStaffDtos = WXGDMapManager.translateStaffDto(mRepairStaffController.getRepairStaffEntities());
//        if (repairStaffDtos == null){
//            onLoadFailed("人力栏禁止存在空人员信息");
//            return;
//        }
//        map.put("dg1531695961519ModelCode", "BEAM2_1.0.0_workList_RepairStaff");
//        map.put("dgLists['dg1531695961519']", new ArrayList<>());

//        LinkedList<SparePartEntityDto> sparePartEntityDtos = WXGDMapManager.translateSparePartDto(mSparePartController.getSparePartEntities());
//        if (sparePartEntityDtos == null){
//            onLoadFailed("备件栏禁止存在空备件信息");
//            return;
//        }
//        map.put("dg1531695961378ModelCode", "BEAM2_1.0.0_workList_SparePart");
//        map.put("dgLists['dg1531695961378']", new ArrayList<>());

//        LinkedList<LubricateOilsEntityDto> lubricateOilsEntityDtos = WXGDMapManager.translateLubricateOilsDto(mLubricateOilsController.getLubricateOilsEntities());
//        if (lubricateOilsEntityDtos == null){
//            onLoadFailed("润滑油栏禁止存在空润滑油信息");
//            return;
//        }
//        map.put("dg1531695961550ModelCode", "BEAM2_1.0.0_workList_LubricateOil");
//        map.put("dgLists['dg1531695961550']", new ArrayList<>());


        List<AcceptanceCheckEntityDto> acceptanceCheckEntityDtos = WXGDMapManager.translateAcceptChkDto(getCurrentAcceptChk());
        map.put("dg1531695961597ModelCode", "BEAM2_1.0.0_workList_AccceptanceCheck");
        map.put("dgLists['dg1531695961597']", acceptanceCheckEntityDtos.toString());

        //表头检验结论
        map.put("workRecord.checkResult.id", currentAcceptChkEntity.checkResult == null ? "" : currentAcceptChkEntity.checkResult.id);
        if (acceptChkResult.getSpinnerValue().equals("不合格") && Constant.Transition.SUBMIT.equals(map.get("operateType"))) {
            map.put("workRecord.repairSum", currentAcceptChkEntity.timesNum + 1);
        }

        wxgdSubmitController.doAcceptChkSubmit(map);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void submitSuccess(BapResultEntity bapResultEntity) {
        onLoadSuccessAndExit("处理成功", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                EventBus.getDefault().post(new RefreshEvent());
                finish();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reSubmit(LoginEvent loginEvent) {
        SnackbarHelper.showMessage(content, "登陆成功，请重新操作!");
    }

    /**
     * @param
     * @return
     * @description 加载数据, 监听验收当次, 数据回填
     * @author zhangwenshuai1 2018/9/10
     */
    @Override
    public void getLastItem(List<AcceptanceCheckEntity> list) {
//        List<AcceptanceCheckEntity> currentAcceptChkList = list;
        if (list.size() > 0) {
            AcceptanceCheckEntity entity = list.get(list.size() - 1);

            if (entity.checkStaff != null) {
                acceptChkStaff.setValue(entity.checkStaff.name);
                acceptChkStaffCode.setValue(entity.checkStaff.code);

//                Staff staff = new Staff();
//                staff.id = entity.checkStaff.id;
//                staff.name = entity.checkStaff.name;
//                staff.code = entity.checkStaff.code;
//
                currentAcceptChkEntity.checkStaff = entity.checkStaff;
            }

            String time = entity.checkTime == null ? "" : DateUtil.dateFormat(entity.checkTime, "yyyy-MM-dd HH:mm:ss");
            acceptChkTime.setDate(time);

            currentAcceptChkDateTime = time;//若已有时间，赋值

            if (entity.checkResult != null) {
                acceptChkResult.setSpinner(entity.checkResult.value);

//                SystemCodeEntity systemCodeEntity = new SystemCodeEntity();
//                systemCodeEntity.id = entity.checkResult.id;
//                systemCodeEntity.value = entity.checkResult.value;
//                systemCodeEntity.code = entity.checkResult.code;
//
                currentAcceptChkEntity.checkResult = entity.checkResult;
            }

            currentAcceptChkEntity.checkTime = entity.checkTime;
            currentAcceptChkEntity.id = entity.id;
            currentAcceptChkEntity.timesNum = entity.timesNum;
            currentAcceptChkEntity.remark = entity.remark;

        } else {
            //新增验收
            AccountInfo accountInfo = EamApplication.getAccountInfo();
            acceptChkStaff.setValue(accountInfo.staffName);
            acceptChkStaffCode.setValue(accountInfo.staffCode);

            Staff staff = new Staff();
            staff.id = accountInfo.staffId;
            staff.name = accountInfo.staffName;
            staff.code = accountInfo.staffCode;

            currentAcceptChkEntity.checkStaff = staff;
            currentAcceptChkEntity.timesNum = mWXGDEntity.repairSum;
        }
    }

    /**
     * @param
     * @return
     * @description 获取当前的验收
     * @author zhangwenshuai1 2018/9/10
     */
    public List<AcceptanceCheckEntity> getCurrentAcceptChk() {

        List<AcceptanceCheckEntity> currentAcceptChkList = new ArrayList<>();
        currentAcceptChkList.add(currentAcceptChkEntity);

        return currentAcceptChkList;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAcceptChkStaff(CommonSearchEvent event) {
        if ("acceptChkStaff".equals(event.flag)) {
            UserInfo userInfo = (UserInfo) event.commonSearchEntity;
            acceptChkStaff.setValue(userInfo.staffName);
            acceptChkStaffCode.setValue(userInfo.staffCode);
            currentAcceptChkEntity.checkStaff = new Staff();
            currentAcceptChkEntity.checkStaff.id = userInfo.staffId;
            currentAcceptChkEntity.checkStaff.code = userInfo.staffCode;
            currentAcceptChkEntity.checkStaff.name = userInfo.staffName;
        }
    }

    /**
     * @param
     * @return
     * @description 验收数据是否改变
     * @author zhangwenshuai1 2018/9/11
     */
    private boolean doCheckChange() {
        List<AcceptanceCheckEntity> list = mAcceptanceCheckController.getCurrentAcceptChkEntitiesListLast();
        if (list.size() > 0) {
            AcceptanceCheckEntity old = GsonUtil.gsonToBean(list.get(list.size() - 1).toString(), AcceptanceCheckEntity.class);
            if (!currentAcceptChkEntity.toString().equals(old.toString())) {
                return true;
            }
            return false;
        }
        if (currentAcceptChkEntity.checkResult != null || currentAcceptChkEntity.checkTime != null) {
            return true;
        }
        return false;
    }

    @Override
    public void listWxgdsSuccess(WXGDListEntity entity) {
        refreshController.refreshComplete();
        List<WXGDEntity> wxgdEntityList = entity.result;
        if (wxgdEntityList.size() > 0) {
            mWXGDEntity = wxgdEntityList.get(0);
        }
    }

    @Override
    public void listWxgdsFailed(String errorMsg) {
        SnackbarHelper.showError(rootView,errorMsg);
    }
}
