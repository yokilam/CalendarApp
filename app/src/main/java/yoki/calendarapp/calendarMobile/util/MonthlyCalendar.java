package yoki.calendarapp.calendarMobile.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import yoki.calendarapp.R;

import static android.content.ContentValues.TAG;

public class MonthlyCalendar extends LinearLayout {
    private static final String[] ENG_MONTH_NAMES = {"January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"};
    private Calendar calendar;
    private LinearLayout weekOne, weekTwo, weekThree, weekFour, weekFive, weekSix;
    private LinearLayout[] weeks;
    private TextView currentMonth, currentYear;
    private Button selectedDayButton;
    private Button[] days;
    private int currentDateDay, chosenDateDay, userMonth, userYear, currentDateMonth, chosenMonth, currentDateYear, chosenYear,
            pickedDateDay, pickedDateMonth, pickedDateYear;;
    private LinearLayout.LayoutParams userButtonParams, defaultButtonParams;
    private DayClickListener myListener;


    public MonthlyCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Log.d(TAG, "metrics: " + metrics.toString());

        View view = LayoutInflater.from(context).inflate(R.layout.calendar_month, this, true);
        calendar = Calendar.getInstance();

        setupViews(view);

        currentDateDay = chosenDateDay = calendar.get(Calendar.DAY_OF_MONTH);

        Log.d(TAG, "currentDateDay: " + currentDateDay);
        Log.d(TAG, "currentDateMonth: " + currentDateMonth);
        Log.d(TAG, "chosenMonth: " + chosenMonth);
        Log.d(TAG, "chosenYear: " + chosenYear);
        Log.d(TAG, "userMonth: " + userMonth);
        Log.d(TAG, "userYear: " + userYear);

        if (userMonth != 0 && userYear != 0) {
            currentDateMonth = chosenMonth = userMonth;
            currentDateYear = chosenYear = userYear;
        } else {
            currentDateMonth = chosenMonth = calendar.get(Calendar.MONTH);
            currentDateYear = chosenYear = calendar.get(Calendar.YEAR);
        }
        Log.d(TAG, "currentMonth: " + Calendar.MONTH);
        currentMonth.setText(ENG_MONTH_NAMES[currentDateMonth] + " ");
        currentYear.setText(String.valueOf(currentDateYear));

