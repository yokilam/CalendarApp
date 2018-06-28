package yoki.calendarapp.calendarBackend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import yoki.calendarapp.model.Event;

public class CalendarDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "calendarDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EVENT = "event";

    public CalendarDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE =
                "CREATE TABLE " + TABLE_EVENT + "("
                        + "eventId INTEGER PRIMARY KEY,"
                        + "month TEXT NOT NULL,"
                        + "date TEXT NOT NULL,"
                        + "title TEXT NOT NULL,"
                        + "description TEXT NOT NULL,"
                        + "startTime TEXT NOT NULL,"
                        + "endTime TEXT NOT NULL"
                        + ")";

        db.execSQL(CREATE_EVENTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
            onCreate(db);
        }
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("month", event.month);
            values.put("date", event.date);
            values.put("title", event.title);
            values.put("description", event.description);
            values.put("startTime", event.startTime);
            values.put("endTime", event.endTime);
            db.insertOrThrow(TABLE_EVENT, null, values);
        } catch (Exception e) {
            Log.e("EVENT DB", "Error while trying to add post to database", e);
        }
    }

    public void deleteEvent(int eventId) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.delete(TABLE_EVENT, "eventId = ?", new String[] { String.valueOf(eventId) });
        } catch (Exception e) {
            Log.e("EVENT DB", "Error while trying to delete all posts and users", e);
        }
    }


    public List<Event> getEvent() {
        List<Event> events = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENT, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int eventId = cursor.getInt(cursor.getColumnIndex("movieId"));
                    String month = cursor.getString(cursor.getColumnIndex("month"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                    String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                    events.add(Event.from(date, title, description, startTime, endTime));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Event DB", "Error while trying to get events from database", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return events;
    }
}
