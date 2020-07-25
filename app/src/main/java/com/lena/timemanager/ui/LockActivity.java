package com.lena.timemanager.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;

import com.leaf.library.StatusBarUtil;
import com.lena.timemanager.R;
import com.lena.timemanager.service.CheckService;

public class LockActivity extends AppCompatActivity {


    private CheckService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        StatusBarUtil.setTransparentForWindow(this);
        if (android.os.Build.VERSION.SDK_INT <= 28) {
            StatusBarUtil.setDarkMode(this);
        } else {
            Configuration configuration = getResources().getConfiguration();
            int currentNightMode =
                    configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    StatusBarUtil.setDarkMode(this);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    StatusBarUtil.setLightMode(this);
                    break;
            }
        }

        Intent intent = new Intent(this, CheckService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mService.check();
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CheckService.MyBinder binder = (CheckService.MyBinder) service;
            mService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}