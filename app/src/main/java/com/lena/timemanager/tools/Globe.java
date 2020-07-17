package com.lena.timemanager.tools;

import android.app.usage.UsageStats;
import android.content.Context;
import android.util.Log;

import com.lena.timemanager.data.ApplicationInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Globe {

    private static final String TAG = "Globe";

    private static String HomeLauncherPackName = null;
    private static ArrayList<ApplicationInfo> AppInfoList =
            null;
    private static float TotalTime;

    public static void initInfo(Context context) {
        if (AppInfoList == null) {
            if (HomeLauncherPackName == null) {
                HomeLauncherPackName =
                        SimpleTool.getHomeLauncherPackName(context);
            }
            Log.d(TAG, HomeLauncherPackName);
            AppInfoList = new ArrayList<ApplicationInfo>();
            long time = System.currentTimeMillis();
            ArrayList<UsageStats> usageStatsList =
                    SimpleTool.getUsageList(context,
                            time - 3600000,
                            time);
            for (UsageStats temp : usageStatsList) {
                long tTime = temp.getTotalTimeInForeground();
                String PackName = temp.getPackageName();
                Log.d(TAG, PackName);
                if (tTime > 1000 && !SimpleTool.isSystemApp(context,
                        PackName) && !PackName.equals(HomeLauncherPackName)) {
                    TotalTime += tTime;
                    AppInfoList.add(new ApplicationInfo(tTime,
                            SimpleTool.getApplicationNameByPackageName(context, PackName), PackName, SimpleTool.getIcon(context, PackName)));
                }
            }

            Collections.sort(AppInfoList,
                    new Comparator<ApplicationInfo>() {
                        @Override
                        public int compare(ApplicationInfo o1,
                                           ApplicationInfo o2) {

                            if (o2.getUseTime() - o1.getUseTime() > 0) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
        }
    }


    public static ArrayList<ApplicationInfo> getAppInfoList() {
        return AppInfoList;
    }

    public static float getTotalTime() {
        return TotalTime;
    }

}
