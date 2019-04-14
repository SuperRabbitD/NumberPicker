package com.super_rabbit.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.super_rabbit.demo.demo_fragments.BirthdayPickerFragment;
import com.super_rabbit.demo.demo_fragments.DateAndTimePickerFragment;
import com.super_rabbit.demo.demo_fragments.UnlimitedNumberPickerDemo;
import com.super_rabbit.demo.demo_fragments.WeekDayPickerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(BirthdayPickerFragment.newInstance());
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.show_style_demo:
                intent = new Intent(MainActivity.this, DemoActivity.class);
                startActivity(intent);
                break;
            case R.id.show_unlimited_number_picker:
                replaceFragment(UnlimitedNumberPickerDemo.newInstance());
                break;
            case R.id.show_week_day_picker:
                replaceFragment(WeekDayPickerFragment.newInstance());
                break;
            case R.id.show_day_picker:
                replaceFragment(DateAndTimePickerFragment.newInstance());
                break;
            case R.id.show_birthday_day_picker:
                replaceFragment(BirthdayPickerFragment.newInstance());
                break;
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
}
