package com.supcon.mes.module_wxgd.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.model.bean.LubricateOil;
import com.supcon.mes.module_wxgd.R;

/**
 * @author yangfei.cao
 * @ClassName LubricateAdapter
 * @date 2018/9/5
 * 润滑油添加选择列表Adapter
 * ------------- Description -------------
 */
public class LubricateAdapter extends BaseListDataRecyclerViewAdapter<LubricateOil> {
    public LubricateAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<LubricateOil> getViewHolder(int viewType) {
        return new RefProductViewHolder(context);
    }

    class RefProductViewHolder extends BaseRecyclerViewHolder<LubricateOil> {
        @BindByTag("layout_refproduct")
        RelativeLayout layout_refproduct;
        @BindByTag("productName")
        TextView productName;
        @BindByTag("productSpecif")
        TextView productSpecif;
        @BindByTag("productCode")
        TextView productCode;
        @BindByTag("eamIc")
        ImageView eamIc;

        public RefProductViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemChildViewClick(v,0,null);
//                }
//            });
            layout_refproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(v, 0);
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_product;
        }

        @Override
        protected void update(LubricateOil data) {
            eamIc.setImageResource(R.drawable.ic_lubricate_oil);
            productName.setText(data.name);
            productSpecif.setText(data.specify);
            productCode.setText(data.code);
        }
    }

}
