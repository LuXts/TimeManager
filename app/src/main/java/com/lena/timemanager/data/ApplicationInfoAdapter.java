package com.lena.timemanager.data;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lena.timemanager.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ApplicationInfoAdapter extends BaseQuickAdapter<ApplicationInfo,
        BaseViewHolder> {

    public ApplicationInfoAdapter(@LayoutRes int layoutResId,
                                  @Nullable List<ApplicationInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper,
                           ApplicationInfo item) {
        long hour = item.getUseTime() / 3600000;
        long min = item.getUseTime() / 60000 - hour * 60;
        long sec = item.getUseTime() / 1000 - hour * 3600 - min * 60;
        String temp_hour = "";
        String temp_min = "";
        String temp_sec = "";

        if (hour != 0) {
            temp_hour = getContext().getString(R.string.Date_Hour_Info,
                    Long.toString(hour));
        }
        if (min != 0) {
            temp_min = getContext().getString(R.string.Date_Minute_Info,
                    Long.toString(min));
        } else if (sec != 0) {
            temp_sec = getContext().getString(R.string.Date_Second_Info,
                    Long.toString(sec));
        }


        String temp = temp_hour + temp_min + temp_sec;
        helper.setImageDrawable(R.id.Time_List_Item_Icon,
                item.getIcon())
                .setText(R.id.Time_List_Item_Name, item.getName())
                .setText(R.id.Time_List_Item_Time,
                        temp);
    }


}