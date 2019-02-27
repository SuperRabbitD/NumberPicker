package com.super_rabbit.demo.demo_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.super_rabbit.demo.R
import com.super_rabbit.demo.wheel_picker_adapters.DayAdapter
import com.super_rabbit.demo.wheel_picker_adapters.MonthAdapter
import com.super_rabbit.wheel_picker.OnValueChangeListener
import com.super_rabbit.wheel_picker.WheelPicker
import kotlinx.android.synthetic.main.fragment_birthday_picker.*
import java.util.*

class BirthdayPickerFragment : androidx.fragment.app.Fragment() {

    val TAG = BirthdayPickerFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_birthday_picker, container, false)
    }

    private val monthAdapter = MonthAdapter()
    private var dayAdapter = DayAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        day.setAdapter(dayAdapter)
        month.setAdapter(monthAdapter)

        val birthday = with(Calendar.getInstance()) {
            Birthday(get(Calendar.DAY_OF_MONTH), get(Calendar.MONTH), get(Calendar.YEAR).minus(18))
        }

        day.scrollToValue(birthday.day.toString())
        month.scrollToValue(monthAdapter.getValue(birthday.month))
        year.scrollToValue(birthday.year.toString())


        day.onValueChange { _, _, newValue ->
            Log.v(TAG, "day=$newValue")
        }

        month.onValueChange { _, _, _ ->
            clampDaysByMonth()
        }

        year.onValueChange { _, _, _ ->
            clampDaysByMonth()
        }
    }

    private fun clampDaysByMonth() {
        val selectedDay = day.getCurrentItem()

        val daysInMonth = GregorianCalendar(year.getCurrentItem().toInt(), monthAdapter.getPosition(month.getCurrentItem()), 1)
            .getActualMaximum(Calendar.DAY_OF_MONTH)

        dayAdapter.days.clear()
        dayAdapter.days.addAll((1..daysInMonth).toMutableList())

        dayAdapter.notifyDataSetChanged()

        var pos = dayAdapter.getPosition(selectedDay)
        if (pos == -1)
            pos = dayAdapter.getMaxIndex()

        Log.v(TAG, "scroll to pos=$pos selectedDay=$selectedDay daysInMonth=$daysInMonth dayAdapter=${dayAdapter.getMaxIndex()}")

        day.setValue(selectedDay)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BirthdayPickerFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}


fun Int.clamp(min: Int, max: Int): Int {
    if (this < min) return min
    return if (this > max) max else this
}

data class Birthday(
    val day: Int,
    /**
     * Note: starts at 1, e.g. January is 1, December is 12
     */
    val month: Int,
    val year: Int
)


open class SimpleOnValueChangeListener : OnValueChangeListener {
    override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
    }
}

inline fun WheelPicker.onValueChange(crossinline block: (picker: WheelPicker, oldValue: String, newValue: String) -> Unit) {
    setOnValueChangedListener(object : SimpleOnValueChangeListener() {
        override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) = block(picker, oldVal, newVal)
    })
}