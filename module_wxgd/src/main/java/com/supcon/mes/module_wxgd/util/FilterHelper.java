package com.supcon.mes.module_wxgd.util;

import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ScreenEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_wxgd.model.bean.WorkState;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper {

    /**
     * @param
     * @return
     * @description 工单来源过滤条件
     * @author zhangwenshuai1 2018/8/14
     */
    public static List<FilterBean> createWorkSourceList() {
        List<FilterBean> list = new ArrayList<>();

        FilterBean filterBean;

        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdWorkSource_CN.faultInfoSource;
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdWorkSource_CN.checkRepair;
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdWorkSource_CN.bigRepair;
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdWorkSource_CN.lubrication;
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdWorkSource_CN.maintenance;
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdWorkSource_CN.sparePart;
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdWorkSource_CN.allSource;
        list.add(filterBean);

        return list;
    }

    public static List<FilterBean> createRepairTypeList() {
        List<SystemCodeEntity> list = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_WX_TYPE);
        List<FilterBean> filterBeanList = new ArrayList<>();
        FilterBean filterBean;
        for (SystemCodeEntity entity : list) {
            filterBean = new FilterBean();
            filterBean.name = entity.value;
            filterBeanList.add(filterBean);
        }
        filterBean = new FilterBean();
        filterBean.name = "类型不限";
        filterBean.type = CustomFilterView.VIEW_TYPE_ALL;
        filterBeanList.add(filterBean);

        return filterBeanList;
    }

    public static List<FilterBean> createPriorityList() {
        List<SystemCodeEntity> list = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_PRIORITY);
        List<FilterBean> filterBeanList = new ArrayList<>();
        FilterBean filterBean;
        for (SystemCodeEntity entity : list) {
            filterBean = new FilterBean();
            filterBean.name = entity.value;
            filterBeanList.add(filterBean);
        }
        filterBean = new FilterBean();
        filterBean.name = "级别不限";
        filterBean.type = CustomFilterView.VIEW_TYPE_ALL;
        filterBeanList.add(filterBean);

        return filterBeanList;
    }

    //工作流状态
    public static List<ScreenEntity> createWorkflowList() {
        List<ScreenEntity> list = new ArrayList<>();
        list.add(WorkState.ALL.getScreenEntity());
        list.add(WorkState.DISPATCH.getScreenEntity());
        list.add(WorkState.CONFIRM.getScreenEntity());
        list.add(WorkState.IMPLEMENT.getScreenEntity());
        list.add(WorkState.ACCEPTANCE.getScreenEntity());
        list.add(WorkState.COMPLETE.getScreenEntity());
        return list;
    }
}
