package com.lena.timemanager.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leaf.library.StatusBarUtil;
import com.lena.timemanager.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private long firstClick;
    private Fragment mainFragment1 = null;
    private Fragment mainFragment2 = null;
    private Fragment mainFragment3 = null;
    private FragmentTransaction mftr;
    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtil.setTransparentForWindow(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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

        bottomNavigation =
                (BottomNavigationView) findViewById(R.id.Main_BottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        setFragment(0);

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

    private void setFragment(int i) {
        FragmentManager fm = getSupportFragmentManager();
        mftr = fm.beginTransaction();
        hideFragment();
        switch (i) {
            case 0:
                if (mainFragment1 == null) {
                    mainFragment1 = new FragmentManagerList();
                    mftr.add(R.id.Main_FrameLayout, mainFragment1);
                }
                mftr.show(mainFragment1);
                break;
            case 1:
                if (mainFragment2 == null) {
                    mainFragment2 = new FragmentTimeList();
                    mftr.add(R.id.Main_FrameLayout, mainFragment2);
                }
                mftr.show(mainFragment2);
                break;
            case 2:
                if (mainFragment3 == null) {
                    mainFragment3 = new FragmentSetting();
                    mftr.add(R.id.Main_FrameLayout, mainFragment3);
                }
                mftr.show(mainFragment3);
                break;
        }
        mftr.commit();

    }

    private void hideFragment() {
        if (mainFragment1 != null) {
            mftr.hide(mainFragment1);
        }
        if (mainFragment2 != null) {
            mftr.hide(mainFragment2);
        }
        if (mainFragment3 != null) {
            mftr.hide(mainFragment3);
        }
    }

    @Override
    public void onBackPressed() {

        long secondClick = System.currentTimeMillis();
        if (secondClick - firstClick > 1000) {
            Toast.makeText(MainActivity.this, "再次点击退出", Toast.LENGTH_SHORT).show();
            firstClick = secondClick;

        } else {
            this.finish();
        }
    }

}