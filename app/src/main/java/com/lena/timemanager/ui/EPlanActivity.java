package com.lena.timemanager.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.leaf.library.StatusBarUtil;
import com.lena.timemanager.R;
import com.lena.timemanager.data.AppInfoSelectAdapter;
import com.lena.timemanager.data.ManagerPlan;
import com.lena.timemanager.tools.AppInfoList;
import com.lena.timemanager.tools.PlanTool;
import com.lena.timemanager.tools.SimpleTool;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;

public class EPlanActivity extends AppCompatActivity {

    private static final String TAG = "EPlanActivity";

    private String name;

    private int StartHour;
    private int StartMin;
    private int EndHour;
    private int EndMin;

    private boolean[] Week = {false, false, false, false, false, false, false};


    private static HashMap<String, Boolean> AllowApps = new HashMap<>();
    private static HashMap<String, Boolean> TempAllowApps = new HashMap<>();

    public static HashMap<String, Boolean> getAllowApps() {
        return AllowApps;
    }

    private int position = -1;

    private TextView tStartTime = null;
    private TextView tEndTime = null;
    private EditText eName = null;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_plan);


        MMKV.initialize(this);
        StatusBarUtil.setTransparentForWindow(this);
        if (android.os.Build.VERSION.SDK_INT <= 28) {
            StatusBarUtil.setDarkMode(this);
        } else {
            Configuration configuration = getResources().getConfiguration();
            int currentNightMode =
                    configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    StatusBarUtil.setDarkMode(this);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    StatusBarUtil.setLightMode(this);
                    break;
            }
        }

        tStartTime = findViewById(R.id.E_Plan_Start_Time_Text);
        tEndTime = findViewById(R.id.E_Plan_End_Time_Text);
        eName = findViewById(R.id.E_Plan_Name_Edit);

        MaterialCardView StartButton =
                findViewById(R.id.E_Plan_Start_Time_Button);
        MaterialCardView EndButton = findViewById(R.id.E_Plan_End_Time_Button);
        MaterialCardView SelectButton =
                findViewById(R.id.E_Plan_Apps_List_Button);
        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SimpleTool.isFastClick()) {
                    showTimePicker(0);
                }
            }
        });
        EndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SimpleTool.isFastClick()) {
                    showTimePicker(1);
                }
            }
        });
        SelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SimpleTool.isFastClick()) {
                    showSelectApp();
                }
            }
        });


        int[] WeekButtonList = {R.id.E_Plan_Week_1, R.id.E_Plan_Week_2,
                R.id.E_Plan_Week_3, R.id.E_Plan_Week_4, R.id.E_Plan_Week_5,
                R.id.E_Plan_Week_6, R.id.E_Plan_Week_7};
        for (int i = 0; i < 7; i++) {
            MaterialCardView Week_Button = findViewById(WeekButtonList[i]);
            Week_Button.setOnClickListener(onWeekClickListener);
        }


        Button button = findViewById(R.id.E_Plan_Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Close();
            }
        });

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isNew", true)) {
            StartHour = 0;
            StartMin = 0;
            EndHour = 0;
            EndMin = 1;
            SetTime();
        } else {
            position = intent.getIntExtra("position", -1);
            initData();
            SetTime();
        }

    }

    @Override
    protected void onDestroy() {
        AllowApps.clear();
        TempAllowApps.clear();
        super.onDestroy();
    }

    private void Close() {
        switch (isSafe()) {
            case 1:
                if (position == -1) {
                    PlanTool.addPlan(name, StartHour, StartMin, EndHour,
                            EndMin, ManagerPlan.weekToInt(Week[0],
                                    Week[1], Week[2],
                                    Week[3]
                                    , Week[4], Week[5], Week[6]), true,
                            AllowApps);
                } else {
                    PlanTool.editPlan(position, name, StartHour, StartMin,
                            EndHour, EndMin, ManagerPlan.weekToInt(Week[0],
                                    Week[1], Week[2],
                                    Week[3]
                                    , Week[4], Week[5], Week[6]), true,
                            AllowApps);
                }

                this.finish();
                break;
            case 0:
                break;
            case -1:
                Toast.makeText(this, getString(R.string.E_Plan_Warn_1),
                        Toast.LENGTH_SHORT).show();
                break;
            case -2:
                Toast.makeText(this, getString(R.string.E_Plan_Warn_2),
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void initData() {
        ManagerPlan managerPlan = PlanTool.getPlanList().get(position);
        eName.setText(managerPlan.getName());
        StartHour = managerPlan.getStartHour();
        StartMin = managerPlan.getStartMin();
        EndHour = managerPlan.getEndHour();
        EndMin = managerPlan.getEndMin();
        AllowApps.putAll(managerPlan.getAllowApps());
        if (managerPlan.getWeek_1()) {
            selectWeek(0);
        }
        if (managerPlan.getWeek_2()) {
            selectWeek(1);
        }
        if (managerPlan.getWeek_3()) {
            selectWeek(2);
        }
        if (managerPlan.getWeek_4()) {
            selectWeek(3);
        }
        if (managerPlan.getWeek_5()) {
            selectWeek(4);
        }
        if (managerPlan.getWeek_6()) {
            selectWeek(5);
        }
        if (managerPlan.getWeek_7()) {
            selectWeek(6);
        }

    }

    private int isSafe() {
        int b = 0;
        name = eName.getText().toString();
        if (Week[0] || Week[1] || Week[2] || Week[3] || Week[4] || Week[5] || Week[6]) {
            if (!name.isEmpty() && !name.trim().isEmpty()) {
                b = 1;
            } else {
                b = -2;
            }
        } else {
            b = -1;
        }
        return b;
    }


    private void showTimePicker(int mark) {

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view =
                getLayoutInflater().inflate(R.layout.activity_time_picker
                        , null);
        dialog.setContentView(view);

        final TimePicker timePicker =
                view.findViewById(R.id.Time_Picker_TimePicker);
        Button button = view.findViewById(R.id.Time_Picker_Button);

        timePicker.setIs24HourView(true);
        timePicker.setBackgroundResource(R.color.background);

        if (mark == 0) {
            timePicker.setHour(StartHour);
            timePicker.setMinute(StartMin);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StartHour = timePicker.getHour();
                    StartMin = timePicker.getMinute();
                    if (StartHour >= EndHour) {
                        EndHour = StartHour;
                        if (StartMin >= EndMin) {
                            EndMin = StartMin + 1;
                            if (EndMin == 60) {
                                if (EndHour == 23) {
                                    EndMin = 59;
                                    StartMin = 58;
                                } else {
                                    EndMin = 0;
                                    EndHour += 1;
                                }
                            }
                        }
                    }
                    dialog.dismiss();
                    SetTime();
                }
            });
        } else {
            timePicker.setHour(EndHour);
            timePicker.setMinute(EndMin);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EndHour = timePicker.getHour();
                    EndMin = timePicker.getMinute();
                    if (StartHour >= EndHour) {
                        StartHour = EndHour;
                        if (StartMin >= EndMin) {
                            StartMin = EndMin - 1;
                            if (StartMin == -1) {
                                if (StartHour == 0) {
                                    StartMin = 0;
                                    EndMin = 1;
                                } else {
                                    StartMin = 59;
                                    StartHour -= 1;
                                }
                            }
                        }
                    }
                    dialog.dismiss();
                    SetTime();
                }
            });
        }

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.
                from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setHideable(false);
        behavior.setDraggable(false);

        dialog.show();

    }

    private void showSelectApp() {

        TempAllowApps.clear();
        TempAllowApps.putAll(AllowApps);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view =
                getLayoutInflater().inflate(R.layout.activity_select_app
                        , null);
        dialog.setContentView(view);


        RecyclerView recyclerView =
                view.findViewById(R.id.Select_App_List);
        //创建布局管理
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        //创建适配器
        AppInfoSelectAdapter adapter =
                new AppInfoSelectAdapter(R.layout.item_select_app,
                        AppInfoList.getAllAppsList());

        adapter.addChildClickViewIds(R.id.Select_App_Item,
                R.id.Select_CheckBox);

        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom);
        adapter.setAnimationEnable(true);


        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter,
                                         @NonNull View view, int position) {

                CheckBox checkBox = null;
                switch (view.getId()) {
                    case R.id.Select_App_Item: {
                        checkBox = view.findViewById(R.id.Select_CheckBox);
                        checkBox.setChecked(!checkBox.isChecked());
                        break;
                    }
                    case R.id.Select_CheckBox: {
                        checkBox = (CheckBox) view;
                        break;
                    }
                }
                if (checkBox != null) {
                    if (checkBox.isChecked()) {
                        AllowApps.put(AppInfoList.getAllAppsList().get(position).getPackName(), true);
                    } else {
                        AllowApps.remove(AppInfoList.getAllAppsList().get(position).getPackName());

                    }
                }

            }
        });

        //给RecyclerView设置适配器
        recyclerView.setAdapter(adapter);

        Button button = view.findViewById(R.id.Selete_App_Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        BottomSheetBehavior<View> behavior = BottomSheetBehavior.
                from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setHideable(false);
        behavior.setDraggable(false);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                AllowApps.clear();
                AllowApps.putAll(TempAllowApps);
            }
        });

        dialog.show();
    }

    @SuppressLint("DefaultLocale")
    @SuppressWarnings("deprecation")
    private void SetTime() {
        tStartTime.setText(String.format("%02d:%02d", StartHour, StartMin));
        tEndTime.setText(String.format("%02d:%02d", EndHour, EndMin));
    }

    private View.OnClickListener onWeekClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.E_Plan_Week_1: {
                            selectWeek(0);
                            break;
                        }
                        case R.id.E_Plan_Week_2: {
                            selectWeek(1);
                            break;
                        }
                        case R.id.E_Plan_Week_3: {
                            selectWeek(2);
                            break;
                        }
                        case R.id.E_Plan_Week_4: {
                            selectWeek(3);
                            break;
                        }
                        case R.id.E_Plan_Week_5: {
                            selectWeek(4);
                            break;
                        }
                        case R.id.E_Plan_Week_6: {
                            selectWeek(5);
                            break;
                        }
                        case R.id.E_Plan_Week_7: {
                            selectWeek(6);
                            break;
                        }
                    }
                }
            };


    private void selectWeek(int i) {
        final int[] idList = {R.id.E_Plan_Week_TextView_1,
                R.id.E_Plan_Week_TextView_2, R.id.E_Plan_Week_TextView_3,
                R.id.E_Plan_Week_TextView_4, R.id.E_Plan_Week_TextView_5,
                R.id.E_Plan_Week_TextView_6, R.id.E_Plan_Week_TextView_7};
        final int[] cardList = {R.id.E_Plan_Week_1,
                R.id.E_Plan_Week_2, R.id.E_Plan_Week_3,
                R.id.E_Plan_Week_4, R.id.E_Plan_Week_5,
                R.id.E_Plan_Week_6, R.id.E_Plan_Week_7};
        TextView textView =
                findViewById(idList[i]);
        MaterialCardView mCardView = findViewById(cardList[i]);
        if (Week[i]) {
            mCardView.setCardBackgroundColor(getColor(R.color.background));
            textView.setTextColor(getColor(R.color.colorPrimary));
        } else {
            mCardView.setCardBackgroundColor(getColor(R.color.colorPrimary));
            textView.setTextColor(getColor(R.color.background));
        }
        Week[i] = !Week[i];
    }

}