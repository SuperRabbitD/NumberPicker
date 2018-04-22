package com.wang.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wang.ui.wheel_picker.WheelPicker
import com.wang.demo.wheel_picker_adapters.WPDayPickerAdapter

class DayPicker : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_day_picker)
        val picker = findViewById<WheelPicker>(R.id.testPicker)
        picker.setAdapter(WPDayPickerAdapter())
    }
}