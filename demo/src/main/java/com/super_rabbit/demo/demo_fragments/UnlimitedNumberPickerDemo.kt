package com.super_rabbit.demo.demo_fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.super_rabbit.demo.R

class UnlimitedNumberPickerDemo : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unlimited_number_picker_demo, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                UnlimitedNumberPickerDemo().apply {
                    arguments = Bundle().apply {}
                }
    }
}
