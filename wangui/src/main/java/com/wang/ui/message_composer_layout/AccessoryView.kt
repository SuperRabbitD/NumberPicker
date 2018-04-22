package com.wang.ui.message_composer_layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

/**
 * Created by wanglu on 4/10/18.
 */
abstract class AccessoryView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    abstract fun getViewTag(): String
}