package com.super_rabbit.demo.demo_fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.super_rabbit.demo.R
import com.super_rabbit.demo.wheel_picker_adapters.WPAMPMPickerAdapter
import com.super_rabbit.demo.wheel_picker_adapters.WPDayPickerAdapter
import com.super_rabbit.wheel_picker.WheelPicker
import com.syw.domore.adapter.reminder_time_picker_adapters.WPHoursPickerAdapter
import com.syw.domore.adapter.reminder_time_picker_adapters.WPMinutesPickerAdapter

class DateAndTimePickerFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_and_time_pikcer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<WheelPicker>(R.id.date_picker).setAdapter(WPDayPickerAdapter())
        view.findViewById<WheelPicker>(R.id.hour_picker).setAdapter(WPHoursPickerAdapter())
        view.findViewById<WheelPicker>(R.id.minutes_picker).setAdapter(WPMinutesPickerAdapter())
        view.findViewById<WheelPicker>(R.id.am_pm_picker).setAdapter(WPAMPMPickerAdapter())
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                DateAndTimePickerFragment().apply {
                    arguments = Bundle().apply {}
                }
    }
}
