package com.supcon.mes.module_wxgd.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.UserInfo;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.bean.RepairStaffEntity;
import com.supcon.mes.module_wxgd.ui.adapter.RepairStaffAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

public class RepairStaffFragment extends BaseRefreshRecyclerFragment<RepairStaffEntity> {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("addRepairStaffItem")
    Button addRepairStaffItem;
    @BindByTag("moreSparePart")
    Button moreSparePart;

    private RepairStaffAdapter repairStaffAdapter;
    private List<RepairStaffEntity> repairStaffEntities;

    private DatePickController mDatePickController;

    @Override
    protected IListAdapter createAdapter() {
        repairStaffAdapter = new RepairStaffAdapter(context, false);
        return repairStaffAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_repairstaff;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setAutoPullDownRefresh(false);

        mDatePickController = new DatePickController(this.getActivity());
        mDatePickController.setCycleDisable(false);
        mDatePickController.textSize(18);
    }

    @Override
    protected void initView() {
        super.initView();

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(8));

        initEmptyView();
    }

    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
    }

    @Override
    protected void initData() {
        super.initData();

        refreshListController.refreshComplete(repairStaffEntities);
    }

    @Override
    protected void initListener() {
        super.initListener();

        addRepairStaffItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentRouter.go(context,Constant.Router.COMMON_SEARCH,new Bundle());
            }
        });

        repairStaffAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                String tag = (String) childView.getTag();
                RepairStaffEntity repairStaffEntity = (RepairStaffEntity) obj;
                switch (tag) {
                    case "repairStaffName":
                        IntentRouter.go(context, Constant.Router.COMMON_SEARCH);
                        break;
                    case "actualStartTime":

                        mDatePickController.listener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                            @Override
                            public void onDateTimePicked(String year, String month, String day, String hour, String minute, String second) {
                                String dateTime = year +"-"+month+"-"+day+ " "+ hour+":"+minute;
                                long select = DateUtil.dateFormat(dateTime, "yyyy-MM-dd HH:mm");
                                repairStaffEntity.startTime = select;

                                repairStaffAdapter.notifyItemChanged(position);
                            }
                        }).show(repairStaffEntity.startTime == 0? new Date().getTime():repairStaffEntity.startTime);

                        break;
                    case "actualEndTime":
                        mDatePickController.listener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                            @Override
                            public void onDateTimePicked(String year, String month, String day, String hour, String minute, String second) {
                                String dateTimeStr = year +"-"+month+"-"+day+ " "+ hour+":"+minute;
                                long select = DateUtil.dateFormat(dateTimeStr, "yyyy-MM-dd HH:mm");
                                repairStaffEntity.endTime = select;

                                repairStaffAdapter.notifyItemChanged(position);
                            }
                        }).show(repairStaffEntity.endTime == 0? new Date().getTime(): repairStaffEntity.endTime);
                        break;
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStaffInfo(UserInfo staffInfo){
        LogUtil.d("--------",staffInfo.name);
        //TODO... 赋值实体-----》unDo

        RepairStaffEntity repairStaffEntity = new RepairStaffEntity();
        Staff repairStaff = new Staff();
        repairStaff.id = staffInfo.id;
        repairStaff.name = staffInfo.name;
        repairStaffEntity.repairStaff = repairStaff;
        repairStaffEntity.startTime = new Date().getTime();
        repairStaffAdapter.addData(repairStaffEntity);
        repairStaffAdapter.notifyItemInserted(0);
        repairStaffAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    public void setRepairStaffFragment(List<RepairStaffEntity> list) {
        repairStaffEntities = list;
    }
}
