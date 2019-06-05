package com.supcon.mes.module_score.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.bean.ScoreEntity;

public class ScoreListAdapter extends BaseListDataRecyclerViewAdapter<ScoreEntity> {
    public ScoreListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ScoreEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<ScoreEntity> {

        @BindByTag("itemScoreEam")
        CustomTextView itemScoreEam;
        @BindByTag("itemScoreNum")
        CustomTextView itemScoreNum;
        @BindByTag("itemScoreTime")
        CustomTextView itemScoreTime;
        @BindByTag("itemScoreStaff")
        CustomTextView itemScoreStaff;
        @BindByTag("itemScoreDept")
        CustomTextView itemScoreDept;

        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score;
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> onItemChildViewClick(itemView, 0, getItem(getLayoutPosition())));
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(ScoreEntity data) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getBeamId().name)
                    , Util.strFormat(data.getBeamId().code));
            itemScoreEam.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));

            itemScoreNum.setValue(Util.strFormat2(data.scoreNum));
            itemScoreTime.setValue(data.scoreTime != null ? DateUtil.dateTimeFormat(data.scoreTime) : "");
            itemScoreStaff.setContent(Util.strFormat(data.getScoreStaff().name));
            itemScoreDept.setContent(Util.strFormat(data.getBeamId().getUseDept().name));

        }
    }
}
