package com.supcon.mes.module_warn.filter;

import android.annotation.SuppressLint;


import com.supcon.mes.mbap.beans.FilterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/1/2.
 * Email:wangshizhan@supcon.com
 */

public class FilterHelper {

    @SuppressLint("CheckResult")
    public static List<TimeStyleBean> createLubriFilter() {
        List<TimeStyleBean> list = new ArrayList<>();
        TimeStyleBean filterBean = new TimeStyleBean();
        filterBean.name = "时间频率";
        filterBean.url = "/BEAM/baseInfo/jWXItem/data-dg1530747504994.action";
        list.add(filterBean);
        TimeStyleBean filterBean1 = new TimeStyleBean();
        filterBean1.name = "运行时长";
        filterBean1.url = "/BEAM/baseInfo/jWXItem/data-dg1530749613834.action";
        list.add(filterBean1);
        return list;
    }

    @SuppressLint("CheckResult")
    public static List<TimeStyleBean> createMainteFilter() {
        List<TimeStyleBean> list = new ArrayList<>();
        TimeStyleBean filterBean = new TimeStyleBean();
        filterBean.name = "时间频率";
        filterBean.url = "/BEAM/baseInfo/jWXItem/data-dg1531171100751.action";
        list.add(filterBean);
        TimeStyleBean filterBean1 = new TimeStyleBean();
        filterBean1.name = "运行时长";
        filterBean1.url = "/BEAM/baseInfo/jWXItem/data-dg1531171100814.action";
        list.add(filterBean1);
        return list;
    }

    @SuppressLint("CheckResult")
    public static List<TimeStyleBean> createSpareFilter() {
        List<TimeStyleBean> list = new ArrayList<>();
        TimeStyleBean filterBean = new TimeStyleBean();
        filterBean.name = "时间频率";
        filterBean.url = "/BEAM/baseInfo/sparePart/data-dg1535424823416.action";
        list.add(filterBean);
        TimeStyleBean filterBean1 = new TimeStyleBean();
        filterBean1.name = "运行时长";
        filterBean1.url = "/BEAM/baseInfo/sparePart/data-dg1543250233613.action";
        list.add(filterBean1);
        return list;
    }
}
