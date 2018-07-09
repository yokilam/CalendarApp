package yoki.calendarapp.calendarMobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yoki.calendarapp.R;
import yoki.calendarapp.calendarBackend.CalendarDatabaseHelper;
import yoki.calendarapp.calendarMobile.util.MonthlyCalendar;
import yoki.calendarapp.model.Event;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "firebase";
    @BindView(R.id.monthly_calendar) MonthlyCalendar monthlyCalendar;
    @BindView(R.id.event_rv) RecyclerView eventRecyclerView;
    private Calendar calendar;
    private int currentMonth, currentDate, currentYear;
    private String date;
    private EventAdapter eventAdapter;
    private List <Event> eventList;
    private DatabaseReference ref;
    private String monthName;
    private boolean hasInternet;
    private CalendarDatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        eventList = new ArrayList<>();

        checkInternetConnection();

        eventRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void checkInternetConnection() {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        hasInternet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if(hasInternet){
            Log.d(TAG, "checkInternetConnection: " + "has internet");
            getEventFromFirebase();
        } else {
            getEventFromLocalDatabase();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        monthlyCalendar.setUserCurrentMonthYear(currentMonth, currentYear);

        monthlyCalendar.setCallBack(new MonthlyCalendar.DayClickListener() {
            @Override
            public void onDayClick(View view, String dateValue, String month) {
                intentToEventActivity(dateValue, month);
            }
        });
    }

    private void intentToEventActivity(String dateValue, String month) {
        date = dateValue;
        Log.d("picked date", "date picked: " + date);
        Intent intent = new Intent(CalendarActivity.this, EventActivity.class);
        intent.putExtra("date", date);
        intent.putExtra("month", month);
        monthName = month;
        startActivity(intent);
    }

    private void getEventFromFirebase() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        monthName = MonthlyCalendar.ENG_MONTH_NAMES[currentMonth];

        ref.child("month").child(monthName).child("event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable <DataSnapshot> events = dataSnapshot.getChildren();

                for (DataSnapshot child : events) {
                    Event event = child.getValue(Event.class);
                    Log.d(TAG, "onDataChange: " + event.month);
                    eventList.add(event);
                    eventAdapter = new EventAdapter(eventList);
                }
                Log.d(TAG, "onDataChange: " + eventList.size());
                eventRecyclerView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void getEventFromLocalDatabase(){
        databaseHelper = CalendarDatabaseHelper.getInstance(CalendarActivity.this);
        eventList= databaseHelper.getEvent();
        eventAdapter = new EventAdapter(eventList);
        eventRecyclerView.setAdapter(eventAdapter);
    }
}
