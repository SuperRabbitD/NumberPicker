package com.super_rabbit.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_normal_number_picker.*

class NormalNumberPicker : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_number_picker)

        var picker = number
        picker.scrollTo(4)
    }
}
