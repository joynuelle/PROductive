package com.example.productive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Event> events = new ArrayList<>();

    public void goToCalculator(View view) {
        Intent intent = new Intent(this, Calculator.class);
        startActivity(intent);
    }

    public void goToCalendar(View view) {
        Intent intent = new Intent(this, Calendar.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
