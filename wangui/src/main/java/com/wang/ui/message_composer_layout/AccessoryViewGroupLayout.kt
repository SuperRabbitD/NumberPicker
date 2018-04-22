package com.wang.ui.message_composer_layout

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import com.demo.wang.ui.R


/**
 * Created by wanglu on 4/7/18.
 */

interface AccessoryViewChangeListener {
    fun onAccessoryViewStatusChanged(tag: String, show: Boolean)
}

open class AccessoryViewGroupLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var mCurVisibleView: AccessoryView? = null
    private val mAccessoryViewGroup: MutableMap<String, AccessoryView> = mutableMapOf()
    private var mAccessoryViewChangeListener: AccessoryViewChangeListener? = null

    @JvmField
    protected var mMessageComposerViewManager: MessageComposerViewManager? = null

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.accessory_view_group, this)
    }

    fun setAccessoryViewChangeListener(accessoryViewChangeListener: AccessoryViewChangeListener) {
        mAccessoryViewChangeListener = accessoryViewChangeListener
    }

    fun addAccessoryView(viewTag: String, accessoryView: AccessoryView) {
        accessoryView.visibility = View.GONE
        mAccessoryViewGroup.put(viewTag, accessoryView)
        findViewById<RelativeLayout>(R.id.accessory_view_container).addView(accessoryView)
    }

    fun setHeight(heightInDp: Float) {
        layoutParams.height = dpToPx(heightInDp)
        requestLayout()
    }

    fun showAccessoryView(viewTag: String, withAnimation: Boolean) {
        val view = mAccessoryViewGroup[viewTag] ?: return
        if (mCurVisibleView == view)
            return

        if (mCurVisibleView != null)
            mAccessoryViewChangeListener?.onAccessoryViewStatusChanged(mCurVisibleView!!.getViewTag(), false)

        mCurVisibleView?.visibility = View.GONE
        view.visibility = View.VISIBLE

        if (withAnimation) {
            val enter = AnimationUtils.loadAnimation(context, R.anim.bottom_to_top)
            enter.interpolator = DecelerateInterpolator()
            view.startAnimation(enter)
        }
        mCurVisibleView = view

        if (mCurVisibleView != null)
            mAccessoryViewChangeListener?.onAccessoryViewStatusChanged(mCurVisibleView!!.getViewTag(), true)
    }

    fun hideAll() {
        if (mCurVisibleView != null)
            mAccessoryViewChangeListener?.onAccessoryViewStatusChanged(mCurVisibleView!!.getViewTag(), false)

        mCurVisibleView?.visibility = View.GONE
        mCurVisibleView = null
    }

    private fun dpToPx(dp: Float): Int {
        val r = resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
        return px.toInt()
    }

    fun setManager(manager: MessageComposerViewManager) {
        mMessageComposerViewManager = manager
    }
}