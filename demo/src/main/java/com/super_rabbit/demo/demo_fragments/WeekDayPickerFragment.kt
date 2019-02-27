package com.super_rabbit.demo.demo_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.super_rabbit.demo.R
import com.super_rabbit.demo.wheel_picker_adapters.WPWeekDaysPickerAdapter
import com.super_rabbit.wheel_picker.WheelPicker

class WeekDayPickerFragment : androidx.fragment.app.Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week_day_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<WheelPicker>(R.id.week_day_picker).setAdapter(WPWeekDaysPickerAdapter())
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            WeekDayPickerFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
