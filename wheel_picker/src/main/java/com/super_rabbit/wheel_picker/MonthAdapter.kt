package com.super_rabbit.wheel_picker

import java.text.DateFormatSymbols

class MonthAdapter : WheelAdapter() {

    private val months = DateFormatSymbols.getInstance().months

    /* override */ fun getMaxIndex(): Int = getSize() - 1

    /* override */ fun getMinIndex(): Int = 0

    override fun getValue(position: Int): String {
        if (position >= getMinIndex() && position <= getMaxIndex())
            return months[position]

        if (position <= getMaxIndex())
            return months[position + getSize()]

        if (position >= getMinIndex())
            return months[position - getSize()]

        return ""
    }

    override fun getPosition(vale: String): Int = months.indexOf(vale).clamp(getMinIndex(), getMaxIndex())

    override fun getTextWithMaximumLength(): String = months.maxBy { it.length } ?: ""

    override fun getSize(): Int = months.size
}