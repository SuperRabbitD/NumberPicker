package com.wang.ui.message_composer_layout

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by wanglu on 4/7/18.
 */
open class MessageComposerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }
}