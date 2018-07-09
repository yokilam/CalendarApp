package yoki.calendarapp.calendarMobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import yoki.calendarapp.R;
import yoki.calendarapp.calendarBackend.CalendarDatabaseHelper;
import yoki.calendarapp.calendarMobile.util.MonthlyCalendar;
import yoki.calendarapp.model.Event;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_event_name) EditText eventName;
    @BindView(R.id.et_event_desc) EditText description;
    @BindView(R.id.et_start_time) EditText startTime;
    @BindView(R.id.et_end_time) EditText endTime;
    @BindView(R.id.button) Button submit;
    private String globalTime = null;
    private DialogFragment dialogFragment;
    private int hrsOfDay, minOfDay;
    private DatabaseReference monthReference;
    private String date, month;
    public Event event;
    private MonthlyCalendar monthlyCalendar;
    private CalendarDatabaseHelper databaseHelper;
    private boolean hasInternet;
    private String TAG= "eventactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        month = intent.getStringExtra("month");
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        monthReference = rootReference.child("month");

        databaseHelper=CalendarDatabaseHelper.getInstance(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eventName.getText().toString().isEmpty() && !description.getText().toString().isEmpty() && !startTime.getText().toString().isEmpty() && !endTime.getText().toString().isEmpty()) {
                    checkInternetConnection();
                    if(hasInternet){
                        Log.d(TAG, "onClick: " + "has internet");
                        Event event = Event.from(month, date, eventName.getText().toString(), description.getText().toString(), startTime.getText().toString(), endTime.getText().toString());
                        rootReference.child("month").child(month).child("event").child(date).setValue(event);
                        databaseHelper.addEvent(event);
                    } else {
                        databaseHelper.addEvent(event);
                    }

                    Intent intent = new Intent(EventActivity.this, CalendarActivity.class);
                    startActivity(intent);
                }
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

    private void checkInternetConnection() {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        hasInternet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    interface GetTime {
        void getTimeMethod(int final_hours, int final_minute);
    }
}