package com.example.productive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    String curDate;
    SQLiteDatabase sqLiteDatabase;
    EditText typeinEvent;
    DBHelper dbHelper;
    ArrayAdapter adapter;

    public static ArrayList<Event> events = new ArrayList<>();
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public void goToCalculator(View view) {
        Intent intent = new Intent(this, Calculator.class);
        startActivity(intent);
    }

    public void goToCalendar(View view) {
        Intent intent = new Intent(this, Calendar.class);
        startActivity(intent);
    }

    public void goToSyllabus(View view) {
        Intent intent = new Intent(this, Syllabus.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        //context.deleteDatabase("events");
        sqLiteDatabase = context.openOrCreateDatabase("events",Context.MODE_PRIVATE, null);
        dbHelper = new DBHelper(sqLiteDatabase);

        ListView listView = findViewById(R.id.homeListView);
        DateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
        Date date1 = new Date();

        TextView date = findViewById(R.id.showDate);
        date.setText(dateFormat.format(date1));
        curDate = dateFormat.format(date1);
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
                AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Delete?");
                Event curevent = eventsList.get(position);
                adb.setMessage("Are you sure you want to delete '" + curevent.getTitle() + "' ?");
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Event curevent = eventsList.get(position);

                        eventsList = dbHelper.deleteEntry(curevent.getDate(), curevent.getContent());
                        adapter.notifyDataSetChanged();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }});
                adb.show();
            }
        });
    }


}
