package com.supcon.mes.module_sbda_online.screen;

import android.annotation.SuppressLint;

import com.supcon.mes.module_sbda_online.model.bean.ScreenEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/1/2.
 * Email:wangshizhan@supcon.com
 */

public class FilterHelper {

    @SuppressLint("CheckResult")
    public static List<ScreenEntity> createStateFilter() {
        List<ScreenEntity> list = new ArrayList<>();
        list.add(ScreenState.UNLIMIT.getScreenEntity());
        list.add(ScreenState.INUSE.getScreenEntity());
        list.add(ScreenState.PROHIBIT.getScreenEntity());
        list.add(ScreenState.SEALUP.getScreenEntity());
        list.add(ScreenState.SCRAP.getScreenEntity());
        return list;
    }

}
