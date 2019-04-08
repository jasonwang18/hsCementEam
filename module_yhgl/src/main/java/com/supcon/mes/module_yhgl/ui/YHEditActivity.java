package com.supcon.mes.module_yhgl.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.Module;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.AreaDao;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupEntity;
import com.supcon.mes.middleware.model.bean.RepairGroupEntityDao;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.UserInfo;
import com.supcon.mes.middleware.model.bean.WXGDEam;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.module_yhgl.model.api.YHSubmitAPI;
import com.supcon.mes.module_yhgl.model.contract.YHSubmitContract;
import com.supcon.mes.module_yhgl.presenter.YHSubmitPresenter;
import com.supcon.mes.module_yhgl.util.FieldHepler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import static com.supcon.mes.middleware.constant.Constant.IntentKey.DEPLOYMENT_ID;

/**
 * Created by wangshizhan on 2018/8/21
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.YH_EDIT)
@Presenter(YHSubmitPresenter.class)
@Controller(OnlineCameraController.class)
public class YHEditActivity extends BaseRefreshActivity implements YHSubmitContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("yhEditFindTime")
    CustomVerticalDateView yhEditFindTime;

    @BindByTag("yhEditFindStaff")
    CustomVerticalTextView yhEditFindStaff;

    @BindByTag("yhEditArea")
    CustomVerticalSpinner yhEditArea;

    @BindByTag("yhEditPriority")
    CustomVerticalSpinner yhEditPriority;

    @BindByTag("yhEditEamCode")
    CustomVerticalTextView yhEditEamCode;

    @BindByTag("yhEditEamName")
    CustomVerticalTextView yhEditEamName;

    @BindByTag("yhEditEamModel")
    CustomTextView yhEditEamModel;

    @BindByTag("yhEditType")
    CustomSpinner yhEditType;

    @BindByTag("yhEditWXType")
    CustomVerticalSpinner yhEditWXType;

    @BindByTag("yhEditWXGroup")
    CustomVerticalSpinner yhEditWXGroup;

    @BindByTag("yhEditDescription")
    CustomVerticalEditText yhEditDescription;

    @BindByTag("yhGalleryView")
    CustomGalleryView yhGalleryView;

    @BindByTag("yhEditMemo")
    CustomVerticalEditText yhEditMemo;

    @BindByTag("yhEditCommentInput")
    CustomEditText yhEditCommentInput;

    @BindByTag("yhEditTransition")
    CustomWorkFlowView yhEditTransition;

    private YHEntity mYHEntity, mOriginalEntity;

    private SinglePickController mSinglePickController;
    private DatePickController mDatePickController;

    private Map<String, SystemCodeEntity> wxTypes;
    private Map<String, SystemCodeEntity> yhTypes;
    private Map<String, SystemCodeEntity> yhPriorities;
    private Map<String, Area> mAreas;
    private Map<String, RepairGroupEntity> mRepairGroups;

//    private AttachmentController mAttachmentController;
    private LinkController mLinkController;
    private List<GalleryBean> pics = new ArrayList<>();
    private boolean isCancel = false;
    private long deploymentId;
    private boolean hits;

    @Subscribe
    public void onReceiveImageDeleteEvent(ImageDeleteEvent imageDeleteEvent) {
        getController(OnlineCameraController.class).deleteGalleryBean(yhGalleryView.getGalleryAdapter().getList().get(imageDeleteEvent.getPos()), imageDeleteEvent.getPos());
//        yhGalleryView.deletePic(imageDeleteEvent.getPos());
        EventBus.getDefault().post(new RefreshEvent());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_yh_edit;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshController.setAutoPullDownRefresh(false);
        refreshController.setPullDownRefreshEnabled(false);
        mYHEntity = (YHEntity) getIntent().getSerializableExtra(Constant.IntentKey.YHGL_ENTITY);
        if (null != mYHEntity.eamID && !mYHEntity.eamID.checkNil()) {
            yhEditArea.setEditable(false);
        }
        deploymentId = getIntent().getLongExtra(DEPLOYMENT_ID, 0L);
        mOriginalEntity = GsonUtil.gsonToBean(mYHEntity.toString(), YHEntity.class);

        initSinglePickController();
        initDatePickController();
//        initAttachmentController();
    }

//    private void initAttachmentController() {
//        mAttachmentController = new AttachmentController();
//    }

    private void initDatePickController() {
        mDatePickController = new DatePickController(this);
        mDatePickController.textSize(18);
        mDatePickController.setCycleDisable(false);
        mDatePickController.setCanceledOnTouchOutside(true);
    }

    private void initSinglePickController() {
        mSinglePickController = new SinglePickController<String>(this);
        mSinglePickController.textSize(18);
        mSinglePickController.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
        mLinkController = new LinkController();
        registerController(Constant.Controller.LINK, mLinkController);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("隐患编辑");
        yhEditFindStaff.setValue(mYHEntity.findStaffID != null ? mYHEntity.findStaffID.name : "");
        yhEditFindTime.setDate(DateUtil.dateTimeFormat(mYHEntity.findTime));
        yhEditPriority.setSpinner(mYHEntity.priority != null ? mYHEntity.priority.value : "");
        yhEditArea.setSpinner(mYHEntity.areaInstall != null ? mYHEntity.areaInstall.name : "");

        if (mYHEntity.eamID != null && !TextUtils.isEmpty(mYHEntity.eamID.name)) {
            yhEditEamCode.setValue(mYHEntity.eamID.code);
            yhEditEamName.setValue(mYHEntity.eamID.name);
            yhEditEamModel.setValue(mYHEntity.eamID.model);
        }

        yhEditType.setSpinner(mYHEntity.faultInfoType != null ? mYHEntity.faultInfoType.value : "");

        if (mYHEntity.repairType != null) {
            yhEditWXType.setSpinner(mYHEntity.repairType.value);
            if (Constant.YHWXType.JX_SYSCODE.equals(mYHEntity.repairType.id) || Constant.YHWXType.DX_SYSCODE.equals(mYHEntity.repairType.id)) {
                yhEditWXGroup.setEditable(false);
            }
        }
        yhEditWXGroup.setSpinner(mYHEntity.repiarGroup != null ? mYHEntity.repiarGroup.name : "");
        if (!TextUtils.isEmpty(mYHEntity.describe)) {
            yhEditDescription.setInput(mYHEntity.describe);
        }

        getController(OnlineCameraController.class).init(Constant.IMAGE_SAVE_YHPATH, Constant.PicType.YH_PIC);
        if (mYHEntity.attachmentEntities != null) {
            getController(OnlineCameraController.class).setPicData(mYHEntity.attachmentEntities);
        }


        if (!TextUtils.isEmpty(mYHEntity.remark)) {
            yhEditMemo.setInput(mYHEntity.remark);
        }
    }

    @SuppressLint("CheckResult")
    @SuppressWarnings("unchecked")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());

        yhEditArea.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.areaInstall = null;
            } else {
                List<String> list = new ArrayList<>(mAreas.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "区域位置列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            Area area = mAreas.get(item);
                            mYHEntity.areaInstall = area;
                            yhEditArea.setSpinner(item);
                        })
                        .show(yhEditArea.getSpinnerValue());
            }
        });

        yhEditWXGroup.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.repiarGroup = null;
            } else {
                List<String> list = new ArrayList<>(mRepairGroups.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "维修组列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            RepairGroupEntity repairGroupEntity = mRepairGroups.get(item);
                            mYHEntity.repiarGroup = repairGroupEntity;
                            yhEditWXGroup.setSpinner(item);
                        })
                        .show(yhEditWXGroup.getSpinnerValue());
            }
        });

        yhEditType.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.faultInfoType = null;
            } else {
                List<String> list = new ArrayList<>(yhTypes.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "隐患类型列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            SystemCodeEntity yhType = yhTypes.get(item);
                            mYHEntity.faultInfoType = yhType;
                            yhEditType.setSpinner(item);
                        })
                        .show(yhEditType.getSpinnerValue());
            }
        });

        yhEditWXType.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.repairType = null;
                yhEditWXGroup.setEditable(true);
            } else {
                List<String> list = new ArrayList<>(wxTypes.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "维修类型列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            SystemCodeEntity wxType = wxTypes.get(item);
                            mYHEntity.repairType = wxType;
                            yhEditWXType.setSpinner(item);

                            //大修或检修时，维修组不可编辑，若有值清空
                            if (Constant.YHWXType.DX.equals(item) || Constant.YHWXType.JX.equals(item)) {
                                yhEditWXGroup.setEditable(false);
                                yhEditWXGroup.setSpinner(null);
                                mYHEntity.repiarGroup = null;
                            } else {
                                yhEditWXGroup.setEditable(true);
                            }
                        })
                        .show(yhEditWXType.getSpinnerValue());
            }
        });

        yhEditPriority.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.priority = null;
            } else {
                List<String> list = new ArrayList<>(yhPriorities.keySet());
                if (list.size() <= 0) {
                    ToastUtils.show(context, "优先级列表数据为空,请重新加载页面！");
                    return;
                }
                mSinglePickController
                        .list(list)
                        .listener((SinglePicker.OnItemPickListener<String>) (index, item) -> {
                            SystemCodeEntity yhPriority = yhPriorities.get(item);
                            mYHEntity.priority = yhPriority;
                            yhEditPriority.setSpinner(item);
                        })
                        .show(yhEditPriority.getSpinnerValue());
            }
        });

        yhEditFindTime.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.findTime = 0;
            } else {
                mDatePickController
                        .listener((year, month, day, hour, minute, second) -> {
                            LogUtil.i(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
                            String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                            long select = DateUtil.dateFormat(dateStr, "yyyy-MM-dd HH:mm");

                            mYHEntity.findTime = select;
                            yhEditFindTime.setDate(DateUtil.dateTimeFormat(mYHEntity.findTime));

                        })
                        .show(mYHEntity.findTime);
            }
        });

        RxTextView.textChanges(yhEditMemo.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> mYHEntity.remark = charSequence.toString());

        RxTextView.textChanges(yhEditDescription.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> mYHEntity.describe = charSequence.toString());
/*        yhEditGalleryView.setOnChildViewClickListener((childView, action, obj) -> {

            int position = (int) obj;

            if (position == -1) {
//                startCamera();

                if (action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_CAMERA) {
                    getController(BaseCameraController.class).startCamera(Constant.YH_PATH, Constant.PicType.YH_PIC);
                }
                else if (action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_GALLERY) {
                    getController(BaseCameraController.class).startGallery(Constant.YH_PATH, Constant.PicType.YH_PIC);
                }

                return;
            }
            List<GalleryBean> galleryBeans = yhEditGalleryView.getGalleryAdapter().getList();
            GalleryBean galleryBean = galleryBeans.get(position);
            if (action == CustomGalleryView.ACTION_VIEW) {
                Bundle bundle = FaultPicHelper.genRequestBundle(context, childView, galleryBeans, position, true);
                getWindow().setWindowAnimations(R.style.fadeStyle);
                IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);
            } else if (action == CustomGalleryView.ACTION_DELETE) {

                new CustomDialog(context)
                        .twoButtonAlertDialog("是否删除图片?")
                        .bindView(R.id.grayBtn, "取消")
                        .bindView(R.id.redBtn, "确定")
                        .bindClickListener(R.id.grayBtn, v1 -> {
                        }, true)
                        .bindClickListener(R.id.redBtn, v3 -> {
                            yhEditGalleryView.deletePic(position);
                            deleteBitmap(galleryBean);
                        }, true)
                        .show();

            }

        });*/

        yhEditTransition.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                if ("作废".equals(workFlowVar.dec)) {
                    isCancel = true;
                } else {

                    isCancel = false;

                }
                switch (action) {
                    case 0:  //保存
                        doSave();
                        break;
                    case 1:
                        if (checkBeforeSubmit()) {
                            doSubmit(workFlowVar);
                        }
                        break;
                    case 2:
                        if (checkBeforeSubmit()) {
                            doSubmit(workFlowVar);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

//        yhEditTransition.setOnChildViewClickListener((childView, action, obj) -> {
//            String tag = (String) childView.getTag();
//            Transition transition = yhEditTransition.currentTransition();
//
//            if ("作废".equals(transition.transitionDesc)) {
//                isCancel = true;
//            } else {
//
//                isCancel = false;
//
//            }
//
//            switch (tag) {
//
//                case Constant.Transition.SUBMIT_BTN:
//                    if (checkBeforeSubmit()) {
//                        doSubmit(transition);
//                    }
//                    break;
//                case Constant.Transition.SAVE:
//
//                    doSave();
//
//                    break;
//
//                case Constant.Transition.ROUTER_BTN:
//                    if (mLinkController.getLinkEntities() == null) {
//                        iniTransition();
//                    }
//                    break;
//
//                default:
//
//                    break;
//
//            }
//
//        });


        yhEditEamCode.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
                mYHEntity.eamID = null;
                mYHEntity.areaInstall = null;
                yhEditArea.setEditable(true);
                yhEditArea.setSpinner(null);
                yhEditEamName.setValue(null);
                yhEditEamModel.setValue(null);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.MODULE, Module.Fault.name());
//                IntentRouter.go(context, Constant.Router.ADD_DEVICE, bundle);
                bundle.putString(Constant.IntentKey.COMMON_SAERCH_MODE, Constant.CommonSearchMode.EAM);
                bundle.putString(Constant.IntentKey.ENTITY_CODE, yhEditArea.getSpinnerValue());
                IntentRouter.go(context, Constant.Router.COMMON_SEARCH, bundle);
            }
        });


        yhEditFindStaff.setOnChildViewClickListener((childView, action, obj) -> {

            if (action == -1) {
                mYHEntity.findStaffID = null;
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                IntentRouter.go(context, Constant.Router.COMMON_SEARCH, bundle);
            }
        });

