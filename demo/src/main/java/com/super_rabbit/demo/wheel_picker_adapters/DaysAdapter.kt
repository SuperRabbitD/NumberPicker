package com.super_rabbit.demo.wheel_picker_adapters

import com.super_rabbit.demo.demo_fragments.clamp
import com.super_rabbit.wheel_picker.WheelAdapter

class DayAdapter(var days: MutableList<Int> = mutableListOf()) : WheelAdapter() {

    init {
        if (days.isEmpty())
            days.addAll((1..31).toMutableList())
    }

    /* override */ fun getMaxIndex(): Int = days.size - 1

    /* override */ fun getMinIndex(): Int = 0

    override fun getPosition(vale: String): Int = days.indexOf(vale.toInt())

    override fun getTextWithMaximumLength(): String = days.max().toString()

    override fun getValue(position: Int): String = days[position.clamp(0, getMaxIndex())].toString()
}