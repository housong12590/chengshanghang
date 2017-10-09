package com.jmm.csg;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.jmm.csg.config.AppConfig;
import com.umeng.analytics.MobclickAgent;


public class BaseApplication extends Application {

    private static BaseApplication instance;
    public static int app_status_code = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppConfig.init(this);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setCatchUncaughtExceptions(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }


    public static Context getContext() {
        return instance.getApplicationContext();
    }

}
