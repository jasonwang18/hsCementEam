package com.supcon.mes.module_wxgd.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.api.AcceptanceCheckAPI;
import com.supcon.mes.module_wxgd.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.module_wxgd.model.bean.AcceptanceCheckListEntity;
import com.supcon.mes.module_wxgd.model.bean.WXGDEntity;
import com.supcon.mes.module_wxgd.model.contract.AcceptanceCheckContract;
import com.supcon.mes.module_wxgd.presenter.AcceptanceCheckPresenter;
import com.supcon.mes.module_wxgd.ui.adapter.AcceptanceCheckAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@Presenter(AcceptanceCheckPresenter.class)
public class AcceptanceCheckController extends BaseViewController implements AcceptanceCheckContract.View {

    @BindByTag("acceptanceCheckListWidget")
    CustomListWidget<AcceptanceCheckEntity> mCustomListWidget;

//    private CustomListWidget<AcceptanceCheckEntity> mCustomListWidget;
    private WXGDEntity mWxgdEntity;
    private long id;
    private List<AcceptanceCheckEntity> mAcceptanceCheckEntities = new ArrayList<>();
    private boolean isEditable;
    private long repairSum;
    private List<AcceptanceCheckEntity> mAcceptanceCheckHistoryEntities = new ArrayList<>(); // 历史验收数据
    private List<AcceptanceCheckEntity> mAcceptanceCheckCurrentEntities = new ArrayList<>(); // 当次验收数据

    private OnLastItemListener onLastItemListener;

    public void setOnLastItemListener(OnLastItemListener onLastItemListener) {
        this.onLastItemListener = onLastItemListener;
    }

    public AcceptanceCheckController(View rootView) {
        super(rootView);
    }
    @Override
    public void onInit() {
        super.onInit();
        mWxgdEntity = (WXGDEntity) ((Activity)context).getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);
        this.id = mWxgdEntity.id;
        this.repairSum = mWxgdEntity.repairSum;
    }

    @Override
    public void initView() {
        super.initView();
        mCustomListWidget.setAdapter(new AcceptanceCheckAdapter(context,isEditable));
    }

    @Override
    public void initListener() {
        super.initListener();
        mCustomListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                switch (action) {
                    case CustomListWidget.ACTION_VIEW_ALL:
                    case 0:
                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, false);
                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
                        bundle.putString(Constant.IntentKey.ACCEPTANCE_ENTITIES, mAcceptanceCheckHistoryEntities.toString());
                        IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE_LIST, bundle);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void listAcceptanceCheckListSuccess(AcceptanceCheckListEntity entity) {
        for (AcceptanceCheckEntity acceptanceCheckEntity : entity.result) {
            if (acceptanceCheckEntity.remark == null) {
                acceptanceCheckEntity.remark = "";
            }
            if (acceptanceCheckEntity.timesNum == repairSum) {
                mAcceptanceCheckCurrentEntities.add(acceptanceCheckEntity);
            } else {
                mAcceptanceCheckHistoryEntities.add(acceptanceCheckEntity);
            }
        }
        mAcceptanceCheckEntities = entity.result;
        if (mCustomListWidget != null) {
//            mCustomListWidget.setData(mAcceptanceCheckHistoryEntities);
//            mCustomListWidget.setTotal(mAcceptanceCheckHistoryEntities.size());
            if (isEditable) {
                mCustomListWidget.setShowText("编辑 (" + mAcceptanceCheckHistoryEntities.size() + ")");
            } else {
                mCustomListWidget.setShowText("查看 (" + mAcceptanceCheckHistoryEntities.size() + ")");
            }
        }
        if (onLastItemListener != null) {
            onLastItemListener.getLastItem(mAcceptanceCheckCurrentEntities);
        }
    }

    @Override
    public void listAcceptanceCheckListFailed(String errorMsg) {
        LogUtil.e("AcceptanceCheckController listAcceptanceCheckListFailed:" + errorMsg);
    }

    public void setCustomListWidget(CustomListWidget<AcceptanceCheckEntity> customListWidget) {
        this.mCustomListWidget = customListWidget;
    }

    @Override
    public void initData() {
        super.initData();
        presenterRouter.create(AcceptanceCheckAPI.class).listAcceptanceCheckList(id);
    }

    /**
     * @param
     * @return
     * @description 获取历史验收列表数据
     * @author zhangwenshuai1 2018/9/10
     */
    public List<AcceptanceCheckEntity> getAcceptanceCheckEntities() {
        if (mAcceptanceCheckHistoryEntities == null) {
            return new ArrayList<>();
        }
        return mAcceptanceCheckHistoryEntities;
    }

    /**
     * @param
     * @return
     * @description 更新列表数据
     * @author zhangwenshuai1 2018/9/10
     */
    public void updateAcceptanceCheckEntities(List<AcceptanceCheckEntity> list) {

        if (list == null) {
            return;
        }
        mAcceptanceCheckEntities = list;

        if (mCustomListWidget != null) {
            mCustomListWidget.setData(mAcceptanceCheckEntities);
//            mCustomListWidget.setTotal(list.size());
            if (isEditable) {
                mCustomListWidget.setShowText("编辑 (" + list.size() + ")");
            } else {
                mCustomListWidget.setShowText("查看 (" + list.size() + ")");
            }
        }
    }

    /**
     * @param
     * @return
     * @description 获取当前验收列表数据
     * @author zhangwenshuai1 2018/9/10
     */
    public List<AcceptanceCheckEntity> getCurrentAcceptChkEntitiesListLast() {
        if (mAcceptanceCheckCurrentEntities == null) {
            return new ArrayList<>();
        }

        return GsonUtil.jsonToList(mAcceptanceCheckCurrentEntities.toString(), AcceptanceCheckEntity.class);
    }

    public interface OnLastItemListener {
        void getLastItem(List<AcceptanceCheckEntity> list);
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }


}
