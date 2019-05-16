package com.supcon.mes.module_olxj.util;

import android.webkit.JavascriptInterface;

public class AndroidtoJs {

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void signIn(String id) {

    }
}
