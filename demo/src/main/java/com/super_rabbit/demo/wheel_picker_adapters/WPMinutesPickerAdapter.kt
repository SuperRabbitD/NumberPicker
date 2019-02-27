package com.super_rabbit.demo.wheel_picker_adapters

import com.super_rabbit.wheel_picker.WheelAdapter


/**
 * Created by wanglu on 3/28/18.
 */
class WPMinutesPickerAdapter : WheelAdapter() {
    override fun getValue(position: Int): String {
        if (position < 10)
            return "0$position"

        return position.toString()
    }

    override fun getPosition(vale: String): Int {
        return when (vale) {
            "00" -> 0
            "01" -> 1
            "02" -> 2
            "03" -> 3
            "04" -> 4
            "05" -> 5
            "06" -> 6
            "07" -> 7
            "08" -> 8
            "09" -> 9
            else -> vale.toInt()
        }
    }

    override fun getTextWithMaximumLength(): String {
        return "00"
    }
}