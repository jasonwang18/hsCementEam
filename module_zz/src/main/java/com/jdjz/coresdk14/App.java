package com.jdjz.coresdk14;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bluetron.base.di.base.application.ApplicationComponent;
import com.bluetron.base.di.base.application.ApplicationModule;
import com.bluetron.base.di.base.application.DaggerApplicationComponent;
import com.bluetron.libs.widgets.utils.Utils;
import com.bluetron.rxretrohttp.RxRetroHttp;
import com.bluetron.zhizhi.BuildConstants;
import com.bluetron.zhizhi.common.api.ChangeBaseUrlInterceptor;
import com.bluetron.zhizhi.common.api.HeaderInterceptor;
import com.bluetron.zhizhi.common.api.TokenInvalidInterceptor;
import com.bluetron.zhizhi.common.util.GreenDaoUtil;
import com.hss01248.notifyutil.NotifyUtil;
import com.jude.utils.JUtils;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.vondear.rxtool.RxTool;

import cn.bluetron.zhizhi.BuildConfig;


public class App extends EamApplication {
    private ApplicationComponent mApplicationComponent;



    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
        //ApplicationInitUtil.init(this);

        ARouter.init(this);
        Utils.init(this);
        com.bluetron.libs.widgets.utils.Utils.init(this);
        NotifyUtil.init(this);
        RxRetroHttp.getInstance().setBaseUrl(BuildConfig.API_SERVER_UPLOAD)
                .generateRetroClient(BuildConstants.RETRO_CLIENT_TAG_FILE);
        RxRetroHttp.getInstance().resetInterceptor()
                .setBaseUrl(BuildConfig.API_SERVER_URL)
                .setTimeOut(BuildConfig.API_TIME_OUT)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new TokenInvalidInterceptor())
                .addInterceptor(new ChangeBaseUrlInterceptor())
                .setDebug(BuildConfig.DEBUG)
                .init(this);
        RxTool.init(this);
        GreenDaoUtil.init(this);

       /* UMConfigure.init(this,getString(R.string.umeng_appkey),"Umeng", UMConfigure.DEVICE_TYPE_PHONE, getString(R.string.umeng_pushkey));
        PushAgent mPushAgent = PushAgent.getInstance(this);
        MobclickAgent.setCatchUncaughtExceptions(true);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                UserCacheUtil.saveYoumengToken(deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                System.out.println(s);

            }
        });*/

        //mPushAgent.setPushIntentServiceClass(UmengNotificationService.class);
        //初始化JUtils
        JUtils.initialize(this);
        JUtils.setDebug(true, "tchl");
        //tbs x5
//        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//            @Override
//            public void onViewInitFinished(boolean b) {
//                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                Log.d("app", " onViewInitFinished is " + b);
//            }
//
//            @Override
//            public void onCoreInitFinished() {
//                // TODO Auto-generated method stub
//            }
//        };

        //x5内核初始化接口
//        QbSdk.initX5Environment(getApplicationContext(), cb);
        /*if (BuildConfig.IS_RELEASE) {

        }*/


        SharedPreferencesUtils.setParam(getApplicationContext(), Constant.ZZ.IP,"192.168.9.31");
        SharedPreferencesUtils.setParam(getApplicationContext(), Constant.ZZ.PORT,"8043");

    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
