package com.lena.timemanager.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.lena.timemanager.R;
import com.lena.timemanager.data.ApplicationInfo;
import com.lena.timemanager.data.ApplicationInfoAdapter;
import com.lena.timemanager.tools.Globe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentTimeList extends Fragment {

    private float OtherTotalTime;


    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_time_list, container,
                false);


        InitPieChart();
        InitList();
        return view;
    }

    public static class MPercentFormatter extends ValueFormatter {

        protected DecimalFormat mFormat;

        public MPercentFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            if (value < 5) {
                return "";
            }
            return mFormat.format(value) + " %";
        }

    }

    @SuppressWarnings("deprecation")
    private void InitPieChart() {
        PieChart pieChart = view.findViewById(R.id.DayPieChart);
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#db5a6b"));
        colors.add(Color.parseColor("#ffa631"));
        colors.add(Color.parseColor("#0aa344"));
        colors.add(Color.parseColor("#815463"));
        colors.add(Color.parseColor("#395260"));
        for (ApplicationInfo applicationInfo : Globe.getAppInfoList()) {
            if (applicationInfo.getUseTime() / Globe.getTotalTime() >= 0.1) {
                entries.add(new PieEntry(applicationInfo.getUseTime() / Globe.getTotalTime()
                        , applicationInfo.getName()));
            } else {
                OtherTotalTime += applicationInfo.getUseTime();
            }
        }
        entries.add(new PieEntry(OtherTotalTime / Globe.getTotalTime(),
                getResources().getString(R.string.Time_List_Other)));
        if (entries.size() % 5 == 1) {
            colors.add(Color.parseColor("#aa63fa"));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(getResources().getColor(R.color.textTitleColor));

        PieData data = new PieData(dataSet);
        data.setValueTextColor(getResources().getColor(R.color.textTitleColor));
        data.setValueFormatter(new MPercentFormatter());

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(14);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.setHoleColor(getResources().getColor(R.color.background));
        pieChart.setDrawCenterText(true);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        pieChart.invalidate();
    }

    private void InitList() {
        RecyclerView recyclerView =
                view.findViewById(R.id.Time_List_Recycler_View);


        //创建布局管理
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //创建适配器
        ApplicationInfoAdapter adapter =
                new ApplicationInfoAdapter(R.layout.item_app_time,
                        Globe.getAppInfoList());

        //给RecyclerView设置适配器
        recyclerView.setAdapter(adapter);
    }

}