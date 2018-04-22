package com.wang.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wang.ui.wheel_picker.WheelPicker
import com.wang.demo.wheel_picker_adapters.WPAMPMPickerAdapter

class AMPMPicker : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ampmpicker)

        val picker = findViewById<WheelPicker>(R.id.testPicker)
        picker.setAdapter(WPAMPMPickerAdapter())
    }
}
