package com.supcon.mes.module_wxgd.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.module_wxgd.R;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/5
 * ------------- Description -------------
 * 备件添加选择列表Adapter
 */
public class RefProductAdapter extends BaseListDataRecyclerViewAdapter<Good> {
    public RefProductAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<Good> getViewHolder(int viewType) {
        return new RefProductViewHolder(context);
    }

    class RefProductViewHolder extends BaseRecyclerViewHolder<Good> {
        @BindByTag("layout_refproduct")
        RelativeLayout layout_refproduct;
        @BindByTag("productName")
        TextView productName;
        @BindByTag("productSpecif")
        TextView productSpecif;
        @BindByTag("productCode")
        TextView productCode;

        public RefProductViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
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
        protected void update(Good data) {
            productName.setText(data.productName);
            productSpecif.setText(data.productSpecif);
            productCode.setText(data.productCode);
        }
    }

}
