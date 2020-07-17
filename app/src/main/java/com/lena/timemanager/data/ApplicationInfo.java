package com.lena.timemanager.data;

import android.graphics.drawable.Drawable;

public class ApplicationInfo {
    private long UseTime;
    private String Name;
    private String PackName;
    private Drawable Icon;

    public ApplicationInfo(long useTime, String name, String packName,
                           Drawable icon) {
        UseTime = useTime;
        Name = name;
        PackName = packName;
        Icon = icon;
    }

    public long getUseTime() {
        return UseTime;
    }

    public void setUseTime(long useTime) {
        UseTime = useTime;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPackName() {
        return PackName;
    }

    public void setPackName(String packName) {
        PackName = packName;
    }

    public Drawable getIcon() {
        return Icon;
    }

    public void setIcon(Drawable icon) {
        Icon = icon;
    }
}
