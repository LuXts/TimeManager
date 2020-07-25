package com.lena.timemanager.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.lena.timemanager.R;
import com.lena.timemanager.data.ManagerPlanAdapter;
import com.lena.timemanager.tools.PlanTool;
import com.tencent.mmkv.MMKV;

public class FragmentManagerList extends Fragment {
    private View view;
    private View head;
    private RecyclerView recyclerView;
    private ManagerPlanAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_manager_list, container,
                false);

        head = inflater.inflate(R.layout.fragment_manager_list_head, container,
                false);

        recyclerView = view.findViewById(R.id.Manager_List_Recycler_View);


        MaterialCardView AddButton =
                head.findViewById(R.id.Manager_List_Add_Plan_Button);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MMKV kv = MMKV.defaultMMKV();
                kv.encode("isEPlan", true);
                Intent intent = new Intent(view.getContext(),
                        EPlanActivity.class);
                intent.putExtra("isNew", true);
                view.getContext().startActivity(intent);
            }
        });

        initList();
        return view;
    }


    public void onRe() {
        reListData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initList() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ManagerPlanAdapter(R.layout.item_plan_list,
                PlanTool.getPlanList());
        adapter.addHeaderView(head);

        adapter.addChildClickViewIds(R.id.Plan_List_Item,
                R.id.Plan_List_Item_Switch);

        adapter.addChildLongClickViewIds(R.id.Plan_List_Item);

        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter,
                                         @NonNull View view, int position) {
                Switch mSwitch = null;
                switch (view.getId()) {
                    case R.id.Plan_List_Item: {
                        MMKV kv = MMKV.defaultMMKV();
                        kv.encode("isEPlan", true);
                        Intent intent = new Intent(view.getContext(),
                                EPlanActivity.class);
                        intent.putExtra("isNew", false);
                        intent.putExtra("position", position);
                        view.getContext().startActivity(intent);
                        break;
                    }
                    case R.id.Plan_List_Item_Switch: {
                        mSwitch = (Switch) view;
                        break;
                    }
                }
                if (mSwitch != null) {
                    PlanTool.setItemStatus(position, mSwitch.isChecked());
                }
            }
        });

        adapter.setOnItemChildLongClickListener(new OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, final int position) {
                final BottomSheetDialog dialog =
                        new BottomSheetDialog(view.getContext());
                @SuppressLint("InflateParams") View mview =
                        getLayoutInflater().inflate(R.layout.activity_delete_plan,
                                null);
                dialog.setContentView(mview);
                Button button = mview.findViewById(R.id.Delete_Plan_Button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlanTool.deletePlan(position);
                        onRe();
                        dialog.dismiss();
                    }
                });

                BottomSheetBehavior<View> behavior = BottomSheetBehavior.
                        from((View) mview.getParent());
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                dialog.show();
                return true;
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void reListData() {
        adapter.notifyDataSetChanged();
    }

}