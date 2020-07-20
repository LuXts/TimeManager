package com.lena.timemanager.tools;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.pm.PackageInfo;

import com.lena.timemanager.data.ApplicationInfo;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Globe {

    private static final String TAG = "Globe";

    private static String HomeLauncherPackName = null;
    private static ArrayList<ApplicationInfo> AppInfoList =
            null;
    private static ArrayList<ApplicationInfo> AllAppsList =
            new ArrayList<>();
    private static float TotalTime;

    public static void loadApps(Context context) {
        List<PackageInfo> apps =
                context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo info : apps) {
            String packageName = info.packageName;
            if (!SimpleTool.isSystemApp(context, packageName)) {
                AllAppsList.add(new ApplicationInfo(0,
                        SimpleTool.getApplicationNameByPackageName(context,
                                packageName), packageName,
                        SimpleTool.getIcon(context, packageName)));
            }
        }
        final Comparator<Object> cmp =
                Collator.getInstance(java.util.Locale.CHINA);
        Collections.sort(AllAppsList,
                new Comparator<ApplicationInfo>() {
                    @Override
                    public int compare(ApplicationInfo o1,
                                       ApplicationInfo o2) {
                        return cmp.compare(o1.getName(), o2.getName());
                    }
                });
    }


    public static void initInfo(final Context context) {
        if (AppInfoList == null) {
            if (HomeLauncherPackName == null) {
                HomeLauncherPackName =
                        SimpleTool.getHomeLauncherPackName(context);
            }
            AppInfoList = new ArrayList<>();
            long time = System.currentTimeMillis();
            ArrayList<UsageStats> usageStatsList =
                    SimpleTool.getUsageList(context,
                            time - 3600000,
                            time);
            for (UsageStats temp : usageStatsList) {
                long tTime = temp.getTotalTimeInForeground();
                String PackName = temp.getPackageName();
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
                            long temp = o2.getUseTime() - o1.getUseTime();
                            if (temp > 0) {
                                return 1;
                            } else if (temp == 0) {
                                return 0;
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

    public static ArrayList<ApplicationInfo> getAllAppsList() {
        return AllAppsList;
    }

    public static float getTotalTime() {
        return TotalTime;
    }

}
