package yoki.calendarapp.calendarMobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
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
import yoki.calendarapp.calendarMobile.util.MonthlyCalendar;
import yoki.calendarapp.R;
import yoki.calendarapp.model.Event;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "firebase";
    @BindView(R.id.monthly_calendar) MonthlyCalendar monthlyCalendar;
    @BindView(R.id.event_rv) RecyclerView eventRecyclerView;
    private Calendar calendar;
    private int currentMonth, currentDate, currentYear;
    private String date;
    private EventAdapter eventAdapter;
    List<Event> eventList;
    DatabaseReference rootReference;
    private String monthName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        calendar= Calendar.getInstance();
        currentMonth= calendar.get(Calendar.MONTH);
        currentYear= calendar.get(Calendar.YEAR);

        eventList= new ArrayList<>();

        rootReference= FirebaseDatabase.getInstance().getReference("date");

        monthlyCalendar.setUserCurrentMonthYear(currentMonth,currentYear);

        monthlyCalendar.setCallBack(new MonthlyCalendar.DayClickListener() {
            @Override
            public void onDayClick(View view, String dateValue, String month) {
                date=dateValue;
                Log.d("picked date", "date picked: "+ date);
                Intent intent= new Intent(CalendarActivity.this, EventActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("month", month);
                monthName=month;
                startActivity(intent);
            }
        });

        eventRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        ChildEventListener childEventListener= new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                for (DataSnapshot eventSnapShot : dataSnapshot.getChildren()) {
//                    Event event= eventSnapShot.getValue(Event.class);
//                    eventList.add(event);
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        rootReference.child("date").addChildEventListener(childEventListener);
//        eventAdapter = new EventAdapter(eventList);

        rootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapShot : dataSnapshot.getChildren()) {
                    Event event= eventSnapShot.getValue(Event.class);
                    Log.d(TAG, "onDataChange: " + event.title);
                    eventList.add(event);
                }
                eventAdapter = new EventAdapter(eventList);
                Log.d(TAG, "onDataChange: " + eventList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
