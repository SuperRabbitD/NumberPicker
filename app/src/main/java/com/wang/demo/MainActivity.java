package com.wang.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.show_normal_picker:
                intent = new Intent(MainActivity.this, NormalNumberPicker.class);
                startActivity(intent);
                break;
            case R.id.show_unlimited_number_picker:
                intent = new Intent(MainActivity.this, UnlimitedNumberPicker.class);
                startActivity(intent);
                break;
            case R.id.show_week_day_picker:
                intent = new Intent(MainActivity.this, WeekDayPicker.class);
                startActivity(intent);
                break;
            case R.id.show_day_picker:
                intent = new Intent(MainActivity.this, DayPicker.class);
                startActivity(intent);
                break;
            case R.id.show_am_pm_picker:
                intent = new Intent(MainActivity.this, AMPMPicker.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
