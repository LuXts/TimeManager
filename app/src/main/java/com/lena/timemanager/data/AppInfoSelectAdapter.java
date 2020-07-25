package com.lena.timemanager.data;

import android.widget.CheckBox;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lena.timemanager.R;
import com.lena.timemanager.ui.EPlanActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppInfoSelectAdapter extends ApplicationInfoAdapter {


    public AppInfoSelectAdapter(int layoutResId,
                                @Nullable List<ApplicationInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper,
                           ApplicationInfo item) {
        CheckBox checkBox = helper.getView(R.id.Select_CheckBox);
        if (EPlanActivity.getAllowApps().containsKey(item.getPackName())) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        helper.setImageDrawable(R.id.Select_App_Item_Icon,
                item.getIcon());
        helper.setText(R.id.Select_App_Item_Name, item.getName());
    }


}
