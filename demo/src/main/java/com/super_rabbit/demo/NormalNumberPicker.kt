package com.super_rabbit.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.super_rabbit.demo.wheel_picker_adapters.WPDayPickerAdapter
import kotlinx.android.synthetic.main.activity_normal_number_picker.*
import kotlinx.android.synthetic.main.activity_normal_number_picker.view.*

class NormalNumberPicker : AppCompatActivity() {
    private var mIsRoundedWrapPreferred = false
    private var mWheelItemCount = 5
    private var mCurSelectedTextColor = R.color.color_4_blue
    private var mIsDayPicker = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_number_picker)
        set_wrap.setOnClickListener({
            mIsRoundedWrapPreferred = !mIsRoundedWrapPreferred
            number_picker.setSelectorRoundedWrapPreferred(mIsRoundedWrapPreferred)
            set_wrap.text = String.format("Set wrap (current = %s)", mIsRoundedWrapPreferred.toString())
            number_picker.reset()
        })

         set_wheel_item_count.setOnClickListener({
             mWheelItemCount = if (mWheelItemCount == 5){
                 3
             } else {
                 5
             }
             number_picker.setWheelItemCount(mWheelItemCount)
             set_wheel_item_count.text = String.format("Set wheel item count (current = %s)", mWheelItemCount.toString())
         })

        set_selected_color.setOnClickListener({
            if (mCurSelectedTextColor == R.color.color_4_blue){
                mCurSelectedTextColor = R.color.color_7_yellow
                set_selected_color.text = String.format("set selected color (current = yellow)")
            } else {
                mCurSelectedTextColor = R.color.color_4_blue
                set_selected_color.text = String.format("set selected color (current = blue)")
            }
            number_picker.setSelectedTextColor(mCurSelectedTextColor)
        })
        set_style.setOnClickListener{
            mIsDayPicker = !mIsDayPicker
            if (mIsDayPicker) {
                set_style.text = "set picker style (current = day picker)"
                number_picker.setAdapter(WPDayPickerAdapter())
                number_picker.requestLayout()
            } else {
                set_style.text = "set picker style (current = normal number picker)"
                number_picker.setAdapter(null)
                number_picker.setMax(9)
                number_picker.setMin(0)
                number_picker.reset()
            }
        }
    }
}
