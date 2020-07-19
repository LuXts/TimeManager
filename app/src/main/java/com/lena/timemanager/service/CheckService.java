package com.lena.timemanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.lena.timemanager.tools.SimpleTool;

public class CheckService extends Service {

    private Handler mHandler = null;
    private HandlerThread handlerThread = null;
    private Handler handler = null;
    private final static int LOOPHANDLER = 0;
    private static String LastPackName = "";
    private static long cycleTime = 2000;

    public CheckService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handlerThread = new HandlerThread("count_thread");
        handlerThread.start();


        mHandler = new Handler(handlerThread.getLooper()) {
            public void dispatchMessage(android.os.Message msg) {
                switch (msg.what) {
                    case LOOPHANDLER:
                        final String temp =
                                SimpleTool.getLastPackName(getApplicationContext());
                        if (LastPackName.isEmpty() || !temp.equals(LastPackName)) {
                            handler = new Handler(handlerThread.getLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            temp, Toast.LENGTH_SHORT).show();
                                }
                            });
                            LastPackName = temp;
                        }
                        break;
                }
                mHandler.sendEmptyMessageDelayed(LOOPHANDLER, cycleTime);
            }
        };
        mHandler.sendEmptyMessage(LOOPHANDLER);
        //return Service.START_STICKY;

        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
