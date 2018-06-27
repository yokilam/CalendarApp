package yoki.calendarapp.calendarBackend;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yokilam on 6/26/18.
 */

public class CalendarDB {

    private static final String TABLE_CALENDAR = "habits_all";
    private static final String DB_NAME = "calendarApp.db";
    private static final String COMMA_SPACE = ", ";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String PRIMARY_KEY = "PRIMARY KEY ";
    private static final String TYPE_TEXT = " TEXT ";
    private static final String TYPE_DATE = " DATETIME ";
    private static final String TYPE_INT = " INTEGER ";
    private static final String AUTOINCREMENT = "AUTOINCREMENT ";
    private static final String NOT_NULL = "NOT NULL ";

    public static final String EVENT_NUM = "EVENT_NUM";
    public static final String MONTH = "MONTH";
    public static final String DATE = "DATE";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCIPTION";
    public static final String START_TIME = "START_TIME";
    public static final String END_TIME = "END_TIME";

    private static final String CREATE_TABLE_CALENDAR =
            CREATE_TABLE + DB_NAME + " ( " +
                    EVENT_NUM + TYPE_INT + NOT_NULL + PRIMARY_KEY + AUTOINCREMENT + COMMA_SPACE +
                    MONTH + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    DATE + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    TITLE + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    DESCRIPTION + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    START_TIME + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    END_TIME + TYPE_TEXT + NOT_NULL +
                    ")";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CALENDAR);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR);
    }
}
