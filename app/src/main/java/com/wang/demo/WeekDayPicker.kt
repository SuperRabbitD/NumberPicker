package com.wang.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wang.ui.wheelPicker.WheelPicker
import com.wang.demo.wheel_picker_adapters.WPWeekDaysPickerAdapter

class WeekDayPicker : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_day_picker)
        val picker = findViewById<WheelPicker>(R.id.testPicker)
        picker.setAdapter(WPWeekDaysPickerAdapter())
    }
}
