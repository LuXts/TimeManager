package com.lena.timemanager.tools;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;

import com.lena.timemanager.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SimpleTool {

    private static final String TAG = "SimpleTool";

    private static Boolean isUsagerS = null;

    public static boolean isUsageStats(Context context) {
        if (isUsagerS == null) {
            long ts = System.currentTimeMillis();
            UsageStatsManager usageStatsManager = (UsageStatsManager) context
                    .getSystemService(Context.USAGE_STATS_SERVICE);
            assert usageStatsManager != null;
            List<android.app.usage.UsageStats> queryUsageStats =
                    usageStatsManager.queryUsageStats(
                            UsageStatsManager.INTERVAL_BEST, 0, ts);
            isUsagerS = !(queryUsageStats == null || queryUsageStats.isEmpty());
        }
        return isUsagerS;
    }

    public static boolean isAlertWindow(Context context) {
        boolean result = true;
        try {
            Class<Settings> clazz = Settings.class;
            Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays"
                    , Context.class);
            result = (boolean) canDrawOverlays.invoke(null, context);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return result;
    }

    public static ArrayList<UsageStats> getUsageList(Context context,
                                                     long startTime,
                                                     long endTime) {
        ArrayList<UsageStats> list = new ArrayList<>();
        UsageStatsManager mUsmManager =
                (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList =
                mUsmManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                        startTime, endTime);
        for (UsageStats stats : usageStatsList) {
            if (stats.getTotalTimeInForeground() > 0) {
                list.add(stats);
            }
        }

        return list;
    }

    public static boolean isSystemApp(Context context, String pkgName) {
        if (pkgName.equals(context.getPackageName())) {
            return true;
        }
        boolean isSystemApp = false;
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(pkgName, 0);
        } catch (Throwable t) {
            //Log.w(TAG, t.getMessage(), t);
        }
        if (pi != null) {
            isSystemApp =
                    (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
        }
        return isSystemApp;
    }

    public static String getApplicationNameByPackageName(Context context,
                                                         String packageName) {
        PackageManager pm = context.getPackageManager();
        String Name;
        try {
            Name = pm.getApplicationLabel(pm.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Name = context.getString(R.string.Tool_NoFind_PackName);
        }
        return Name;
    }


    public static Drawable getIcon(Context context, String pakgename) {
        ApplicationInfo appInfo;
        Drawable appIcon;
        PackageManager pm = context.getPackageManager();
        try {
            appInfo = pm.getApplicationInfo(pakgename,
                    PackageManager.GET_META_DATA);
            appIcon = pm.getApplicationIcon(appInfo);

        } catch (PackageManager.NameNotFoundException e) {
            appIcon = context.getDrawable(R.drawable.ic_unknown_app_24);
            assert appIcon != null;
            appIcon.setTint(context.getColor(R.color.colorPrimary));
        }
        return appIcon;
    }

    public static String getHomeLauncherPackName(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo =
                context.getPackageManager().resolveActivity(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null) {
            return resolveInfo.activityInfo.packageName;
        } else {
            return "";
        }
    }

    public static String getLastPackName(Context context) {
        String packageName;
        @SuppressLint("WrongConstant") UsageStatsManager usageStatsManager =
                (UsageStatsManager) context.getApplicationContext()
                        .getSystemService("usagestats");

        long i = System.currentTimeMillis();

        List<UsageStats> queryUsageStats =
                usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, i);


        UsageStats recentStats = null;
        for (UsageStats usageStats : queryUsageStats) {
            if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                recentStats = usageStats;
            }
        }
        packageName = recentStats != null ? recentStats.getPackageName() :
                "Unknown";
        return packageName;
    }

    private static final int FAST_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME) {
            flag = false;
            lastClickTime = currentClickTime;
        }
        return flag;
    }


}
