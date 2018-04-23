package com.super_rabbit.demo.wheel_picker_adapters

import com.super_rabbit.wheel_picker.WheelAdapter

/**
 * Created by wanglu on 3/28/18.
 */
class WPAMPMPickerAdapter : WheelAdapter {
    override fun getValue(position: Int): String {
        return when(position){
            0 -> "AM"
            1 -> "PM"
            else -> ""
        }
    }

    override fun getPosition(vale: String): Int {
        return when(vale){
            "AM" -> 0
            "PM" -> 1
            else -> 0
        }
    }

    override fun getTextWithMaximumLength(): String {
        return "AM"
    }

    override fun getMaxIndex(): Int {
        return 1
    }

    override fun getMinIndex(): Int {
        return 0
    }
}