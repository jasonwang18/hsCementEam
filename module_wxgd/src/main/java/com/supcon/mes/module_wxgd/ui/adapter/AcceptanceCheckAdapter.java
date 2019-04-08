package com.supcon.mes.module_wxgd.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.bean.AcceptanceCheckEntity;

public class AcceptanceCheckAdapter extends BaseListDataRecyclerViewAdapter<AcceptanceCheckEntity> {

    private boolean isEditable;
    private long repairSum;

    public AcceptanceCheckAdapter(Context context, boolean isEditable) {
        super(context);
        this.isEditable = isEditable;
    }

    @Override
    protected BaseRecyclerViewHolder<AcceptanceCheckEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public void setRepairSum(long repairSum) {
        this.repairSum = repairSum;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    class ViewHolder extends BaseRecyclerViewHolder<AcceptanceCheckEntity> implements OnChildViewClickListener {

        @BindByTag("index")
        TextView index;
        @BindByTag("timesNum")
        TextView timesNum;
        //        @BindByTag("acceptanceDelete")
//        ImageView acceptanceDelete;
        @BindByTag("acceptanceStaffName")
        CustomVerticalTextView acceptanceStaffName;
        @BindByTag("acceptanceStaffCode")
        CustomVerticalTextView acceptanceStaffCode;
        @BindByTag("acceptanceTime")
        CustomVerticalDateView acceptanceTime;
        @BindByTag("acceptanceResult")
        CustomVerticalSpinner acceptanceResult;
        @BindByTag("remark")
        CustomVerticalEditText remark;
        @BindByTag("chkBox")
        CheckBox chkBox;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_acceptance;
        }

        @Override
        protected void initView() {
            super.initView();
            chkBox.setVisibility(View.GONE);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();

            acceptanceStaffName.setOnChildViewClickListener(this);

//            RxTextView.textChanges(remark.editText()).subscribe(charSequence -> {
//                AcceptanceCheckEntity acceptanceCheckEntity = getItem(getAdapterPosition());
//                acceptanceCheckEntity.remark = charSequence.toString();
//            });

            acceptanceTime.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    onItemChildViewClick(acceptanceTime, 0, getItem(getAdapterPosition()));
                }
            });
        }

        @Override
        protected void update(AcceptanceCheckEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            timesNum.setText(String.format(context.getResources().getString(R.string.acceptanceTimes), String.valueOf(getAdapterPosition() + 1)));

            if (data.checkStaff != null) {
                acceptanceStaffName.setValue(data.checkStaff.name);
                acceptanceStaffCode.setValue(data.checkStaff.code);
            } else {
                acceptanceStaffName.setValue("");
                acceptanceStaffCode.setValue("");
            }
            acceptanceTime.setDate(data.checkTime == null ? "" : DateUtil.dateTimeFormat(data.checkTime));

            if (data.checkResult != null){
                acceptanceResult.setSpinner(data.checkResult.value);
            }else {
                acceptanceResult.setSpinner("");
            }

            timesNum.setText(String.format(context.getResources().getString(R.string.wxTimes), String.valueOf(data.timesNum)));

        }

        @Override
        public void onChildViewClick(View childView, int action, Object obj) {
            AcceptanceCheckEntity acceptanceCheckEntity = getItem(getAdapterPosition());
            onItemChildViewClick(childView, 0, acceptanceCheckEntity);
        }
    }

}
