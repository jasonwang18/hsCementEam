package com.supcon.mes.module_warn.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.bean.LubricationWarnEntity;

import java.text.SimpleDateFormat;


/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class DailyLubricationWarnAdapter extends BaseListDataRecyclerViewAdapter<LubricationWarnEntity> {

    private int checkPosition = -1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public DailyLubricationWarnAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position, LubricationWarnEntity lubricationWarnEntity) {
        return lubricationWarnEntity.viewType;
    }

    @Override
    protected BaseRecyclerViewHolder<LubricationWarnEntity> getViewHolder(int viewType) {
        if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        }
        return new ViewHolder(context);
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<LubricationWarnEntity> {

        @BindByTag("itemEquipmentNameTv")
        CustomTextView itemEquipmentNameTv;


        public TitleViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_daily_lubri_warn_title;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void update(LubricationWarnEntity data) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getEamID().name)
                    , Util.strFormat(data.getEamID().code));
            itemEquipmentNameTv.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder<LubricationWarnEntity> {

        @BindByTag("itemLubriOilTv")
        CustomTextView itemLubriOilTv;
        @BindByTag("itemLubriChangeTv")
        TextView itemLubriChangeTv;
        @BindByTag("itemLubriNumTv")
        TextView itemLubriNumTv;
        @BindByTag("itemLubriPartTv")
        CustomTextView itemLubriPartTv;
        @BindByTag("itemLubriLastDateTv")
        CustomTextView itemLubriLastDateTv;
        @BindByTag("itemLubriNextDateTv")
        CustomTextView itemLubriNextDateTv;
        @BindByTag("itemLubriLastDurationTv")
        CustomTextView itemLubriLastDurationTv;
        @BindByTag("itemLubriNextDurationTv")
        CustomTextView itemLubriNextDurationTv;
        @BindByTag("itemLubriClaimTv")
        CustomTextView itemLubriClaimTv;
        @BindByTag("itemLubriContentTv")
        CustomTextView itemLubriContentTv;

        @BindByTag("itemLubriAttachEamTv")
        CustomTextView itemLubriAttachEamTv;
        @BindByTag("itemLubriSparePartIdTv")
        CustomTextView itemLubriSparePartIdTv;

        @BindByTag("chkBox")
        CheckBox chkBox;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_daily_lubri_warn;
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LubricationWarnEntity item = getItem(getAdapterPosition());
                    item.isCheck = !item.isCheck;
                    notifyItemChanged(getAdapterPosition());
                    if (checkPosition != -1) {
                        if (checkPosition != getAdapterPosition()) {
                            LubricationWarnEntity item1 = getItem(checkPosition);
                            item1.isCheck = false;
                        }
                        notifyItemChanged(checkPosition);
                    }
                    checkPosition = getAdapterPosition();
                    onItemChildViewClick(itemView, checkPosition, getItem(checkPosition));
                }
            });
        }

        @Override
        protected void update(LubricationWarnEntity data) {

            itemLubriOilTv.setContent(data.getLubricateOil().name);
            itemLubriChangeTv.setText(String.format(context.getString(R.string.device_style1), "加/换油:", Util.strFormat(data.getOilType().value)));
            itemLubriNumTv.setText(String.format(context.getString(R.string.device_style1), "用量:", Util.big2(data.sum)));
            itemLubriPartTv.setValue(Util.strFormat(data.lubricatePart));
            itemLubriLastDurationTv.setVisibility(View.GONE);
            itemLubriNextDurationTv.setVisibility(View.GONE);
            itemLubriLastDateTv.setVisibility(View.GONE);
            itemLubriNextDateTv.setVisibility(View.GONE);
            if (data.isDuration()) {
                itemLubriLastDurationTv.setVisibility(View.VISIBLE);
                itemLubriNextDurationTv.setVisibility(View.VISIBLE);
                itemLubriLastDurationTv.setValue(Util.big2(data.lastDuration));
                itemLubriNextDurationTv.setValue(Util.big2(data.nextDuration));
                if (data.currentDuration > data.nextDuration) {
                    itemLubriNextDurationTv.setContentTextColor(context.getResources().getColor(R.color.customRed));
                }
            } else {
                itemLubriLastDateTv.setVisibility(View.VISIBLE);
                itemLubriNextDateTv.setVisibility(View.VISIBLE);
                itemLubriLastDateTv.setValue(data.lastTime != null ? dateFormat.format(data.lastTime) : "");
                itemLubriNextDateTv.setValue(data.nextTime != null ? dateFormat.format(data.nextTime) : "");
                long currentTime = System.currentTimeMillis();
                if (data.nextTime < currentTime) {
                    itemLubriNextDateTv.setContentTextColor(context.getResources().getColor(R.color.customRed));
                }
            }

            if (!TextUtils.isEmpty(data.getAccessoryEamId().getAttachEamId().code)) {
                itemLubriAttachEamTv.setVisibility(View.VISIBLE);
                itemLubriAttachEamTv.setContent(data.getAccessoryEamId().getAttachEamId().code);
            } else {
                itemLubriAttachEamTv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(data.getSparePartId().getProductID().productCode)) {
                itemLubriSparePartIdTv.setVisibility(View.VISIBLE);
                itemLubriSparePartIdTv.setContent(data.getSparePartId().getProductID().productCode);
            } else {
                itemLubriSparePartIdTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.claim)) {
                itemLubriClaimTv.setVisibility(View.VISIBLE);
                itemLubriClaimTv.setContent(data.claim);
            } else {
                itemLubriClaimTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.content)) {
                itemLubriContentTv.setVisibility(View.VISIBLE);
                itemLubriContentTv.setContent(data.content);
            } else {
                itemLubriContentTv.setVisibility(View.GONE);
            }

            if (data.isCheck) {
                chkBox.setChecked(true);
            } else {
                chkBox.setChecked(false);
            }
        }
    }
}
