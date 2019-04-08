package com.supcon.mes.module_wxgd.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.module_wxgd.model.bean.SparePartEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.ui.adapter.SparePartAdapter;

import java.util.List;

public class SparePartFragment extends BaseRefreshRecyclerFragment<SparePartEntity> {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("addSparePartItem")
    Button addSparePartItem;

    private SparePartAdapter sparePartAdapter;
    private List<SparePartEntity> sparePartEntities;

    @Override
    protected IListAdapter createAdapter() {
        sparePartAdapter = new SparePartAdapter(context, false);
        return sparePartAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_spareparts;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setAutoPullDownRefresh(false);
    }

    @Override
    protected void initView() {
        super.initView();

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(8));

        initEmptyView();
    }

    /**
     * @param
     * @return
     * @description 初始化无数据
     * @author zhangwenshuai1 2018/8/17
     */
    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
    }

    @Override
    protected void initData() {
        super.initData();

        refreshListController.refreshComplete(sparePartEntities);
    }

    @Override
    protected void initListener() {
        super.initListener();

        addSparePartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO...动态添加备件item
                SparePartEntity sparePartEntity = new SparePartEntity();
                sparePartAdapter.addData(sparePartEntity);
                sparePartAdapter.notifyItemInserted(0);

                sparePartAdapter.notifyDataSetChanged();
//                EventBus.getDefault().post(new RefreshEvent());
                refreshListController.refreshComplete(sparePartEntities);

            }
        });

        sparePartAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {

                String tag = (String) childView.getTag();
                SparePartEntity sparePartEntity = (SparePartEntity) obj;

                switch (tag) {
                    case "sum":
                        break;
                    case "remark":
                        break;
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setSparePartFragmentData(List<SparePartEntity> list) {
        sparePartEntities = list;
    }

}
