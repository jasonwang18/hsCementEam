package com.supcon.mes.module_warn.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.bean.SparePartWarnEntity;

import java.text.SimpleDateFormat;


/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class SparePartWarnAdapter extends BaseListDataRecyclerViewAdapter<SparePartWarnEntity> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public SparePartWarnAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SparePartWarnEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SparePartWarnEntity> {

        @BindByTag("itemSpareEquipmentNameTv")
        CustomTextView itemSpareEquipmentNameTv;
        @BindByTag("itemSpareProductNameTv")
        CustomTextView itemSpareProductNameTv;
        @BindByTag("itemSpareSpecifModelTv")
        CustomTextView itemSpareSpecifModelTv;
        @BindByTag("itemSpareSpecifLastDateTv")
        CustomTextView itemSpareSpecifLastDateTv;
        @BindByTag("itemSpareSpecifNextDateTv")
        CustomTextView itemSpareSpecifNextDateTv;
        @BindByTag("itemSpareSpecifAdvanceTv")
        CustomTextView itemSpareSpecifAdvanceTv;
        @BindByTag("itemMaintenanceMemoTv")
        CustomTextView itemMaintenanceMemoTv;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sparepart_warn;
        }


        @Override
        protected void update(SparePartWarnEntity data) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getEamID().name)
                    , Util.strFormat(data.getEamID().code));
            itemSpareEquipmentNameTv.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));

            String product = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getProductID().productName)
                    , Util.strFormat(data.getProductID().productCode));
            itemSpareProductNameTv.contentView().setText(HtmlParser.buildSpannedText(product, new HtmlTagHandler()));

            String specifModel = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getProductID().productSpecif)
                    , Util.strFormat(data.getProductID().productModel));
            itemSpareSpecifModelTv.contentView().setText(HtmlParser.buildSpannedText(specifModel, new HtmlTagHandler()));

            itemSpareSpecifLastDateTv.setValue(data.lastTime != null ? dateFormat.format(data.lastTime) : "");
            itemSpareSpecifNextDateTv.setValue(data.nextTime != null ? dateFormat.format(data.nextTime) : "");
            itemSpareSpecifAdvanceTv.setContent(data.advanceTime != null ? String.valueOf(data.advanceTime) : "");

            if (!TextUtils.isEmpty(data.spareMemo)) {
                itemMaintenanceMemoTv.setVisibility(View.VISIBLE);
                itemMaintenanceMemoTv.setContent(data.spareMemo);
            }
        }
    }
}
