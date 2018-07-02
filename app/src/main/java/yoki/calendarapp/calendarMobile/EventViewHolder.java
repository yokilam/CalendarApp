package yoki.calendarapp.calendarMobile;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import yoki.calendarapp.R;
import yoki.calendarapp.model.Event;

import static android.content.ContentValues.TAG;

public class EventViewHolder extends RecyclerView.ViewHolder {
//    @BindView(R.id.event_date) TextView eventDate;
//    @BindView(R.id.event_title) TextView eventTitle;
//    @BindView(R.id.event_desc) TextView eventDesc;
//    @BindView(R.id.event_start_time) TextView startTime;
//    @BindView(R.id.event_end_time) TextView endTime;

    private TextView eventDate;
    private TextView eventTitle;
    private TextView eventDesc;
    private TextView startTime;
    private TextView endTime;

    public EventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        eventDate= itemView.findViewById(R.id.event_date);
        eventTitle= itemView.findViewById(R.id.event_title);
        eventDesc= itemView.findViewById(R.id.event_desc);
        startTime= itemView.findViewById(R.id.event_start_time);
        endTime= itemView.findViewById(R.id.event_end_time);
    }

    public void onBind(Event event) {
        StringBuilder date = new StringBuilder();
        date.append(event.month).append(" ").append(event.date).append(": ");
        Log.d(TAG, "onBind: " + event.month + event.date);
        eventDate.setText(date.toString());
        eventTitle.setText(event.title);
        eventDesc.setText(event.description);
        startTime.setText(event.startTime);
        endTime.setText(event.endTime);
    }
}
