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
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.middleware.model.bean.LubricateOilsEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.ui.adapter.LubricateOilsAdapter;

import java.util.ArrayList;
import java.util.List;

public class LubricateOilsFragment extends BaseRefreshRecyclerFragment<LubricateOilsEntity> {

    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("addOilsItem")
    Button addOilsItem;
    @BindByTag("moreOils")
    Button moreOils;

    private LubricateOilsAdapter lubricateOilsAdapter;
    private List<LubricateOilsEntity> lubricateOilsEntities;
    private SinglePickController mSinglePickController;
    private List<String> oilType = new ArrayList<>();


    @Override
    protected IListAdapter createAdapter() {
        lubricateOilsAdapter = new LubricateOilsAdapter(context, false);
        return lubricateOilsAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_lubricate_oils;
    }

    @Override
    protected void onInit() {
        super.onInit();

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setAutoPullDownRefresh(false);

        mSinglePickController = new SinglePickController(this.getActivity());
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.setDividerVisible(true);

        oilType.add("加油");
        oilType.add("换油");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

        refreshListController.refreshComplete(lubricateOilsEntities);
    }

    @Override
    protected void initListener() {
        super.initListener();

        lubricateOilsAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                String tag = (String) childView.getTag();
                LubricateOilsEntity lubricateOilsEntity = (LubricateOilsEntity) obj;

                switch (tag) {
                    case "oilType":
                        showOilType(lubricateOilsEntity,position);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @description 加换油
     * @param
     * @param lubricateOilsEntity
     * @param position
     * @return
     * @author zhangwenshuai1 2018/8/25
     *
     */
    private void showOilType(LubricateOilsEntity lubricateOilsEntity, int position) {
        mSinglePickController.list(oilType).listener((index, item) -> {
            SystemCodeEntity systemCodeEntity = new SystemCodeEntity();
            systemCodeEntity.value = item.toString();
            systemCodeEntity.id = "001";
            lubricateOilsEntity.oilType = systemCodeEntity;
            lubricateOilsAdapter.notifyItemChanged(position);
        }).show();
    }

    public void setLubricateOilsFragmentData(List<LubricateOilsEntity> list) {
        lubricateOilsEntities = list;
    }

}
