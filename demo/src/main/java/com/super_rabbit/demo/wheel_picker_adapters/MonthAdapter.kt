package com.super_rabbit.demo.wheel_picker_adapters

import com.super_rabbit.demo.demo_fragments.clamp
import com.super_rabbit.wheel_picker.WheelAdapter
import java.text.DateFormatSymbols

class MonthAdapter : WheelAdapter {

    private val months = DateFormatSymbols.getInstance().months

    /* override */ fun getMaxIndex(): Int = months.size - 1

    /* override */ fun getMinIndex(): Int = 0

    override fun getValue(position: Int): String = months[position.clamp(0, getMaxIndex())]

    override fun getPosition(vale: String): Int = months.indexOf(vale)

    override fun getTextWithMaximumLength(): String = months.maxBy { it.length } ?: ""
}