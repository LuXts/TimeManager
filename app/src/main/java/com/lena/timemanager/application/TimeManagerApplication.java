package com.lena.timemanager.application;

import android.app.Application;

import com.gyf.cactus.Cactus;
import com.lena.timemanager.R;
import com.lena.timemanager.tools.PlanTool;
import com.tencent.mmkv.MMKV;

public class TimeManagerApplication extends Application {

    private static TimeManagerApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MMKV.initialize(getApplicationContext());
        PlanTool.initTool(getApplicationContext());
        PlanTool.loadPlan();
        Cactus.getInstance().isDebug(false).setChannelId("TimeManager")
                .setChannelName(getString(R.string.app_name))
                .setTitle(getString(R.string.app_name))
                .setContent(getString(R.string.Service_Name))
                .setCrashRestartUIEnabled(true)
                .setServiceId(4000)
                .setMusicInterval(0)
                .hideNotificationAfterO(true)
                .setMusicEnabled(true)
                .setBackgroundMusicEnabled(true);

    }

    public static TimeManagerApplication getInstance() {
        return instance;
    }


}