        initializeDaysAndWeeks();
        if (userButtonParams != null) {
            defaultButtonParams = userButtonParams;
        } else {
            defaultButtonParams = getdaysLayoutParams();
        }
        addDaysInCalendar(defaultButtonParams, context, metrics);
        initCalendarWithDate(chosenYear, chosenMonth, chosenDateDay);

    }

    private void initCalendarWithDate(int year, int month, int date) {
        calendar.set(year, month, date);

        int daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "daysInCurrentMonth= " + daysInCurrentMonth);

        chosenYear = year;
        chosenMonth = month;
        chosenDateDay = date;

        calendar.set(year, month, 1);
        int firstDayOfCurrentMonth = calendar.get(Calendar.DAY_OF_WEEK)-1;
        Log.d(TAG, "firstDayOfCurrentMonth: " + firstDayOfCurrentMonth);

        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        int dayNumber = 1;
        int daysLeftInFirstWeek = 0;
        int indexOfDayAfterLastDayOfMonth = 0;

        if (firstDayOfCurrentMonth != 1) {
            daysLeftInFirstWeek = firstDayOfCurrentMonth;
            Log.d(TAG, "daysLeftInFirstWeek: " + daysLeftInFirstWeek);
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth;
            Log.d(TAG, "indexOfDayAfterLasyDayOfMonth " + indexOfDayAfterLastDayOfMonth);
            for (int i = firstDayOfCurrentMonth; i < firstDayOfCurrentMonth + daysInCurrentMonth; ++i) {
                if (currentDateMonth == chosenMonth
                        && currentDateYear == chosenYear
                        && dayNumber == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                    days[i].setTextColor(Color.WHITE);
                } else {
                    days[i].setTextColor(Color.BLACK);
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                Log.d(TAG, "dayNumber" + dayNumber);
                dateArr[1] = chosenMonth;
                dateArr[2] = chosenYear;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));

                days[i].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDayClick(v);
                    }
                });
                dayNumber++;
            }
        }

        if (month > 0){
            calendar.set(year, month - 1, 1);
        } else {
            calendar.set(year - 1, 11, 1);
        }
        int daysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "daysInPreviousMonth: " + daysInPreviousMonth);

        for (int i = daysLeftInFirstWeek - 1; i >= 0; i--) {
            int[] dateArr = new int[3];

            if (chosenMonth > 0) {
                if (currentDateMonth == chosenMonth - 1
                        && currentDateYear == chosenYear
                        && daysInPreviousMonth == currentDateDay) {
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = chosenMonth - 1;
                dateArr[2] = chosenYear;
            } else {
                if (currentDateMonth == 11
                        && currentDateYear == chosenYear - 1
                        && daysInPreviousMonth == currentDateDay) {
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = 11;
                dateArr[2] = chosenYear - 1;
            }

            days[i].setTag(dateArr);
            days[i].setText(String.valueOf(daysInPreviousMonth--));
            days[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDayClick(v);
                }
            });
        }
    }

    public void onDayClick(View view) {
        myListener.onDayClick(view, String.valueOf(pickedDateDay));

        if (selectedDayButton != null) {
            if (chosenYear == currentDateYear
                    && chosenMonth == currentDateMonth
                    && pickedDateDay == currentDateDay) {
                selectedDayButton.setBackgroundColor(getResources().getColor(R.color.pink));
                selectedDayButton.setTextColor(Color.WHITE);
            } else {
                selectedDayButton.setBackgroundColor(Color.TRANSPARENT);
                if (selectedDayButton.getCurrentTextColor() != Color.RED) {
                    selectedDayButton.setTextColor(getResources()
                            .getColor(R.color.calendar_number));
                }
            }
        }

        selectedDayButton = (Button) view;
        if (selectedDayButton.getTag() != null) {
            int[] dateArray = (int[]) selectedDayButton.getTag();
            pickedDateDay = dateArray[0];
            pickedDateMonth = dateArray[1];
            pickedDateYear = dateArray[2];
        }

        if (pickedDateYear == currentDateYear
                && pickedDateMonth == currentDateMonth
                && pickedDateDay == currentDateDay) {
            selectedDayButton.setBackgroundColor(getResources().getColor(R.color.pink));
            selectedDayButton.setTextColor(Color.WHITE);
        } else {
            selectedDayButton.setBackgroundColor(getResources().getColor(R.color.colorMainBgLight));
            if (selectedDayButton.getCurrentTextColor() != Color.RED) {
                selectedDayButton.setTextColor(Color.GRAY);
            }
        }
    }


    private void addDaysInCalendar(LayoutParams defaultButtonParams, Context context, DisplayMetrics metrics) {
        int daysCounter = 0;

        for (int weekNumber = 0; weekNumber < 6; weekNumber++) {
            for (int dayInWeek = 0; dayInWeek < 7; dayInWeek++) {
                final Button day = new Button(context);
                day.setTextColor(getResources().getColor(R.color.customGrey));
                day.setBackgroundColor(Color.TRANSPARENT);
                day.setLayoutParams(defaultButtonParams);
                day.setTextSize((int) metrics.density * 8);
                day.setSingleLine();

                days[daysCounter] = day;
                weeks[weekNumber].addView(day);

                daysCounter++;
            }
        }
    }

    private LayoutParams getdaysLayoutParams() {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;
        return buttonParams;
    }

    private void initializeDaysAndWeeks() {
        weeks = new LinearLayout[6];
        days = new Button[42];

        weeks[0] = weekOne;
        weeks[1] = weekTwo;
        weeks[2] = weekThree;
        weeks[3] = weekFour;
        weeks[4] = weekFive;
        weeks[5] = weekSix;
    }

    private void setupViews(View view) {
        weekOne = view.findViewById(R.id.calendar_week_1);
        weekTwo = view.findViewById(R.id.calendar_week_2);
        weekThree = view.findViewById(R.id.calendar_week_3);
        weekFour = view.findViewById(R.id.calendar_week_4);
        weekFive = view.findViewById(R.id.calendar_week_5);
        weekSix = view.findViewById(R.id.calendar_week_6);
        currentYear = view.findViewById(R.id.current_year);
        currentMonth = view.findViewById(R.id.current_month);
    }

    public interface DayClickListener {
        void onDayClick(View view, String dateValue);
    }

    public void setCallBack(DayClickListener mListener) {
        this.myListener = mListener;
    }

    public void setUserCurrentMonthYear(int userMonth, int userYear) {
        this.userMonth = userMonth;
        this.userYear = userYear;
    }

}
