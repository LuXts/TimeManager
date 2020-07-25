package com.lena.timemanager.tools;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lena.timemanager.data.ManagerPlan;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class PlanTool {

    private static final String TAG = "PlanTool";

    private static ArrayList<ManagerPlan> PlanList = new ArrayList<>();

    private static SQLiteDatabase SQLiteDB;


    public static void initTool(Context context) {
        SQLiteDB =
                SQLiteDatabase.openOrCreateDatabase(context.getFilesDir() +
                        "/Plan.db", null);
        String CreateTable = "create table if not exists PlanList(_id integer" +
                " primary key autoincrement,Name varchar(100),Status integer," +
                "StartHour integer,StartMin integer,EndHour integer,EndMin " +
                "integer,Week integer,AllowList text)";
        SQLiteDB.execSQL(CreateTable);
    }

    public static void savePlan() {

    }

    public static void loadPlan() {
        PlanList.clear();
        Cursor cursor = SQLiteDB.query("PlanList", null, null, null, null,
                null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            boolean status =
                    cursor.getInt(cursor.getColumnIndex("Status")) > 0;
            int StartHour = cursor.getInt(cursor.getColumnIndex(
                    "StartHour"));
            int StartMin = cursor.getInt(cursor.getColumnIndex("StartMin"));
            int EndHour = cursor.getInt(cursor.getColumnIndex("EndHour"));
            int EndMin = cursor.getInt(cursor.getColumnIndex("EndMin"));
            int Week = cursor.getInt(cursor.getColumnIndex("Week"));

            String sTemp = cursor.getString(cursor.getColumnIndex(
                    "AllowList"));
            HashMap<String, Boolean> hashMap = new HashMap<>();
            try {
                JSONArray jsonArray = new JSONArray(sTemp);
                for (int j = 0; j < jsonArray.length(); ++j) {
                    hashMap.put(jsonArray.getString(j), true);
                }
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
            addPlan(id, name, StartHour, StartMin, EndHour, EndMin, Week,
                    status, hashMap);
        }
        cursor.close();
        rePlanList();
    }

    public static void rePlanList() {
        Collections.sort(PlanList,
                new Comparator<ManagerPlan>() {
                    @Override
                    public int compare(ManagerPlan o1,
                                       ManagerPlan o2) {
                        int temp = o1.getStartHour() - o2.getStartHour();
                        if (temp > 0) {
                            return 1;
                        } else if (temp == 0) {
                            int temp2 = o1.getStartMin() - o2.getStartMin();
                            return Integer.compare(temp2, 0);
                        } else {
                            return -1;
                        }
                    }
                });
    }

    private static void addPlan(int id, String name, int startHour,
                                int startMin,
                                int endHour,
                                int endMin, int week, boolean status,
                                HashMap<String, Boolean> hashMap) {
        ManagerPlan managerPlan = new ManagerPlan(name, startHour,
                startMin,
                endHour, endMin, week, status);
        managerPlan.getAllowApps().putAll(hashMap);
        managerPlan.set_id(id);
        PlanList.add(managerPlan);
    }


    public static void addPlan(String name, int startHour, int startMin,
                               int endHour,
                               int endMin, int week, boolean status,
                               HashMap<String, Boolean> hashMap) {
        ManagerPlan managerPlan = new ManagerPlan(name, startHour,
                startMin,
                endHour, endMin, week, status);
        managerPlan.getAllowApps().putAll(hashMap);

        Set<String> set = hashMap.keySet();
        JSONArray jsonArray = new JSONArray();
        for (String str : set) {
            jsonArray.put(str);
        }

        String sTemp = jsonArray.toString();
        ContentValues cValue = new ContentValues();
        cValue.put("Name", name);
        if (status) {
            cValue.put("Status", 1);
        } else {
            cValue.put("Status", 0);
        }
        cValue.put("StartHour", startHour);
        cValue.put("StartMin", startMin);
        cValue.put("EndHour", endHour);
        cValue.put("EndMin", endMin);
        cValue.put("Week", week);
        cValue.put("AllowList", sTemp);
        SQLiteDB.insert("PlanList", null, cValue);

        Cursor cursor = SQLiteDB.query("PlanList", null, null, null, null,
                null, "_id");
        if (cursor.moveToLast()) {
            managerPlan.set_id(cursor.getInt(0));
        }
        cursor.close();
        PlanList.add(managerPlan);
        rePlanList();
    }

    public static void editPlan(int index, String name, int startHour,
                                int startMin,
                                int endHour,
                                int endMin, int week, boolean status,
                                HashMap<String, Boolean> hashMap) {
        ManagerPlan managerPlan =
                PlanList.get(index);
        managerPlan.setStatus(status);
        managerPlan.setName(name);
        managerPlan.setWeek(week);
        managerPlan.setStartHour(startHour);
        managerPlan.setStartMin(startMin);
        managerPlan.setEndHour(endHour);
        managerPlan.setEndMin(endMin);
        managerPlan.getAllowApps().clear();
        managerPlan.getAllowApps().putAll(hashMap);

        Set<String> set = hashMap.keySet();
        JSONArray jsonArray = new JSONArray();
        for (String str : set) {
            jsonArray.put(str);
        }
        String sTemp = jsonArray.toString();
        ContentValues cValue = new ContentValues();
        cValue.put("Name", name);
        if (status) {
            cValue.put("Status", 1);
        } else {
            cValue.put("Status", 0);
        }
        cValue.put("StartHour", startHour);
        cValue.put("StartMin", startMin);
        cValue.put("EndHour", endHour);
        cValue.put("EndMin", endMin);
        cValue.put("Week", week);
        cValue.put("AllowList", sTemp);

        @SuppressLint("DefaultLocale") String[] argList = {String.format("%d"
                , managerPlan.get_id())};

        SQLiteDB.update("PlanList", cValue, "_id = ?", argList);
        rePlanList();
    }

    public static void setItemStatus(int index, boolean status) {
        ManagerPlan managerPlan =
                PlanList.get(index);
        ContentValues cValue = new ContentValues();
        if (status) {
            cValue.put("Status", 1);
        } else {
            cValue.put("Status", 0);
        }
        @SuppressLint("DefaultLocale") String[] argList = {String.format("%d"
                , managerPlan.get_id())};

        SQLiteDB.update("PlanList", cValue, "_id = ?", argList);
        managerPlan.setStatus(status);
    }

    public static void deletePlan(int index) {
        ManagerPlan managerPlan =
                PlanList.get(index);
        @SuppressLint("DefaultLocale") String[] argList = {String.format("%d"
                , managerPlan.get_id())};
        SQLiteDB.delete("PlanList", "_id = ?", argList);
        PlanList.remove(index);
        rePlanList();
    }

    public static ArrayList<ManagerPlan> getPlanList() {
        return PlanList;
    }

    public static boolean updateMap(int week, int hour, int min,
                                    String temp) {

        @SuppressLint("DefaultLocale")
        String[] argList = {String.format("%d", hour), String.format("%d",
                min), String.format("%d", hour), String.format("%d", min)};

        final int[] weekTable = {ManagerPlan.PLAN_WEEK_1,
                ManagerPlan.PLAN_WEEK_2,
                ManagerPlan.PLAN_WEEK_3, ManagerPlan.PLAN_WEEK_4,
                ManagerPlan.PLAN_WEEK_5, ManagerPlan.PLAN_WEEK_6,
                ManagerPlan.PLAN_WEEK_7};

        week = (week + 5) % 7;

        int newTime = hour * 60 + min;

        int i = 0;
        for (ManagerPlan plan : PlanList) {
            if (plan.getStatus()) {
                i++;
                if ((ManagerPlan.weekToInt(plan.getWeek_1(), plan.getWeek_2(),
                        plan.getWeek_3(), plan.getWeek_4(), plan.getWeek_5(),
                        plan.getWeek_6(), plan.getWeek_7()) & week) != 0) {
                    if ((plan.getStartHour() * 60 + plan.getStartMin()) <= newTime
                            && (plan.getEndHour() * 60 + plan.getEndMin()) >= newTime) {
                        if (plan.getAllowApps().containsKey(temp)) {
                            return false;
                        }

                    }
                }
            }
        }
        return i != 0;
    }
}
