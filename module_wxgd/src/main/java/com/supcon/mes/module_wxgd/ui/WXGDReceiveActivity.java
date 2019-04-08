package com.supcon.mes.module_wxgd.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.beans.Transition;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.mbap.view.CustomPopTransation;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.RoleController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
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
import com.supcon.mes.module_wxgd.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDListContract;
import com.supcon.mes.module_wxgd.presenter.WXGDListPresenter;
import com.supcon.mes.module_wxgd.util.WXGDMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/8/28
 * ------------- Description -------------
 * 维修工单接单
 */
@Router(value = Constant.Router.WXGD_RECEIVE)
@Presenter(value = {WXGDListPresenter.class})
@Controller(value = {SparePartController.class, RepairStaffController.class, LubricateOilsController.class, AcceptanceCheckController.class})
public class WXGDReceiveActivity extends BaseRefreshActivity implements WXGDSubmitController.OnSubmitResultListener, WXGDListContract.View {

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

    private RepairStaffController mRepairStaffController;
    private SparePartController mSparePartController;
    private LubricateOilsController mLubricateOilsController;
    private AcceptanceCheckController mAcceptanceCheckController;


    private WXGDEntity mWXGDEntity;//传入维修工单实体参数
    private LinkController mLinkController;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private RoleController roleController;
    private String tip;
    private WXGDSubmitController wxgdSubmitController;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_receive;
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

    }


    @Override
    protected void initView() {
        super.initView();
        eamIc = findViewById(R.id.eamIc);
        titleText.setText(mWXGDEntity.pending == null ? "" : mWXGDEntity.pending.taskDescription);

        initTableHeadView();

//        sparePartListWidget.setAdapter(new SparePartAdapter(context, false));
//        repairStaffListWidget.setAdapter(new RepairStaffAdapter(context, false));
//        lubricateOilsListWidget.setAdapter(new LubricateOilsAdapter(context, false));
//        acceptanceCheckListWidget.setAdapter(new AcceptanceCheckAdapter(context,false));
    }

    @Override
    protected void initData() {
        super.initData();
        initTableHeadData();
        mLinkController.initPendingTransition(transition, mWXGDEntity.pending != null ? mWXGDEntity.pending.id : 0);
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
        if (mWXGDEntity.eamID != null && mWXGDEntity.eamID.id != null) {
            eamName.setValue(mWXGDEntity.eamID.name);
            eamCode.setValue(mWXGDEntity.eamID.code);
            eamArea.setValue(mWXGDEntity.eamID.installPlace == null ? "" : mWXGDEntity.eamID.installPlace.name);

            new EamPicController().initEamPic(eamIc, mWXGDEntity.eamID.id);
        }

        if (mWXGDEntity.faultInfo != null) {
            if (Constant.WxgdWorkSource.lubrication.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.maintenance.equals(mWXGDEntity.workSource.id) || Constant.WxgdWorkSource.sparePart.equals(mWXGDEntity.workSource.id)) {
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
        if (mWXGDEntity.planStartDate != null) {
            planStartTime.setDate(sdf.format(mWXGDEntity.planStartDate));
        }
        if (mWXGDEntity.planEndDate != null) {
            planEndTime.setDate(sdf.format(mWXGDEntity.planEndDate));
        }

    }

    @Override
    protected void initListener() {
        super.initListener();

        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put(Constant.BAPQuery.TABLE_NO, mWXGDEntity.tableNo);
                presenterRouter.create(WXGDListAPI.class).listWxgds(1, queryParam);
            }
        });

        RxView.clicks(leftBtn).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(v -> {
                    onBackPressed();
                });

        transition.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                switch (action) {
                    case 0:
                        tip = "保存成功";
                        doSave();
                        break;
                    case 1:
                    case 2:
                        doSubmit(workFlowVar);
                        tip = "提交成功";
                        break;
                    default:
                        break;
                }
            }
        });
//        transition.setOnChildViewClickListener(new OnChildViewClickListener() {
//            @Override
//            public void onChildViewClick(View childView, int action, Object obj) {
//                String tag = (String) childView.getTag();
//                Transition currentTransition = transition.currentTransition();
//                switch (tag) {
//                    case Constant.Transition.SUBMIT_BTN:
//                        tip = "提交成功";
//                        doSubmit(currentTransition);
//                        break;
//                    case Constant.Transition.SAVE:
//                        tip = "保存成功";
//                        doSave();
//                        break;
//
//                }
//            }
//        });

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
//                        bundle.putString(Constant.IntentKey.TABLE_STATUS,mWXGDEntity.pending.taskDescription);
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
//                        bundle.putString(Constant.IntentKey.TABLE_STATUS,mWXGDEntity.pending.taskDescription);
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
//                        bundle.putString(Constant.IntentKey.TABLE_STATUS,mWXGDEntity.pending.taskDescription);
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
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    //保存
    private void doSave() {
        onLoading("工单保存中...");
        Map<String, Object> map = WXGDMapManager.createMap(mWXGDEntity);
        map.put("operateType", Constant.Transition.SAVE);
        doSubmit(map);
    }

    //提交
    private void doSubmit(WorkFlowVar workFlowVar) {
        onLoading("工单提交中...");
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
        doSubmit(map);
    }

    //请求接口
    private void doSubmit(Map<String, Object> map) {
//        RoleEntity roleEntity = roleController.getRoleEntity(0);
//        map.put("workRecord.createPositionId", /*roleEntity.role != null ? roleEntity.role.id :*/ EamApplication.getAccountInfo().roleIds.split(",")[0]);
        map.put("viewselect", "workReceiptEdit");
        map.put("workRecord.receiptInfo", "jiedan");
        map.put("datagridKey", "BEAM2_workList_workRecord_workReceiptEdit_datagrids");
        map.put("viewCode", "BEAM2_1.0.0_workList_workReceiptEdit");
        map.put("taskDescription", "BEAM2_1.0.0.work.task457");
        map.put("workFlowVar.comment", commentInput.getInput());


        map.put("dg1531695879537ModelCode", "BEAM2_1.0.0_workList_AccceptanceCheck");
        map.put("dg1531695879537ListJson", new LinkedList().toString());

        map.put("dg1531695879443ModelCode", "BEAM2_1.0.0_workList_LubricateOil");
        map.put("dg1531695879443ListJson", new LinkedList().toString());

        map.put("dg1531695879084ModelCode", "BEAM2_1.0.0_workList_SparePart");
        map.put("dg1531695879084ListJson", new LinkedList().toString());

        map.put("dg1531695879365ModelCode", "BEAM2_1.0.0_workList_RepairStaff");
        map.put("dg1531695879365ListJson", new LinkedList().toString());
        wxgdSubmitController.doReceiveSubmit(map);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        initData();
    }

    @Override
    public void submitSuccess(BapResultEntity bapResultEntity) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reSubmit(LoginEvent loginEvent) {
        SnackbarHelper.showMessage(content, "登陆成功，请重新操作!");
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
        SnackbarHelper.showError(rootView, errorMsg);
    }
}
