package com.lena.timemanager.data;

import android.annotation.SuppressLint;
import android.widget.Switch;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lena.timemanager.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ManagerPlanAdapter extends BaseQuickAdapter<ManagerPlan,
        BaseViewHolder> {

    public ManagerPlanAdapter(@LayoutRes int layoutResId,
                              @Nullable List<ManagerPlan> data) {
        super(layoutResId, data);
    }


    @SuppressLint("DefaultLocale")
    @Override
    protected void convert(@NotNull BaseViewHolder helper,
                           ManagerPlan item) {
        Switch aSwitch = helper.getView(R.id.Plan_List_Item_Switch);
        if (item.getStatus()) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }

        helper.setText(R.id.Plan_List_Item_Time, String.format("%02d:%02d" +
                        "-%02d:%02d",
                item.getStartHour(), item.getStartMin(), item.getEndHour(),
                item.getEndMin()));
        helper.setText(R.id.Plan_List_Item_Title, item.getName());
        String temp = "";
        if (item.getWeek_1()) {
            temp += getContext().getString(R.string.Plan_Week_1);
            temp += getContext().getString(R.string.Plan_Week_Separator);
        }
        if (item.getWeek_2()) {
            temp += getContext().getString(R.string.Plan_Week_2);
            temp += getContext().getString(R.string.Plan_Week_Separator);
        }
        if (item.getWeek_3()) {
            temp += getContext().getString(R.string.Plan_Week_3);
            temp += getContext().getString(R.string.Plan_Week_Separator);
        }
        if (item.getWeek_4()) {
            temp += getContext().getString(R.string.Plan_Week_4);
            temp += getContext().getString(R.string.Plan_Week_Separator);
        }
        if (item.getWeek_5()) {
            temp += getContext().getString(R.string.Plan_Week_5);
            temp += getContext().getString(R.string.Plan_Week_Separator);
        }
        if (item.getWeek_6()) {
            temp += getContext().getString(R.string.Plan_Week_6);
            temp += getContext().getString(R.string.Plan_Week_Separator);
        }
        if (item.getWeek_7()) {
            temp += getContext().getString(R.string.Plan_Week_7);
            temp += getContext().getString(R.string.Plan_Week_Separator);
        }
        temp = temp.substring(0,
                temp.length() - getContext().getString(R.string.Plan_Week_Separator).length());
        helper.setText(R.id.Plan_List_Item_Week, temp);
    }

}
