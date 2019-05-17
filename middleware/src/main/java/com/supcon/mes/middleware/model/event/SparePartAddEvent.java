package com.supcon.mes.middleware.model.event;

import com.supcon.mes.middleware.model.bean.Good;

/**
 * SparePartAddEvent 备件添加回调Event
 * created by zhangwenshuai1 2018/10/26
 */
public class SparePartAddEvent {
    private boolean flag;
    private Good good;

    public SparePartAddEvent() {
    }

    public SparePartAddEvent(boolean flag, Good good) {
        this.flag = flag;
        this.good = good;
    }

    public boolean isFlag() {
        return flag;
    }

    public Good getGood() {
        return good;
    }
}
