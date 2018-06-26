package yoki.calendarapp.CalendarMobile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by yokilam on 6/25/18.
 */


@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

        EditText et_time;
        String finaltime;
        EventActivity.GetTime listner;

        public TimePickerFragment(String time, EditText et_time, EventActivity.GetTime listner) {
            this.et_time = et_time;
            this.finaltime = time;
            this.listner = listner;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            try {
                listner.getTimeMethod(hourOfDay, minute);
                String time, convention;
                int hrs, min;
                String hrsS, minS;

                finaltime = "00:" + hourOfDay + ":" + minute;

                if (hourOfDay > 12) {
                    hrs = hourOfDay - 12;
                    convention = "PM";
                } else {
                    hrs = hourOfDay;
                    convention = "AM";
                }
                hrsS = String.valueOf((hrs < 10) ? "0" + hrs : hrs);
                minS = String.valueOf((minute < 10) ? "0" + minute : minute);
                et_time.setText(hrsS + ":" + minS + " " + convention);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
}
