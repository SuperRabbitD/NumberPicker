package com.super_rabbit.demo.wheel_picker_adapters

import com.super_rabbit.wheel_picker.WheelAdapter


/**
 * Created by wanglu on 3/28/18.
 */
class WPHoursPickerAdapter : WheelAdapter() {
    override fun getValue(position: Int): String {
        return when (position) {
            0 -> "1"
            1 -> "2"
            2 -> "3"
            3 -> "4"
            4 -> "5"
            5 -> "6"
            6 -> "7"
            7 -> "8"
            8 -> "9"
            9 -> "10"
            10 -> "11"
            11 -> "12"
            else -> ""
        }
    }

    override fun getPosition(vale: String): Int {
        return when (vale) {
            "1" -> 0
            "2" -> 1
            "3" -> 2
            "4" -> 3
            "5" -> 4
            "6" -> 5
            "7" -> 6
            "8" -> 7
            "9" -> 8
            "10" -> 9
            "11" -> 10
            "12" -> 11
            else -> 0
        }
    }

    override fun getTextWithMaximumLength(): String {
        return "12"
    }
}