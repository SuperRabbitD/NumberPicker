package com.super_rabbit.demo.wheel_picker_adapters

import com.super_rabbit.wheel_picker.WheelAdapter

/**
 * Created by wanglu on 3/28/18.
 */
class WPWeekDaysPickerAdapter : WheelAdapter() {
    override fun getValue(position: Int): String {
        return when (position) {
            0 -> "Monday"
            1 -> "Tuesday"
            2 -> "Wednesday"
            3 -> "Thursday"
            4 -> "Friday"
            5 -> "Saturday"
            6 -> "Sunday"
            else -> ""
        }
    }

    override fun getPosition(vale: String): Int {
        return when (vale) {
            "Monday" -> 0
            "Tuesday" -> 1
            "Wednesday" -> 2
            "Thursday" -> 3
            "Friday" -> 4
            "Saturday" -> 5
            "Sunday" -> 6
            else -> 0
        }
    }

    override fun getTextWithMaximumLength(): String {
        return "Wednesday"
    }
}