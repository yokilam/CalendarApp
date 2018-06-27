package yoki.calendarapp.calendarMobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import yoki.calendarapp.calendarMobile.util.MonthlyCalendar;
import yoki.calendarapp.R;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.monthly_calendar) MonthlyCalendar monthlyCalendar;

    private Calendar calendar;
    private int currentMonth, currentDate, currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        calendar= Calendar.getInstance();
        currentMonth= calendar.get(Calendar.MONTH);
        currentYear= calendar.get(Calendar.YEAR);

        monthlyCalendar.setUserCurrentMonthYear(currentMonth,currentYear);

        monthlyCalendar.setCallBack(new MonthlyCalendar.DayClickListener() {
            @Override
            public void onDayClick(View view) {
                Intent intent= new Intent(CalendarActivity.this, EventActivity.class);
                startActivity(intent);
            }
        });
    }
}
