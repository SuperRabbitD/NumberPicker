package com.wang.ui.messageComposerLayout

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout

/**
 * Created by wanglu on 4/7/18.
 */
val SHARE_PREFERENCE_NAME = "EmotionKeyboard"
val SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height"

class MessageComposerViewManager : AccessoryViewChangeListener {
    private var mContentView: ContentView? = null
    private var mComposerBody: ComposerBody? = null
    private var mAccessoryViewGroupLayout: AccessoryViewGroupLayout? = null
    private var mActivity: Activity? = null
    private var mInputManager: InputMethodManager? = null
    private var sp: SharedPreferences? = null
    private var mMessageComposerView: MessageComposerView? = null
    private var mIsAccessoryGroupLayoutShown = false
    private var mMaxAccessoryViewHeight: Int? = 0

    private var mAccessoryViewStatus: MutableMap<String, Boolean> = mutableMapOf()

    companion object {
        @JvmStatic
        fun newInstance(activity: Activity, composerView: MessageComposerView): MessageComposerViewManager {
            val messageComposerViewManager = MessageComposerViewManager()
            messageComposerViewManager.mActivity = activity
            messageComposerViewManager.mInputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            messageComposerViewManager.sp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE)
            messageComposerViewManager.mMessageComposerView = composerView
            return messageComposerViewManager
        }
    }

    fun bindContentView(contentView: ContentView): MessageComposerViewManager {
        mContentView = contentView
        val params = LinearLayout.LayoutParams(MATCH_PARENT, 0);
        params.weight = 1.0f
        mContentView!!.layoutParams = params
        mContentView!!.setManager(this)
        return this
    }

    fun bindComposerView(composerBody: ComposerBody): MessageComposerViewManager {
        mComposerBody = composerBody
        mComposerBody!!.setManager(this)
        mComposerBody?.setListener(object : ComposerBodyListener {
            override fun onStartEdit() {
                if (mIsAccessoryGroupLayoutShown) {
                    lockMessageBodyHeight()
                    fadeOutAccessoryView()
                    unlockMessageBodyHeightDelayed()
                    mIsAccessoryGroupLayoutShown = false
                }
            }

            override fun onAccessoryViewButtonClicked(show: Boolean, viewTag: String) {
                mAccessoryViewStatus.put(viewTag, show)
                if (mIsAccessoryGroupLayoutShown) {
                    if (show) {
                        mAccessoryViewGroupLayout?.showAccessoryView(viewTag, true)
                    } else {
                        if (isSoftInputShown()) {
                            lockMessageBodyHeight()
                            fadeOutAccessoryView()
                            unlockMessageBodyHeightDelayed()
                        } else {
                            moveAccessoryViewDown()
                        }

                        mIsAccessoryGroupLayoutShown = false
                    }
                } else {
                    if (!show)
                        return

                    mIsAccessoryGroupLayoutShown = true
                    if (isSoftInputShown()) {
                        lockMessageBodyHeight()
                        Tools.hideSoftKeyboard(mActivity, mAccessoryViewGroupLayout)
                        mAccessoryViewGroupLayout?.showAccessoryView(viewTag, true)
                        unlockMessageBodyHeightDelayed()
                    } else {
                        mAccessoryViewGroupLayout?.showAccessoryView(viewTag, false)
                    }
                    moveAccessoryViewUp()
                }
            }
        })
        return this
    }

    fun bindAccessoryView(accessoryView: AccessoryView): MessageComposerViewManager {
        if (mAccessoryViewGroupLayout == null) {
            mAccessoryViewGroupLayout = AccessoryViewGroupLayout(mActivity!!)
            mAccessoryViewGroupLayout!!.setAccessoryViewChangeListener(this)
            mAccessoryViewGroupLayout!!.setManager(this)
        }
        val accessoryViewHeight = Tools.getViewHeightBeforeDrawn(accessoryView, mActivity!!)
        mMaxAccessoryViewHeight = Math.max(mMaxAccessoryViewHeight!!, accessoryViewHeight)
        mAccessoryViewGroupLayout!!.addAccessoryView(accessoryView.getViewTag(), accessoryView)
        mAccessoryViewStatus.put(accessoryView.getViewTag(), false)
        return this
    }

    fun build(): MessageComposerView? {
        if (mMessageComposerView == null)
            return mMessageComposerView

        if (mContentView != null) {
            mMessageComposerView!!.addView(mContentView)
        }

        if (mComposerBody != null) {
            mMessageComposerView!!.addView(mComposerBody)
        }

        if (mAccessoryViewGroupLayout != null) {
            mMessageComposerView!!.addView(mAccessoryViewGroupLayout)
            /**
             * The accessory should be outside the screen before it shows
             */
            (mAccessoryViewGroupLayout!!.layoutParams as LinearLayout.LayoutParams)
                    .setMargins(0, 0, 0, -mMaxAccessoryViewHeight!!)
            mAccessoryViewGroupLayout!!.visibility = GONE
            mMessageComposerView!!.requestLayout()
        }

        return mMessageComposerView
    }

    private fun lockMessageBodyHeight() {
        if (mContentView == null)
            return
        mContentView!!.layoutParams.height = mContentView!!.height
        (mContentView!!.layoutParams as LinearLayout.LayoutParams).weight = 0f
        mContentView!!.requestLayout()
    }

    private fun unlockMessageBodyHeightDelayed() {
        if (mContentView == null)
            return

        mContentView!!.postDelayed({
            mContentView?.layoutParams?.height = 0
            (mContentView?.layoutParams as LinearLayout.LayoutParams).weight = 1f
            mContentView?.requestLayout()
        }, 100)
    }

    private fun isSoftInputShown(): Boolean {
        return getSupportSoftInputHeight() != 0;
    }

    private fun moveAccessoryViewUp() {
        mAccessoryViewGroupLayout?.visibility = VISIBLE
        val va: ValueAnimator = ValueAnimator.ofInt(-mMaxAccessoryViewHeight!!, 0)
        va.addUpdateListener { valueAnimator ->
            val h = valueAnimator.animatedValue as Int
            (mAccessoryViewGroupLayout?.layoutParams as LinearLayout.LayoutParams).setMargins(0, 0, 0, h)
            mAccessoryViewGroupLayout?.requestLayout()
        }
        va.duration = 100
        va.start()
    }

    private fun moveAccessoryViewDown() {
        val va: ValueAnimator = ValueAnimator.ofInt(0, -mMaxAccessoryViewHeight!!)
        va.addUpdateListener { valueAnimator ->
            val h = valueAnimator.animatedValue as Int
            (mAccessoryViewGroupLayout?.layoutParams as LinearLayout.LayoutParams).setMargins(0, 0, 0, h)
            mAccessoryViewGroupLayout?.requestLayout()
        }
        va.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                mAccessoryViewGroupLayout?.visibility = GONE
                mAccessoryViewGroupLayout?.hideAll()
            }
        })
        va.duration = 100
        va.start()
    }

    private fun fadeOutAccessoryView() {
        val va: ValueAnimator = ValueAnimator.ofFloat(1f, 0f)
        va.addUpdateListener { valueAnimator ->
            val alpha = valueAnimator.animatedValue as Float
            mAccessoryViewGroupLayout?.alpha = alpha
        }
        va.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                mAccessoryViewGroupLayout?.visibility = GONE
                mAccessoryViewGroupLayout?.hideAll()
                mAccessoryViewGroupLayout?.alpha = 1f
                mAccessoryViewGroupLayout?.requestLayout()
            }
        })
        va.duration = 50
        va.start()

        for ((key) in mAccessoryViewStatus) {
            mAccessoryViewStatus.put(key, false)
        }
    }

    private fun getSupportSoftInputHeight(): Int {
        val r = Rect()
        mActivity!!.window.decorView.getWindowVisibleDisplayFrame(r)
        val screenHeight = mActivity!!.window.decorView.rootView.height
        var softInputHeight = screenHeight - r.bottom
        if (Build.VERSION.SDK_INT >= 20) {
            softInputHeight -= getSoftButtonsBarHeight()
        }
        if (softInputHeight > 0) {
            sp!!.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply()
        }
        return softInputHeight
    }

    private fun getSoftButtonsBarHeight(): Int {
        val metrics = DisplayMetrics()
        mActivity!!.windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        if (Build.VERSION.SDK_INT >= 20) {
            mActivity!!.windowManager.defaultDisplay!!.getRealMetrics(metrics)
            val realHeight = metrics.heightPixels
            return if (realHeight > usableHeight) {
                realHeight - usableHeight
            } else {
                0
            }
        }
        return 0
    }

    fun getAccessoryViewStatus(tag: String): Boolean {
        return mAccessoryViewStatus[tag]!!
    }

    override fun onAccessoryViewStatusChanged(tag: String, show: Boolean) {
        mAccessoryViewStatus[tag] = show
        mComposerBody?.onNotifyAccessoryViewStatusChanged(tag, show)
    }
}