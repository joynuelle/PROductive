package com.example.productive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;


public class Calculator extends AppCompatActivity {
    TextView response;
    EditText currentGrade;
    EditText wantGrade;
    EditText weight;
    DecimalFormat precision = new DecimalFormat("0.00");

    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void calculate(View view) {
        double cur = Double.valueOf(currentGrade.getText().toString());
        double des = Double.valueOf(wantGrade.getText().toString());
        double weigh = Double.valueOf(weight.getText().toString());

        double want = (des - cur * (1 - (weigh)/100))/(weigh/100);
        if (want > 100){
            response.setText("You'll need at least a " + precision.format(want) +"% to get your desire grade! This is impossible..");
        }
        else if (want > 90) {
            response.setText("You'll need at least a " + precision.format(want) +"% to get your desire grade! Hope you started studying already.");
        }
        else if (want < 90 && want > 80) {
            response.setText("You'll need at least a " + precision.format(want) +"% to get your desire grade! Better start studying.");
        }
        else if (want < 80 && want > 70) {
            response.setText("You'll need at least a " + precision.format(want) +"% to get your desire grade! Not super stressful, huh!");
        }
        else if (want < 70 && want > 60) {
            response.setText("You'll need at least a " + precision.format(want) +"% to get your desire grade! This should be easy");
        }
        else if (want < 60 && want > 50) {
            response.setText("You'll need at least a " + precision.format(want) +"% to get your desire grade! You're good to go!");
        }
        else if (want < 50) {
            response.setText("You'll need at least a " + precision.format(want) + "% to get your desire grade! Why bother studying!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        response = findViewById(R.id.response);
        currentGrade = findViewById(R.id.current);
        wantGrade = findViewById(R.id.goal);
        weight = findViewById(R.id.weight);
    }
}
