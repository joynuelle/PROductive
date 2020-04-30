package com.example.productive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import java.text.SimpleDateFormat;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.content.DialogInterface;
import android.widget.TextView;
import android.app.AlertDialog;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Calendar extends AppCompatActivity {

    public static ArrayList<Event> events = new ArrayList<>();

    CalendarView calendarView;
    String curDate;
    SQLiteDatabase sqLiteDatabase;
    EditText typeinEvent;
    DBHelper dbHelper;
    ArrayAdapter adapter;
    DateFormat dateFormat;
    Date date1;

    public static ArrayList<Event> eventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar2);

        Context context = getApplicationContext();
        //context.deleteDatabase("events");
        sqLiteDatabase = context.openOrCreateDatabase("events",Context.MODE_PRIVATE, null);
        dbHelper = new DBHelper(sqLiteDatabase);

        typeinEvent = findViewById(R.id.typeInEvent);
        calendarView = findViewById(R.id.calendarView);
        ListView listView = findViewById(R.id.listview);


        dateFormat = new SimpleDateFormat("M/d/yyyy");
        date1 = new Date();
        curDate = dateFormat.format(date1);
        TextView date = findViewById(R.id.date);
        date.setText(dateFormat.format(date1));

        eventsList = dbHelper.readNotes(curDate);
        final ArrayList<String> displayEvents = new ArrayList<>();
        for (Event curEvent : eventsList) {
            displayEvents.add(String.format("Title: %s\nDate: %s\nContent:%s", curEvent.getTitle(), curEvent.getDate(), curEvent.getContent()));
        }

        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, displayEvents);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(Calendar.this);
                adb.setTitle("Delete?");
                Event curevent = eventsList.get(position);
                adb.setMessage("Are you sure you want to delete '" + curevent.getTitle() + "' ?");
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Event curevent = eventsList.get(position);
                        if (curevent.getDate() == "null") {
                            eventsList = dbHelper.deleteEntry(dateFormat.format(date1), curevent.getContent());
                        } else {
                            eventsList = dbHelper.deleteEntry(curevent.getDate(), curevent.getContent());
                        }
                        adapter.notifyDataSetChanged();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }});
                adb.show();
            }
        });



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                curDate = month+1 + "/"+ dayOfMonth  + "/" + year;
                TextView date = findViewById(R.id.date);
                date.setText(curDate);

                eventsList = dbHelper.readNotes(curDate);
                ArrayList<String> displayEvents = new ArrayList<>();
                for (Event curEvent : eventsList) {
                    displayEvents.add(String.format("Title: %s\nDate: %s\nContent:%s", curEvent.getTitle(), curEvent.getDate(), curEvent.getContent()));
                }
                ListView listView = findViewById(R.id.listview);

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, displayEvents);
                listView.setAdapter(adapter);
            }
        });
    }


    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveFunction(View view) {
        EditText eventText = findViewById(R.id.typeInEvent);
        EditText eventTitle = findViewById(R.id.title);
        String title = eventTitle.getText().toString();
        String content = eventText.getText().toString();

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events",Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        dbHelper.saveEvent(title, content, curDate);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
