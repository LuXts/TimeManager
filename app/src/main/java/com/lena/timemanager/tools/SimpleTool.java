package com.lena.timemanager.tools;

import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

public class SimpleTool {

    private static final String TAG = "SimpleTool";

    public static boolean isUsageStats(Context context) {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context
                .getSystemService(Context.USAGE_STATS_SERVICE);
        assert usageStatsManager != null;
        List<android.app.usage.UsageStats> queryUsageStats =
                usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        return !(queryUsageStats == null || queryUsageStats.isEmpty());
    }

    public static boolean isAlertWindow(Context context) {
        Boolean result = true;
        try {
            Class<Settings> clazz = Settings.class;
            Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays"
                    , Context.class);
            result = (Boolean) canDrawOverlays.invoke(null, context);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return result;
    }
}
