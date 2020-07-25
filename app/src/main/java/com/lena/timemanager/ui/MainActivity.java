package com.lena.timemanager.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.billy.android.preloader.PreLoader;
import com.billy.android.preloader.interfaces.DataListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.cactus.Cactus;
import com.leaf.library.StatusBarUtil;
import com.lena.timemanager.R;
import com.lena.timemanager.service.CheckService;
import com.lena.timemanager.tools.PlanTool;
import com.tencent.mmkv.MMKV;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private int preLoaderId;

    private long firstClick;
    private FragmentManagerList mainFragment1 = null;
    private FragmentTimeList mainFragment2 = null;
    private FragmentSetting mainFragment3 = null;
    private FragmentTransaction mFragmentTransaction;

    private static int m = 0;

    private CheckService mService;

    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        BottomNavigationView bottomNavigation =
                findViewById(R.id.Main_BottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigation.setSelectedItemId(bottomNavigation.getMenu().getItem(m).getItemId());

        preLoaderId = getIntent().getIntExtra("preLoaderId", -1);

        class Listener implements DataListener<String> {
            @Override
            public void onDataArrived(String data) {
                onRe();
            }
        }

        if (preLoaderId != -1) {
            PreLoader.listenData(preLoaderId, new Listener());
        }


        Cactus.getInstance()
                .register(this);

        Intent intent = new Intent(this, CheckService.class);
        startService(intent);

        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


        setFragment(m);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        Intent intent = new Intent(this, CheckService.class);
        stopService(intent);
        Cactus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MMKV kv = MMKV.defaultMMKV();
        if (!kv.getBoolean("isEPlan", false)) {
            PreLoader.refresh(preLoaderId);
        } else {
            kv.encode("isEPlan", false);
            onRe();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Main_Bottom_Manager_List:
                    setFragment(0);
                    return true;
                case R.id.Main_Bottom_Time_List:
                    setFragment(1);
                    return true;
                case R.id.Main_Bottom_Setting:
                    setFragment(2);
                    return true;
                default:
                    break;
            }
            return false;
        }


    };


    protected void onRe() {
        PlanTool.rePlanList();
        switch (m) {
            case 0:
                if (mainFragment1 != null) {
                    mainFragment1.onRe();
                }
            case 1:
                if (mainFragment2 != null) {
                    mainFragment2.onRe();
                }
        }
    }

    private void setFragment(int i) {
        m = i;
        FragmentManager fm = getSupportFragmentManager();
        mFragmentTransaction = fm.beginTransaction();
        hideFragment();
        switch (i) {
            case 0:
                if (mainFragment1 == null) {
                    mainFragment1 = new FragmentManagerList();
                    mFragmentTransaction.add(R.id.Main_FrameLayout,
                            mainFragment1);
                }
                mFragmentTransaction.show(mainFragment1);
                break;
            case 1:
                if (mainFragment2 == null) {
                    mainFragment2 = new FragmentTimeList();
                    mFragmentTransaction.add(R.id.Main_FrameLayout,
                            mainFragment2);
                }
                mFragmentTransaction.show(mainFragment2);
                break;
            case 2:
                if (mainFragment3 == null) {
                    mainFragment3 = new FragmentSetting();
                    mFragmentTransaction.add(R.id.Main_FrameLayout,
                            mainFragment3);
                }
                mFragmentTransaction.show(mainFragment3);
                break;
        }
        mFragmentTransaction.commit();

    }

    private void hideFragment() {
        if (mainFragment1 != null) {
            mFragmentTransaction.hide(mainFragment1);
        }
        if (mainFragment2 != null) {
            mFragmentTransaction.hide(mainFragment2);
        }
        if (mainFragment3 != null) {
            mFragmentTransaction.hide(mainFragment3);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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