/*        getController(BaseCameraController.class).setOnSuccessListener(new OnSuccessListener<File>() {
            @Override
            public void onSuccess(File result) {
                uploadLocalPic(result);
            }
        });*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addDeviceEvent(CommonSearchEvent commonSearchEvent) {
        CommonDeviceEntity commonDeviceEntity = (CommonDeviceEntity) commonSearchEvent.commonSearchEntity;
        if (commonDeviceEntity != null) {
            yhEditEamName.setValue(commonDeviceEntity.eamName);
            yhEditEamCode.setValue(commonDeviceEntity.eamCode);
            yhEditEamModel.setValue(commonDeviceEntity.eamModel);
            WXGDEam wxgdEam = new WXGDEam();
            wxgdEam.name = commonDeviceEntity.eamName;
            wxgdEam.code = commonDeviceEntity.eamCode;
            wxgdEam.model = commonDeviceEntity.eamModel;
            wxgdEam.id = commonDeviceEntity.eamId;
            mYHEntity.eamID = wxgdEam;

            //处理区域位置
            yhEditArea.setEditable(false);
            yhEditArea.setSpinner(commonDeviceEntity.installPlace);
            mYHEntity.areaInstall = mAreas.get(commonDeviceEntity.installPlace);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMaintenanceStaff(CommonSearchEvent commonSearchEvent) {
        UserInfo userInfo = (UserInfo) commonSearchEvent.commonSearchEntity;
        yhEditFindStaff.setValue(userInfo.staffName);
        mYHEntity.findStaffID = new Staff();
        mYHEntity.findStaffID.id = userInfo.staffId;
        mYHEntity.findStaffID.code = userInfo.staffCode;
        mYHEntity.findStaffID.name = userInfo.staffName;
    }

    @Override
    protected void initData() {
        super.initData();

        iniTransition();

        List<SystemCodeEntity> wxTypeEntities = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_WX_TYPE);
        wxTypes = initEntities(wxTypeEntities);

        List<SystemCodeEntity> yhTypeEntities = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.QX_TYPE);
        yhTypes = initEntities(yhTypeEntities);

        List<SystemCodeEntity> yhPriorityEntity = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_PRIORITY);
        yhPriorities = initEntities(yhPriorityEntity);

        List<Area> areaEntities = EamApplication.dao().getAreaDao().queryBuilder().where(AreaDao.Properties.Ip.eq(MBapApp.getIp())).list();
        mAreas = initEntities(areaEntities);

        List<RepairGroupEntity> repairGroupEntities =
                EamApplication.dao().getRepairGroupEntityDao().queryBuilder().where(RepairGroupEntityDao.Properties.IsUse.eq(Boolean.valueOf(true)), RepairGroupEntityDao.Properties.Ip.eq(EamApplication.getIp())).list();
        mRepairGroups = initEntities(repairGroupEntities);
    }

    private void iniTransition() {

        if (mYHEntity.pending == null) {
            mLinkController.initStartTransition(yhEditTransition, "faultInfoFW");
        } else {
            mLinkController.initPendingTransition(yhEditTransition, mYHEntity.pending.id);
        }
    }

    private <T extends BaseEntity> Map<String, T> initEntities(List<T> entities) {

        Map<String, T> map = new LinkedHashMap<>();


        for (T entity : entities) {

            String name = FieldHepler.getFieldValue(entity.getClass(), entity, "name");
            if (!TextUtils.isEmpty(name)) {
                map.put(name, entity);
                continue;
            }

            String value = FieldHepler.getFieldValue(entity.getClass(), entity, "value");
            if (!TextUtils.isEmpty(value)) {
                map.put(value, entity);
            }

        }

        return map;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setWindowAnimations(R.style.activityAnimation);
    }

    @Override
    public void onBackPressed() {
        //退出前校验表单是否修改过
        //如果修改过, 提示是否保存
        if (isModified()) {
            new CustomDialog(context)
                    .twoButtonAlertDialog("页面已经被修改，是否要保存?")
                    .bindView(R.id.grayBtn, "保存")
                    .bindView(R.id.redBtn, "离开")
                    .bindClickListener(R.id.grayBtn, v -> doSave(), true)
                    .bindClickListener(R.id.redBtn, v3 -> {
                        //关闭页面
                        YHEditActivity.this.finish();
                    }, true)
                    .show();
        } else {
            finish();
        }

    }

    /**
     * 提交之前校验必填字段
     *
     * @return
     */
    private boolean checkBeforeSubmit() {

        if (isCancel) {
            return true;
        }

        if (mYHEntity.findStaffID == null || null == mYHEntity.findStaffID.id) {
            SnackbarHelper.showError(rootView, "请填写发现人！");
            return false;
        }
        if (mYHEntity.findTime == 0) {
            SnackbarHelper.showError(rootView, "请填写发现时间！");
            return false;
        }

        if (mYHEntity.priority == null) {
            SnackbarHelper.showError(rootView, "请选择优先级！");
            return false;
        }

        //判断必填字段是否为空
        if (mYHEntity.eamID == null || mYHEntity.eamID.code == null) {
            SnackbarHelper.showError(rootView, "请选择设备");
            return false;
        }

        if (mYHEntity.faultInfoType == null) {
            SnackbarHelper.showError(rootView, "请选择隐患类型！");
            return false;
        }

        if (TextUtils.isEmpty(yhEditDescription.getInput())) {
            SnackbarHelper.showError(rootView, "请填写隐患现象！");
            return false;
        }

        if (mYHEntity.repairType == null) {
            SnackbarHelper.showError(rootView, "请选择维修类型！");
            return false;
        }

//        RoleEntity roleEntity = ((RoleController) getController(Constant.Controller.ROLE)).getRoleEntity(0);
//        if (roleEntity == null) {
//            SnackbarHelper.showError(rootView, "角色为空，请保存！");
//            return false;
//        }

//        if (TextUtils.isEmpty(yhEditTransition.currentTransition().outCome)) {
//            SnackbarHelper.showError(rootView, "请选择操作！");
//            return false;
//        }

        return true;
    }

    private void doSave() {
        hits = true;
        submit(null);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.dec = workFlowVar.dec;
        workFlowEntity.type = workFlowVar.outcomeMapJson.get(0).type;
        workFlowEntity.outcome = workFlowVar.outCome;
        workFlowEntities.add(workFlowEntity);

        submit(workFlowEntities);
    }

    private void submit(List<WorkFlowEntity> workFlowEntities) {
        WorkFlowEntity workFlowEntity = null;
        if (workFlowEntities != null && workFlowEntities.size() != 0) {
            workFlowEntity = workFlowEntities.get(0);
        } else {
            workFlowEntity = new WorkFlowEntity();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("bap_validate_user_id", String.valueOf(EamApplication.getAccountInfo().userId));
        map.put("faultInfo.createStaffId", mYHEntity.findStaffID == null || mYHEntity.findStaffID.checkNil() ? "" : mYHEntity.findStaffID.id);
        map.put("faultInfo.createTime", DateUtil.dateTimeFormat(mYHEntity.findTime));
        map.put("faultInfo.findStaffID.id", mYHEntity.findStaffID == null || mYHEntity.findStaffID.checkNil() ? "" : mYHEntity.findStaffID.id);
        map.put("faultInfo.findTime", DateUtil.dateTimeFormat(mYHEntity.findTime));
        map.put("faultInfo.createPositionId", EamApplication.getAccountInfo().positionId);

        if (mYHEntity.id != 0) {
            map.put("id", mYHEntity.id);
            map.put("faultInfo.id", mYHEntity.id);
        } else {
            map.put("id", "");
            map.put("faultInfo.id", "");
        }

        if (mYHEntity.pending != null && mYHEntity.pending.id != null) {

            map.put("pendingId", mYHEntity.pending.id);
            map.put("deploymentId", mYHEntity.pending.deploymentId);
        } else {
            map.put("deploymentId", deploymentId);
            map.put("faultInfo.version", 1);
        }


        if (mYHEntity.eamID != null && mYHEntity.eamID.id != null) {
            map.put("faultInfo.eamID.id", mYHEntity.eamID.id);
        } else {
            map.put("faultInfo.eamID.id", "");
        }


        if (mYHEntity.areaInstall != null && mYHEntity.areaInstall.id != 0) {
            map.put("faultInfo.areaInstall.id", mYHEntity.areaInstall.id);
        } else {
            map.put("faultInfo.areaInstall.id", "");
        }
        if (mYHEntity.repiarGroup != null && mYHEntity.repiarGroup.id != null) {
            map.put("faultInfo.repiarGroup.id", mYHEntity.repiarGroup.id);
        } else {
            map.put("faultInfo.repiarGroup.id", "");
        }

        if (mYHEntity.faultInfoType != null) {
            map.put("faultInfo.faultInfoType.id", mYHEntity.faultInfoType.id);
            map.put("faultInfo.faultInfoType.value", mYHEntity.faultInfoType.value);
        } else {
            map.put("faultInfo.faultInfoType.id", "");
            map.put("faultInfo.faultInfoType.value", "");
        }

        if (mYHEntity.priority != null) {
            map.put("faultInfo.priority.id", mYHEntity.priority.id);
        } else {
            map.put("faultInfo.priority.id", "");
        }

        if (mYHEntity.repairType != null) {
            map.put("faultInfo.repairType.id", mYHEntity.repairType.id);
        } else {
            map.put("faultInfo.repairType.id", "");
        }

        if (mYHEntity.describe != null)
            map.put("faultInfo.describe", mYHEntity.describe);
        if (mYHEntity.remark != null)
            map.put("faultInfo.remark", mYHEntity.remark);

        map.put("linkId", mYHEntity.tableInfoId != 0 ? mYHEntity.tableInfoId : "");
//        map.put("dlTableInfoId",                mYHEntity.tableInfoId);
        map.put("tableInfoId", mYHEntity.tableInfoId != 0 ? mYHEntity.tableInfoId : "");
        if (workFlowEntities != null) {//保存为空
            map.put("workFlowVar.outcomeMapJson", workFlowEntities);
            map.put("workFlowVar.dec", workFlowEntity.dec);
            map.put("workFlowVar.outcome", workFlowEntity.outcome);
            map.put("operateType", "submit");
        } else {
//            map.put("workFlowVar.dec", "");
//            map.put("workFlowVar.outcome", "");
            map.put("operateType", "save");
        }
        map.put("taskDescription", "BEAM2_1.0.0.faultInfoFW.task342");
        map.put("workFlowVar.comment", yhEditCommentInput.getInput());

        map.put("viewCode", "BEAM2_1.0.0_faultInfo_faultInfoEdit");

        map.put("modelName", "FaultInfo");
        map.put("datagridKey", "BEAM2_faultInfo_faultInfo_faultInfoEdit_datagrids");
        map.put("__file_upload", true);


        Map<String, Object> attachmentMap = new HashMap<>();

//        List<Map<String, Object>> files = new ArrayList<>();
//        for (GalleryBean galleryBean : pics) {
//            Map<String, Object> file = new HashMap<>();
//            file.put("file.filePath", galleryBean.url);
//            file.put("file.name", galleryBean.url.substring(galleryBean.url.lastIndexOf("\\") + 1));
//            file.put("file.memos", "pda隐患拍照上传");
//            file.put("file.fileType", "attachment");
//            files.add(file);
//        }
//
//        if (pics.size() != 0) {
//            attachmentMap.put("file.staffId", String.valueOf(EamApplication.getAccountInfo().staffId));
//            attachmentMap.put("file.type", "Table");
//            attachmentMap.put("files", files);
//            attachmentMap.put("linkId", String.valueOf(mYHEntity.tableInfoId));
//        }
        getController(OnlineCameraController.class).doSave(attachmentMap);
        if(attachmentMap.size()!=0){
            attachmentMap.put("linkId", String.valueOf(mYHEntity.tableInfoId));
        }

        LogUtil.d(GsonUtil.gsonString(map));
        onLoading("单据处理中...");
        presenterRouter.create(YHSubmitAPI.class).doSubmit(map, attachmentMap, true);

    }

    @Override
    public boolean isModified() {
        return checkIsModified() || super.isModified();
    }

    /**
     * 判断是否填写过表单
     */
    private boolean checkIsModified() {
        if (!GsonUtil.gsonString(mYHEntity).equals(mOriginalEntity.toString())) {
            return true;
        }

        return !TextUtils.isEmpty(yhEditCommentInput.getInput());
    }

/*
    private void uploadLocalPic(File file) {
        onLoading("正在上传照片...");
        mAttachmentController.uploadAttachment(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String result) {
                GalleryBean galleryBean = new GalleryBean();
                galleryBean.localPath = file.getAbsolutePath();
                galleryBean.url = result;
                yhEditGalleryView.addGalleryBean(galleryBean);
                onLoadSuccess("照片上传成功！");
                pics.add(galleryBean);
            }
        }, file);
    }

    private void deleteBitmap(GalleryBean galleryBean) {
        if (pics.contains(galleryBean)) {
            pics.remove(galleryBean);
//            return;
        }

        String picPath = galleryBean.url;

        if (mYHEntity.attachmentEntities != null)
            for (AttachmentEntity attachmentEntity : mYHEntity.attachmentEntities) {
                if (attachmentEntity.path.equals(picPath)) {
                    mAttachmentController.deleteAttachment(result ->
                            SnackbarHelper.showMessage(rootView, "删除附件成功！"), attachmentEntity.id);
                }
            }
    }*/

    @Override
    public void doSubmitSuccess(BapResultEntity entity) {
        LogUtil.d("entity:" + entity);

        RefreshEvent refreshEvent = new RefreshEvent();
        if (isCancel) {
            refreshEvent.action = Constant.Transition.CANCEL;
            isCancel = false;
        }
        EventBus.getDefault().post(refreshEvent);

        if (hits) {
            hits = false;
            onLoadSuccessAndExit("保存成功！", this::finish);
        } else {
            onLoadSuccessAndExit("处理成功！", this::finish);
        }
    }

    @Override
    public void doSubmitFailed(String errorMsg) {
        LogUtil.e("errorMsg:" + errorMsg);
        hits = false;
        onLoadFailed(errorMsg);
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
    }
}
