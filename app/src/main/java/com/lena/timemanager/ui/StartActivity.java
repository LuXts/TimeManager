package com.lena.timemanager.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.billy.android.preloader.PreLoader;
import com.billy.android.preloader.interfaces.DataLoader;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leaf.library.StatusBarUtil;
import com.lena.timemanager.R;
import com.lena.timemanager.tools.AppInfoList;
import com.lena.timemanager.tools.SimpleTool;
import com.tencent.mmkv.MMKV;

import me.jessyan.autosize.AutoSizeConfig;


public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    private BottomSheetDialog dialog = null;

    private int preLoaderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AutoSizeConfig.getInstance().setBaseOnWidth(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        StatusBarUtil.setTransparentForWindow(this);


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(Color.TRANSPARENT);

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


        MMKV kv = MMKV.defaultMMKV();
        if (kv.getBoolean("New", true)) {
            kv.encode("New", false);
            Log.d(TAG, "true");
        } else {
            kv.encode("New", true);
            Log.d(TAG, "false");
        }


        class Loader implements DataLoader<String> {
            @Override
            public String loadData() {
                AppInfoList.initInfo(getApplicationContext());
                AppInfoList.loadApps(getApplicationContext());
                return "data from network server";
            }
        }

        preLoaderId = PreLoader.preLoad(new Loader());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermission();
            }
        }, 1000);
    }

    @Override
    protected void onRestart() {
        checkPermission();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if (dialog != null) {
            dialog.dismiss();
        }
        super.onPause();
    }

    private void checkPermission() {
        if (SimpleTool.isUsageStats(this)) {
            new Thread() {
                @Override
                public void run() {
                    AppInfoList.initInfo(getApplicationContext());
                }
            }.start();
            // 有权限
            if (SimpleTool.isAlertWindow(this)) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("preLoaderId", preLoaderId);
                startActivity(intent);
                this.finish();
            } else {
                showGuide(1);
            }
        } else {
            // 没有权限
            showGuide(2);
        }
    }

    private void showGuide(int mark) {
        dialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view =
                getLayoutInflater().inflate(R.layout.activity_guide, null);
        dialog.setContentView(view);
        TextView textView = view.findViewById(R.id.Guide_Text);
        Button button = view.findViewById(R.id.Guide_Button);
        if (mark == 1) {
            textView.setText(R.string.Guide_Text_1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =
                            new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
        } else if (mark == 2) {
            textView.setText(R.string.Guide_Text_2);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =
                            new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
        }
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.
                from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                checkPermission();
            }
        });
        dialog.show();
    }


}