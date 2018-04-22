package com.wang.ui.message_composer_layout

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by wanglu on 4/9/18.
 */
object Tools {
    fun getViewHeightBeforeDrawn(view: View, activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        view.measure(size.x, size.y)

        return view.measuredHeight
    }

    fun hideSoftKeyboard(context: Context?, view: View?) {
        if (context == null || view == null) return
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}