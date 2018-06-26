package yoki.calendarapp.CalendarMobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import yoki.calendarapp.R;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.et_event_name)
    EditText eventName;
    @BindView(R.id.et_event_desc)
    EditText description;
    @BindView(R.id.et_start_time)
    EditText startTime;
    @BindView(R.id.et_end_time)
    EditText endTime;
    @BindView(R.id.button)
    Button submit;
    private String globalTime = null;
    private DialogFragment dialogFragment;
    private int hrsOfDay, minOfDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        dialogFragment = new TimePickerFragment(globalTime, startTime, new EventActivity.GetTime() {
            @Override
            public void getTimeMethod(int final_hours, int final_minute) {
                hrsOfDay = final_hours;
                minOfDay = final_minute;
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialogFragment.show(getSupportFragmentManager(), "timePicker");
                }
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        endTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialogFragment.show(getSupportFragmentManager(), "timePicker");
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(EventActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

     interface GetTime {
        void getTimeMethod(int final_hours, int final_minute);
    }
}