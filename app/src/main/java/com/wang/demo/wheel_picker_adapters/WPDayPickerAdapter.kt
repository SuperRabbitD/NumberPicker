package com.wang.demo.wheel_picker_adapters

import com.wang.ui.wheelPicker.WheelAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wanglu on 3/28/18.
 */
class WPDayPickerAdapter : WheelAdapter {
    override fun getValue(position: Int): String {
        if (position == 0)
            return "Today"

        if(position == -1)
            return "Yesterday"

        if (position == 1)
            return "Tomorrow"

        val curDate = Date(System.currentTimeMillis())// 获取当前时间
        val rightNow = Calendar.getInstance()
        rightNow.time = curDate;
        rightNow.add(Calendar.DATE, position);// 日期减1年

        val simpleDateFormat = SimpleDateFormat("MMM d, yyyy")// 输入日期的格式
        return simpleDateFormat.format(rightNow.time)
    }

    override fun getPosition(value: String): Int {
        return 0
    }

    override fun getTextWithMaximumLength(): String {
        return "Mmm 00, 0000"
    }

    override fun getMaxIndex(): Int {
        return Integer.MAX_VALUE
    }

    override fun getMinIndex(): Int {
        return Integer.MIN_VALUE
    }
}