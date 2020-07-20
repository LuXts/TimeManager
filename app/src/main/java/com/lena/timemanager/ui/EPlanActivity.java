package com.lena.timemanager.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.leaf.library.StatusBarUtil;
import com.lena.timemanager.R;
import com.lena.timemanager.data.SelectAppAdapter;
import com.lena.timemanager.tools.Globe;

import java.util.Date;

public class EPlanActivity extends AppCompatActivity {

    private static final String TAG = "EPlanActivity";

    private int StartHour;
    private int StartMin;
    private int EndHour;
    private int EndMin;

    private TextView tStartTime = null;
    private TextView tEndTime = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_plan);

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

        MaterialCardView StartButton =
                findViewById(R.id.E_Plan_Start_Time_Button);
        MaterialCardView EndButton = findViewById(R.id.E_Plan_End_Time_Button);

        MaterialCardView SelectButton =
                findViewById(R.id.E_Plan_Apps_List_Button);

        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(0);
            }
        });

        EndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(1);
            }
        });

        SelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectApp();
            }
        });

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
            //TODO 编辑时处理。
        }

    }

    private void Close() {
        this.finish();
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
        final SelectAppAdapter adapter =
                new SelectAppAdapter(R.layout.item_select_app,
                        Globe.getAllAppsList());


        //给RecyclerView设置适配器
        recyclerView.setAdapter(adapter);


        BottomSheetBehavior<View> behavior = BottomSheetBehavior.
                from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setHideable(false);
        behavior.setDraggable(false);

        dialog.show();
    }


    @SuppressWarnings("deprecation")
    private void SetTime() {
        Date date = new Date(0, 0, 0, StartHour, StartMin);
        tStartTime.setText(String.format("%tR", date));
        date.setHours(EndHour);
        date.setMinutes(EndMin);
        tEndTime.setText(String.format("%tR", date));
    }

}