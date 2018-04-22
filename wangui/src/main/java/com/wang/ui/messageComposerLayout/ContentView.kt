package com.wang.ui.messageComposerLayout

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout


/**
 * Created by wanglu on 4/7/18.
 */
open class ContentView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    protected var mMessageComposerViewManager: MessageComposerViewManager? = null

    fun setManager(manager: MessageComposerViewManager) {
        mMessageComposerViewManager = manager
    }
}