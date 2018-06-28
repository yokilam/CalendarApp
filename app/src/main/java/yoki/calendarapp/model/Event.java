package yoki.calendarapp.model;

/**
 * Created by yokilam on 6/26/18.
 */

public class Event {
    public String month;
    public String date;
    public String title;
    public String description;
    public String startTime;
    public String endTime;

    public static Event from(String date, String title, String description, String startTime, String endTime) {
        Event event= new Event();
        event.date = date;
        event.title = title;
        event.startTime = startTime;
        event.endTime = endTime;
        return event;
    }
}
