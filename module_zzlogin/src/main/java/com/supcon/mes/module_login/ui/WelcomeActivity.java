package com.supcon.mes.module_login.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.service.HeartBeatService;

/**
 * Created by wangshizhan on 2017/8/11.
 */

public class WelcomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        //无title
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //全屏
//        this.getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        new Handler().postDelayed(() -> {
            if(MBapApp.isIsLogin()){
                IntentRouter.go(WelcomeActivity.this, Constant.Router.MAIN);
                //在线模式使用心跳防止session过期
                if (!SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.OFFLINE_ENABLE, false)) {
                    HeartBeatService.startLoginLoop(this);
                }
            }
            else{
                Bundle bundle = new Bundle();
//                bundle.putInt(Constant.IntentKey.LOGIN_LOGO_ID, R.drawable.ic_login_logo);
                bundle.putBoolean(Constant.IntentKey.FIRST_LOGIN, true);
                IntentRouter.go(WelcomeActivity.this, Constant.Router.LOGIN, bundle);
            }

            finish();
        }, 100);
    }


}
