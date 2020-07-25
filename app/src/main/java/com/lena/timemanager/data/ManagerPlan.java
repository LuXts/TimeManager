package com.lena.timemanager.data;

import java.util.HashMap;

public class ManagerPlan {
    private static final String TAG = "ManagerPlan";

    public static int PLAN_WEEK_1 = 1;
    public static int PLAN_WEEK_2 = 2;
    public static int PLAN_WEEK_3 = 4;
    public static int PLAN_WEEK_4 = 8;
    public static int PLAN_WEEK_5 = 16;
    public static int PLAN_WEEK_6 = 32;
    public static int PLAN_WEEK_7 = 64;

    public static int weekToInt(boolean week_1, boolean week_2,
                                boolean week_3, boolean week_4, boolean week_5,
                                boolean week_6, boolean week_7) {
        int i = 0;
        if (week_1) {
            i += PLAN_WEEK_1;
        }
        if (week_2) {
            i += PLAN_WEEK_2;
        }
        if (week_3) {
            i += PLAN_WEEK_3;
        }
        if (week_4) {
            i += PLAN_WEEK_4;
        }
        if (week_5) {
            i += PLAN_WEEK_5;
        }
        if (week_6) {
            i += PLAN_WEEK_6;
        }
        if (week_7) {
            i += PLAN_WEEK_7;
        }
        return i;
    }


    private int _id;

    private String name;

    private boolean Status;

    private int StartHour;
    private int StartMin;
    private int EndHour;
    private int EndMin;

    private boolean Week_1;
    private boolean Week_2;
    private boolean Week_3;
    private boolean Week_4;
    private boolean Week_5;
    private boolean Week_6;
    private boolean Week_7;

    private HashMap<String, Boolean> AllowApps = new HashMap<>();


    public ManagerPlan(String name, int startHour, int startMin, int endHour,
                       int endMin, int week, boolean status) {
        this.name = name;
        StartHour = startHour;
        StartMin = startMin;
        EndHour = endHour;
        EndMin = endMin;
        Status = status;
        setWeek(week);
    }

    public ManagerPlan(String name, int startHour, int startMin, int endHour,
                       int endMin, int week) {
        this.name = name;
        StartHour = startHour;
        StartMin = startMin;
        EndHour = endHour;
        EndMin = endMin;
        Status = true;
        setWeek(week);
    }

    public void setWeek(int week) {
        Week_1 = (week & PLAN_WEEK_1) != 0;
        Week_2 = (week & PLAN_WEEK_2) != 0;
        Week_3 = (week & PLAN_WEEK_3) != 0;
        Week_4 = (week & PLAN_WEEK_4) != 0;
        Week_5 = (week & PLAN_WEEK_5) != 0;
        Week_6 = (week & PLAN_WEEK_6) != 0;
        Week_7 = (week & PLAN_WEEK_7) != 0;
    }


    public HashMap<String, Boolean> getAllowApps() {
        return AllowApps;
    }

    public void setAllowApps(HashMap<String, Boolean> allowApps) {
        AllowApps = allowApps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartHour() {
        return StartHour;
    }

    public void setStartHour(int startHour) {
        StartHour = startHour;
    }

    public int getStartMin() {
        return StartMin;
    }

    public void setStartMin(int startMin) {
        StartMin = startMin;
    }

    public int getEndHour() {
        return EndHour;
    }

    public void setEndHour(int endHour) {
        EndHour = endHour;
    }

    public int getEndMin() {
        return EndMin;
    }

    public void setEndMin(int endMin) {
        EndMin = endMin;
    }

    public boolean getWeek_1() {
        return Week_1;
    }

    public void setWeek_1(boolean week_1) {
        Week_1 = week_1;
    }

    public boolean getWeek_2() {
        return Week_2;
    }

    public void setWeek_2(boolean week_2) {
        Week_2 = week_2;
    }

    public boolean getWeek_3() {
        return Week_3;
    }

    public void setWeek_3(boolean week_3) {
        Week_3 = week_3;
    }

    public boolean getWeek_4() {
        return Week_4;
    }

    public void setWeek_4(boolean week_4) {
        Week_4 = week_4;
    }

    public boolean getWeek_5() {
        return Week_5;
    }

    public void setWeek_5(boolean week_5) {
        Week_5 = week_5;
    }

    public boolean getWeek_6() {
        return Week_6;
    }

    public void setWeek_6(boolean week_6) {
        Week_6 = week_6;
    }

    public boolean getWeek_7() {
        return Week_7;
    }

    public void setWeek_7(boolean week_7) {
        Week_7 = week_7;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
