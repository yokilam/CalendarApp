package yoki.calendarapp.calendarMobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import yoki.calendarapp.R;
import yoki.calendarapp.model.Event;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

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
    private DatabaseReference ref;
    private FirebaseDatabase database;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        DatabaseReference rootReference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference monthReference=rootReference.child("month");
        final DatabaseReference dateReference= monthReference.child("date");

        Intent intent= getIntent();
        date=intent.getStringExtra("date");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Event event= Event.from(date, eventName.getText().toString(), description.getText().toString(), startTime.getText().toString(), endTime.getText().toString());
                    dateReference.setValue(date);
                    dateReference.child("event").setValue(event);

//                Intent intent = new Intent(EventActivity.this, CalendarActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void createDialogFragment(EditText edittext) {
        dialogFragment = new TimePickerFragment(globalTime, edittext, new GetTime() {
            @Override
            public void getTimeMethod(int final_hours, int final_minute) {
                hrsOfDay = final_hours;
                minOfDay = final_minute;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_start_time:
                createDialogFragment(startTime);
                dialogFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.et_end_time:
                createDialogFragment(endTime);
                dialogFragment.show(getSupportFragmentManager(), "timePicker");
                break;
        }
    }

    interface GetTime {
        void getTimeMethod(int final_hours, int final_minute);
    }
}