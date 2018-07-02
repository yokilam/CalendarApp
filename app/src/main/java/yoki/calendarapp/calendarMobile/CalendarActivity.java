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

    private static final String[] ENG_MONTH_NAMES = {"January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"};
    private static final String TAG = "firebase";
    @BindView(R.id.monthly_calendar)
    MonthlyCalendar monthlyCalendar;
    @BindView(R.id.event_rv)
    RecyclerView eventRecyclerView;
    private Calendar calendar;
    private int currentMonth, currentDate, currentYear;
    private String date;
    private EventAdapter eventAdapter;
    List <Event> eventList;
    DatabaseReference ref;
    private String monthName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        monthName = ENG_MONTH_NAMES[currentMonth];

        Log.d(TAG, "onCreate: " + monthName);

        eventList=new ArrayList <>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        monthlyCalendar.setUserCurrentMonthYear(currentMonth, currentYear);

        monthlyCalendar.setCallBack(new MonthlyCalendar.DayClickListener() {
            @Override
            public void onDayClick(View view, String dateValue, String month) {
                date = dateValue;
                Log.d("picked date", "date picked: " + date);
                Intent intent = new Intent(CalendarActivity.this, EventActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("month", month);
                monthName = month;
                startActivity(intent);
            }
        });

        eventRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void onStart() {
        super.onStart();

        ref.child("month").child(monthName).child("event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList=new ArrayList <>();

                Iterable<DataSnapshot> events= dataSnapshot.getChildren();

                for(DataSnapshot child: events){
                    Event event =child.getValue(Event.class);
                    Log.d(TAG, "onDataChange: "+ event.month);
                    eventList.add(event);
                }
                Log.d(TAG, "onDataChange: " + eventList.size());
                eventAdapter = new EventAdapter(eventList);
                eventRecyclerView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Event event= dataSnapshot.child("event").getValue(Event.class);
//                if (event != null) {
//                    Log.d(TAG, "onDataChange: " + event.month);
//                }else{
//                    Log.d(TAG, "onDataChange: is null, not getting any data." );
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        eventList = new ArrayList <>();
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    Log.d(TAG, child.getKey());
//                    Event event = child.getValue(Event.class);
//                    if (event != null) {
//                        Log.d(TAG, "onDataChange: " + event.month);
//                        eventList.add(event);
//                    }else{
//                        Log.d(TAG, "onDataChange: is null, not getting any data.");
//                    }
//                }
//                eventAdapter = new EventAdapter(eventList);
//                eventRecyclerView.setAdapter(eventAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

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
//        ValueEventListener eventListener= new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                Event event= dataSnapshot.getValue(Event.class);
////                Log.d(TAG, "onDataChange: " + event.title);
//
//                DataSnapshot eventSnapshot= dataSnapshot.child("date");
//                Iterable<DataSnapshot> dateChildren= eventSnapshot.getChildren();
//
//                eventList= new ArrayList<>();
//                for(DataSnapshot events: dateChildren){
////                    Event event= dataSnapshot.getValue(Event.class);
//                    Event event= events.getValue(Event.class);
//                    Log.d(TAG, "onDataChange: " + event.title);
//                    eventList.add(event);
//
//
//                }
//                eventRecyclerView.setAdapter(eventAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };

//        rootReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot eventSnapShot : dataSnapshot.getChildren()) {
//                    Event event= eventSnapShot.getValue(Event.class);
//                    Log.d(TAG, "onDataChange: " + event.title);
//                    eventList.add(event);
//                }
//                eventAdapter = new EventAdapter(eventList);
//                Log.d(TAG, "onDataChange: " + eventList.size());
//                eventRecyclerView.setAdapter(eventAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
    }
}
