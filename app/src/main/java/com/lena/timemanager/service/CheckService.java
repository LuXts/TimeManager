package com.lena.timemanager.service;

import android.app.Service;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.gyf.cactus.Cactus;
import com.lena.timemanager.tools.PlanTool;
import com.lena.timemanager.tools.SimpleTool;
import com.lena.timemanager.ui.LockActivity;

public class CheckService extends Service {

    private static final String TAG = "CheckService";

    private String LauncherPackName;

    private String LastPackName;
    private int hour;
    private int min;

    private boolean status;

    private MyBinder myBinder;

    private Handler mHandler = null;
    private final static int LOOPHANDLER = 0;
    private HandlerThread handlerThread = null;
    private static long cycleTime = 300;

    public class MyBinder extends Binder {
        public CheckService getService() {
            return CheckService.this;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        myBinder = new MyBinder();
        hour = -1;
        min = -1;
        LauncherPackName =
                SimpleTool.getHomeLauncherPackName(CheckService.this);
        LastPackName = "";
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Cactus.getInstance()
                .register(this);

        handlerThread = new HandlerThread("count_thread");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper()) {
            public void dispatchMessage(android.os.Message msg) {
                switch (msg.what) {
                    case LOOPHANDLER:
                        check();
                        break;
                }
                mHandler.sendEmptyMessageDelayed(LOOPHANDLER, cycleTime);
            }
        };
        mHandler.sendEmptyMessage(LOOPHANDLER);
        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "CheckService Destroy!");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    public void check() {
        if (SimpleTool.isUsageStats(CheckService.this)) {
            String temp =
                    SimpleTool.getLastPackName(getApplicationContext());
            if (!temp.equals(LastPackName)) {
                Calendar calendar = Calendar.getInstance();
                if (calendar.get(Calendar.MINUTE) != min) {
                    if (calendar.get(Calendar.HOUR_OF_DAY) != hour) {
                        hour = calendar.get(Calendar.HOUR_OF_DAY);
                    }
                    min = calendar.get(Calendar.MINUTE);
                    LauncherPackName =
                            SimpleTool.getHomeLauncherPackName(CheckService.this);
                }

                if (!SimpleTool.isSystemApp(CheckService.this, temp) && !temp.equals(LauncherPackName)) {
                    if (PlanTool.updateMap(calendar.get(Calendar.DAY_OF_WEEK)
                            , hour, min, temp)) {
                        show();
                    }
                }
                LastPackName = temp;
            }
        }
    }


    private void show() {
        if (!SimpleTool.isFastClick()) {
            Intent intent =
                    new Intent(CheckService.this,
                            LockActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CheckService.this.startActivity(intent);
        }
    }

}
