package yoki.calendarapp.calendarMobile;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import yoki.calendarapp.R;
import yoki.calendarapp.model.Event;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private List<Event> eventsList = Collections.emptyList();

    public EventAdapter(List <Event> eventsList) {
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_itemview, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.onBind(eventsList.get(position));
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
