package com.wang.ui.message_composer_layout

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Created by wanglu on 4/7/18.
 */
interface ComposerBodyListener {
    fun onStartEdit()
    fun onAccessoryViewButtonClicked(show: Boolean, viewTag: String)
}

abstract class ComposerBody @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    @JvmField
    protected var mMessageComposerViewManager: MessageComposerViewManager? = null

    private var mListener : ComposerBodyListener?=null

    fun setListener(composerBodyListener: ComposerBodyListener){
        mListener = composerBodyListener
    }

    protected fun onEditStarted(){
        mListener?.onStartEdit()
    }

    protected fun onAccessoryViewButtonClicked(show: Boolean, viewTag: String){
        mListener?.onAccessoryViewButtonClicked(show, viewTag)
    }

    fun setManager(manager: MessageComposerViewManager) {
        mMessageComposerViewManager = manager
    }

    abstract fun onNotifyAccessoryViewStatusChanged()

    abstract fun onNotifyAccessoryViewStatusChanged(tag: String, show: Boolean)
}