package com.example.productive;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DBHelper {

    SQLiteDatabase sqLiteDatabase;

    public DBHelper (SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS events " +
                "(id INTEGER PRIMARY KEY, date TEXT, title TEXT, content TEXT);");
    }

    public ArrayList<Event> readNotes (String dateToFind) {

        createTable();
        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from events where date like '%s'", dateToFind), null);

        int dateIndex = c.getColumnIndex("date");
        int eventIndex = c.getColumnIndex("title");
        int descriptionIndex = c.getColumnIndex("content");

        c.moveToFirst();

        ArrayList<Event> eventList = new ArrayList<>();

        while (!c.isAfterLast()) {

            String eventTitle = c.getString(eventIndex);
            String date = c.getString(dateIndex);
            String description = c.getString(descriptionIndex);

            Event event = new Event(date, eventTitle, description);
            eventList.add(event);
            c.moveToNext();
        }
        c.close();
        //sqLiteDatabase.close();

        return eventList;
    }

    public void saveEvent(String title, String content, String date) {
        createTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO events (date, title, content) VALUES ('%s','%s','%s')",
                date, title, content));
    }

    public ArrayList<Event> deleteEntry( String date, String toFindDescription) {
        createTable();
        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from events where date like '%s'", date), null);

        sqLiteDatabase.delete("events", "date=? and content=?", new String[]{date, toFindDescription});

        int dateIndex = c.getColumnIndex("date");
        int eventIndex = c.getColumnIndex("title");
        int descriptionIndex = c.getColumnIndex("content");

        c.moveToFirst();

        ArrayList<Event> eventList = new ArrayList<>();
        while (!c.isAfterLast()) {
            String eventTitle = c.getString(eventIndex);
            String date1 = c.getString(dateIndex);
            String description = c.getString(descriptionIndex);

            Event event = new Event(date1, eventTitle, description);
            eventList.add(event);
            c.moveToNext();
        }
        c.close();
        //sqLiteDatabase.close();

        return eventList;
    }
}
