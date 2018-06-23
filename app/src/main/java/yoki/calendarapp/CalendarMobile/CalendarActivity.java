package yoki.calendarapp.CalendarMobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

import yoki.calendarapp.CalendarMobile.Util.MonthlyCalendar;
import yoki.calendarapp.R;

public class CalendarActivity extends AppCompatActivity {

    private Calendar calendar;
    private int currentMonth, currentDate, currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar= Calendar.getInstance();
        currentMonth= calendar.get(Calendar.MONTH);
        currentYear= calendar.get(Calendar.YEAR);


        MonthlyCalendar monthlyCalendar= findViewById(R.id.monthly_calendar);
        monthlyCalendar.setUserCurrentMonthYear(currentMonth,currentYear);

        monthlyCalendar.setCallBack(new MonthlyCalendar.DayClickListener() {
            @Override
            public void onDayClick(View view) {

            }
        });

    }
}
