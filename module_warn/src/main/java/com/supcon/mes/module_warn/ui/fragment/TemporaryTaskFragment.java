package com.supcon.mes.module_warn.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.fragment.BaseRefreshFragment;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.ui.adapter.DailyLubricationPartAdapter;

public class TemporaryTaskFragment extends BaseRefreshFragment {

    @BindByTag("recyclerView")
    RecyclerView recyclerView;
    private DailyLubricationPartAdapter dailyLubricationPartAdapter;

    public static TemporaryTaskFragment newInstance() {
        TemporaryTaskFragment fragment = new TemporaryTaskFragment();
        return fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        dailyLubricationPartAdapter = new DailyLubricationPartAdapter(context);
        dailyLubricationPartAdapter.setEditable(true);
        recyclerView.setAdapter(dailyLubricationPartAdapter);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_temporary;
    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshController.refreshComplete();
            }
        });
    }
}
