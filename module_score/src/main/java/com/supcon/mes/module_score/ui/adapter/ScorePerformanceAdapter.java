package com.supcon.mes.module_score.ui.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.bean.ScorePerformanceEntity;
import com.supcon.mes.module_score.ui.view.FlowLayout;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class ScorePerformanceAdapter extends BaseListDataRecyclerViewAdapter<ScorePerformanceEntity> {

    private boolean isEdit = false;

    public ScorePerformanceAdapter(Context context) {
        super(context);
    }

    public void setEditable(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @Override
    protected BaseRecyclerViewHolder<ScorePerformanceEntity> getViewHolder(int viewType) {
        if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        } else if (viewType == ListType.HEADER.value()) {
            return new HeadViewHolder(context);
        }
        return new ViewHolder(context);
    }

    @Override
    public int getItemViewType(int position, ScorePerformanceEntity scorePerformanceEntity) {
        return scorePerformanceEntity.viewType;
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<ScorePerformanceEntity> {

        @BindByTag("contentTitle")
        TextView contentTitle;
        @BindByTag("fraction")
        TextView fraction;

        public TitleViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_title;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();

        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void update(ScorePerformanceEntity data) {
            contentTitle.setText(data.scoreStandard);
            fraction.setText(data.getTotalScore() + "分");
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder<ScorePerformanceEntity> implements CompoundButton.OnCheckedChangeListener {

        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("scoreItem")
        TextView scoreItem;

        @BindByTag("scoreRadioGroup")
        RadioGroup scoreRadioGroup;
        @BindByTag("scoreRadioBtn1")
        RadioButton scoreRadioBtn1;
        @BindByTag("scoreRadioBtn2")
        RadioButton scoreRadioBtn2;

        @BindByTag("checkLayout")
        FlowLayout checkLayout;


        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_content;
        }

        @Override
        protected void initListener() {
            super.initListener();
            scoreRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (scoreRadioBtn1.isPressed() || scoreRadioBtn2.isPressed()) {
                        ScorePerformanceEntity item = getItem(getLayoutPosition());
                        item.result = !item.result;
                        if (item.result) {
                            item.scorePerformanceEntity.setTotalScore(item.scorePerformanceEntity.getTotalScore() + item.score);
                        } else {
                            item.scorePerformanceEntity.setTotalScore(item.scorePerformanceEntity.getTotalScore() - item.score);
                        }
                        if (item.scorePerformanceEntity.getTotalScore() < 0) {
                            item.scorePerformanceEntity.setTotalScore(0);
                        } else if (item.scorePerformanceEntity.getTotalScore() > item.scorePerformanceEntity.defaultTotalScore) {
                            item.scorePerformanceEntity.setTotalScore(item.scorePerformanceEntity.defaultTotalScore);
                        }
                        List<ScorePerformanceEntity> list = getList();
                        int position = list.indexOf(item.scorePerformanceEntity);
                        notifyItemChanged(position);
                    }
                }
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(ScorePerformanceEntity data) {
            itemIndex.setText(data.Index + ".");
            scoreItem.setText(data.itemDetail + (data.marks.size() > 0 ? "" : "(" + data.score + ")"));
            checkLayout.removeAllViews();
            scoreRadioGroup.setVisibility(View.VISIBLE);
            checkLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(data.isItemValue) && !TextUtils.isEmpty(data.noItemValue)) {
                scoreRadioBtn1.setText(data.isItemValue);
                scoreRadioBtn2.setText(data.noItemValue);
                scoreRadioBtn1.setChecked(data.result);
                scoreRadioBtn2.setChecked(!data.result);
                checkLayout.setVisibility(View.GONE);
            } else {
                scoreRadioGroup.setVisibility(View.GONE);
                addview(context, checkLayout, data.marks, data.marksState);
            }
            scoreRadioBtn1.setEnabled(isEdit);
            scoreRadioBtn2.setEnabled(isEdit);
        }

        @SuppressLint("CheckResult")
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ScorePerformanceEntity item = getItem(getLayoutPosition());
            Boolean result = item.marksState.get(buttonView.getText().toString());
            result = !result;
            item.marksState.put(buttonView.getText().toString(), result);
            if (result) {
                item.scorePerformanceEntity.setTotalScore(item.scorePerformanceEntity.getTotalScore() - item.marks.get(buttonView.getText().toString()));
            } else {
                item.scorePerformanceEntity.setTotalScore(item.scorePerformanceEntity.getTotalScore() + item.marks.get(buttonView.getText().toString()));
            }
            if (item.scorePerformanceEntity.getTotalScore() < 0) {
                item.scorePerformanceEntity.setTotalScore(0);
            } else if (item.scorePerformanceEntity.getTotalScore() > item.scorePerformanceEntity.defaultTotalScore) {
                item.scorePerformanceEntity.setTotalScore(item.scorePerformanceEntity.defaultTotalScore);
            }
            List<ScorePerformanceEntity> list = getList();
            int position = list.indexOf(item.scorePerformanceEntity);
            notifyItemChanged(position);
        }

        //动态添加视图
        public void addview(Context context, FlowLayout layout, Map<String, Integer> marks, Map<String, Boolean> marksState) {
            int index = 0;
            for (Map.Entry<String, Integer> mark : marks.entrySet()) {
                String ss = mark.getKey();
                CheckBox checkBox = new CheckBox(context);
                checkBox.setEnabled(isEdit);
                checkBox.setChecked(marksState.get(ss) != null ? marksState.get(ss) : false);
                setRaidBtnAttribute(context, checkBox, ss, index);
                layout.addView(checkBox);
                index++;
            }
        }

        @SuppressLint("ResourceType")
        private void setRaidBtnAttribute(Context context, CheckBox checkBox, String btnContent, int id) {
            if (null == checkBox) {
                return;
            }
            checkBox.setButtonDrawable(R.drawable.sl_checkbox_selector_small);
            checkBox.setTextColor(context.getResources().getColorStateList(R.drawable.tvbg_tag_item));
            checkBox.setId(id);
            checkBox.setText(btnContent);
            checkBox.setTextSize(14);
            checkBox.setGravity(Gravity.CENTER);
            checkBox.setPadding(Util.dpToPx(context, 5), Util.dpToPx(context, 5), Util.dpToPx(context, 5), Util.dpToPx(context, 5));
            checkBox.setOnCheckedChangeListener(this::onCheckedChanged);
            ViewGroup.MarginLayoutParams rlp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(rlp);
        }
    }

    class HeadViewHolder extends BaseRecyclerViewHolder<ScorePerformanceEntity> {

        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("scoreItem")
        TextView scoreItem;
        @BindByTag("scoreRight")
        TextView scoreRight;
        @BindByTag("expend")
        ImageView expend;


        @BindByTag("titleLayout")
        RelativeLayout titleLayout;
        @BindByTag("timeLayout")
        LinearLayout timeLayout;
        @BindByTag("cumulativeRunTime")
        TextView cumulativeRunTime;
        @BindByTag("cumulativeDownTime")
        TextView cumulativeDownTime;


        public HeadViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_head;
        }

        @Override
        protected void initListener() {
            super.initListener();
            titleLayout.setOnClickListener(v -> {
                if (timeLayout.getVisibility() == View.VISIBLE) {
                    rotationExpandIcon(90, 0);
                    timeLayout.setVisibility(View.GONE);
                } else {
                    rotationExpandIcon(0, 90);
                    timeLayout.setVisibility(View.VISIBLE);
                }
            });
        }

        @SuppressLint({"SetTextI18n", "StringFormatInvalid", "StringFormatMatches"})
        @Override
        protected void update(ScorePerformanceEntity data) {
            itemIndex.setText(data.Index + ".");
            scoreItem.setText(data.itemDetail + (data.marks.size() > 0 ? "" : "(" + data.score + ")"));

            scoreRight.setText(Util.big2(data.resultValue) + "%");

            String runTime = String.format(context.getString(R.string.device_style14), "累计运行时间:"
                    , Util.strFormat2(data.totalRunTime));
            cumulativeRunTime.setText(HtmlParser.buildSpannedText(runTime, new HtmlTagHandler()));

            String stopTime = String.format(context.getString(R.string.device_style14), "累计停机时间:"
                    , Util.strFormat2(data.accidentStopTime));
            cumulativeDownTime.setText(HtmlParser.buildSpannedText(stopTime, new HtmlTagHandler()));
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private void rotationExpandIcon(float from, float to) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
                valueAnimator.setDuration(500);
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.addUpdateListener(valueAnimator1 -> expend.setRotation((Float) valueAnimator1.getAnimatedValue()));
                valueAnimator.start();
            }
        }
    }
}
