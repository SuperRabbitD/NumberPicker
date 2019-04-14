package com.super_rabbit.wheel_picker

class DayAdapter(var days: MutableList<Int> = mutableListOf()) : WheelAdapter() {

    init {
        if (days.isEmpty())
            days.addAll((1..31).toMutableList())
    }

    /* override */ fun getMaxIndex(): Int = getSize() - 1

    /* override */ fun getMinIndex(): Int = 0

    override fun getPosition(vale: String): Int = days.indexOf(vale.toInt()).clamp(getMinIndex(), getMaxIndex())

    override fun getTextWithMaximumLength(): String = days.max().toString()

    override fun getValue(position: Int): String {
        if (position >= getMinIndex() && position <= getMaxIndex())
            return days[position].toString()

        if (position <= getMaxIndex())
            return days[position + getSize()].toString()

        if (position >= getMinIndex())
            return days[position - getSize()].toString()

        return ""
    }

    override fun getSize(): Int = days.size
}