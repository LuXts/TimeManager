package com.lena.timemanager.data;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lena.timemanager.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SelectAppAdapter extends ApplicationInfoAdapter {


    public SelectAppAdapter(int layoutResId,
                            @Nullable List<ApplicationInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper,
                           ApplicationInfo item) {

        helper.setImageDrawable(R.id.Select_App_Item_Icon,
                item.getIcon());
        helper.setText(R.id.Select_App_Item_Name, item.getName());
    }


}
