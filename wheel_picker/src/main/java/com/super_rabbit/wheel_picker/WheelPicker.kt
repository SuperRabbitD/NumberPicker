package com.super_rabbit.wheel_picker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.OverScroller
import androidx.core.content.ContextCompat
import java.util.*

/**
 * Created by wanglu on 3/10/18.
 */
/**
 * Interface to listen for changes of the current value.
 */
interface OnValueChangeListener {

    /**
     * Called upon a change of the current value.
     *
     * @param picker The NumberPicker associated with this listener.
     * @param oldVal The previous value.
     * @param newVal The new value.
     */
    fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String)
}

interface OnScrollListener {
    /**
     * Callback invoked while the number picker scroll state has changed.
     *
     * @param view The view whose scroll state is being reported.
     * @param scrollState The current scroll state. One of
     * [.SCROLL_STATE_IDLE],
     * [.SCROLL_STATE_TOUCH_SCROLL] or
     * [.SCROLL_STATE_IDLE].
     */
    fun onScrollStateChange(view: WheelPicker, scrollState: Int)

    companion object {

        /**
         * The view is not scrolling.
         */
        val SCROLL_STATE_IDLE = 0

        /**
         * The user is scrolling using touch, and his finger is still on the screen.
         */
        val SCROLL_STATE_TOUCH_SCROLL = 1

        /**
         * The user had previously been scrolling using touch and performed a fling.
         */
        val SCROLL_STATE_FLING = 2
    }
}

class WheelPicker @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val TAG = WheelPicker::class.java.simpleName

    private val TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9f
    private val SNAP_SCROLL_DURATION = 300
    private val SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 4
    private val DEFAULT_ITEM_COUNT = 3;
    private val DEFAULT_TEXT_SIZE = 80

    private var mSelectorItemCount: Int
    private var mSelectorVisibleItemCount: Int
    private var mMinIndex: Int
    private var mMaxIndex: Int

    private var mWheelMiddleItemIndex: Int
    private var mWheelVisibleItemMiddleIndex: Int
    private var mSelectorItemIndices: ArrayList<Int>
    private var mCurSelectedItemIndex = 0
    private var mWrapSelectorWheelPreferred: Boolean

    private var mTextPaint: Paint? = null
    private var mSelectedTextColor: Int
    private var mUnSelectedTextColor: Int
    private var mTextSize: Int
    private var mTextAlign: String

    private var mOverScroller: OverScroller? = null
    private var mVelocityTracker: VelocityTracker? = null
    private val mTouchSlop: Int
    private val mMaximumVelocity: Int
    private val mMinimumVelocity: Int
    private var mLastY: Float = 0f
    private var mIsDragging: Boolean = false
    private var mCurrentFirstItemOffset: Int = 0
    private var mInitialFirstItemOffset = Int.MIN_VALUE
    private var mTextGapHeight: Int = 0
    private var mItemHeight: Int = 0
    private var mTextHeight: Int = 0
    private var mPreviousScrollerY: Int = 0
    private var mOnValueChangeListener: OnValueChangeListener? = null
    private var mOnScrollListener: OnScrollListener? = null
    private var mAdapter: WheelAdapter? = null
    private var mFadingEdgeEnabled = true
    /**
     * The current scroll state of the number picker.
     */
    private var mScrollState = OnScrollListener.SCROLL_STATE_IDLE

    init {
        val attributesArray = context.obtainStyledAttributes(attrs, R.styleable.WheelPicker, defStyleAttr, 0)

        mSelectorItemCount = attributesArray.getInt(R.styleable.WheelPicker_wheelItemCount, DEFAULT_ITEM_COUNT) + 2
        mWheelMiddleItemIndex = (mSelectorItemCount - 1) / 2
        mSelectorVisibleItemCount = mSelectorItemCount - 2
        mWheelVisibleItemMiddleIndex = (mSelectorVisibleItemCount - 1) / 2
        mSelectorItemIndices = ArrayList(mSelectorItemCount)

        mMinIndex = attributesArray.getInt(R.styleable.WheelPicker_min, Integer.MIN_VALUE)
        mMaxIndex = attributesArray.getInt(R.styleable.WheelPicker_max, Integer.MAX_VALUE)
        mWrapSelectorWheelPreferred = attributesArray.getBoolean(R.styleable.WheelPicker_wrapSelectorWheel, false)


        mOverScroller = OverScroller(context, DecelerateInterpolator(2.5f))
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity / SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity

        mSelectedTextColor = attributesArray.getColor(
            R.styleable.WheelPicker_selectedTextColor
            , ContextCompat.getColor(context, R.color.color_4_blue)
        );
        mUnSelectedTextColor = attributesArray.getColor(
            R.styleable.WheelPicker_textColor
            , ContextCompat.getColor(context, R.color.color_3_dark_blue)
        );
        mTextSize = attributesArray.getDimensionPixelSize(R.styleable.WheelPicker_textSize, DEFAULT_TEXT_SIZE);
        val textAlignInt = attributesArray.getInt(R.styleable.WheelPicker_align, 1)
        mTextAlign = when (textAlignInt) {
            0 -> "LEFT"
            1 -> "CENTER"
            2 -> "RIGHT"
            else -> "CENTER"
        }
        mFadingEdgeEnabled = attributesArray.getBoolean(R.styleable.WheelPicker_fadingEdgeEnabled, true)

        mTextPaint = Paint()
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.textSize = mTextSize.toFloat()
        mTextPaint!!.textAlign = Paint.Align.valueOf(mTextAlign)
        mTextPaint!!.style = Paint.Style.FILL_AND_STROKE

        attributesArray.recycle()

        initializeSelectorWheelIndices()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            // need to do all this when we know our size
            initializeSelectorWheel()
            initializeFadingEdges()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Try greedily to fit the max width and height.
        var lp: ViewGroup.LayoutParams? = layoutParams
        if (lp == null)
            lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        var width = calculateSize(suggestedMinimumWidth, lp.width, widthMeasureSpec)
        var height = calculateSize(suggestedMinimumHeight, lp.height, heightMeasureSpec)

        width += paddingLeft + paddingRight
        height += paddingTop + paddingBottom


        setMeasuredDimension(width, height)
    }

    override fun getSuggestedMinimumWidth(): Int {
        var suggested = super.getSuggestedMinimumHeight()
        if (mSelectorVisibleItemCount > 0) {
            suggested = Math.max(suggested, computeMaximumWidth())
        }
        return suggested
    }

    override fun getSuggestedMinimumHeight(): Int {
        var suggested = super.getSuggestedMinimumWidth()
        if (mSelectorVisibleItemCount > 0) {
            val fontMetricsInt = mTextPaint!!.fontMetricsInt
            val height = fontMetricsInt.descent - fontMetricsInt.ascent
            suggested = Math.max(suggested, height * mSelectorVisibleItemCount)
        }
        return suggested
    }

    private fun computeMaximumWidth(): Int {
        mTextPaint!!.textSize = mTextSize * 1.3f
        if (mAdapter != null) {
            return if (!mAdapter!!.getTextWithMaximumLength().isEmpty()) {
                val suggestedWith = mTextPaint!!.measureText(mAdapter!!.getTextWithMaximumLength()).toInt()
                mTextPaint!!.textSize = mTextSize * 1.0f
                suggestedWith
            } else {
                val suggestedWith = mTextPaint!!.measureText("0000").toInt()
                mTextPaint!!.textSize = mTextSize * 1.0f
                suggestedWith
            }
        }
        val widthForMinIndex = mTextPaint!!.measureText(mMinIndex.toString()).toInt()
        val widthForMaxIndex = mTextPaint!!.measureText(mMaxIndex.toString()).toInt()
        mTextPaint!!.textSize = mTextSize * 1.0f
        return if (widthForMinIndex > widthForMaxIndex)
            widthForMinIndex
        else
            widthForMaxIndex
    }

    private fun calculateSize(suggestedSize: Int, paramSize: Int, measureSpec: Int): Int {
        var result = 0
        val size = View.MeasureSpec.getSize(measureSpec)
        val mode = View.MeasureSpec.getMode(measureSpec)

        when (View.MeasureSpec.getMode(mode)) {
            View.MeasureSpec.AT_MOST ->

                if (paramSize == ViewGroup.LayoutParams.WRAP_CONTENT)
                    result = Math.min(suggestedSize, size)
                else if (paramSize == ViewGroup.LayoutParams.MATCH_PARENT)
                    result = size
                else {
                    result = Math.min(paramSize, size)
                }
            View.MeasureSpec.EXACTLY -> result = size
            View.MeasureSpec.UNSPECIFIED ->

                result = if (paramSize == ViewGroup.LayoutParams.WRAP_CONTENT || paramSize == ViewGroup.LayoutParams
                        .MATCH_PARENT
                )
                    suggestedSize
                else {
                    paramSize
                }
        }

        return result
    }

    private fun initializeSelectorWheel() {
        mItemHeight = getItemHeight()
        mTextHeight = computeTextHeight()
        mTextGapHeight = getGapHeight()

        val visibleMiddleItemPos = mItemHeight * mWheelVisibleItemMiddleIndex + (mItemHeight + mTextHeight) / 2
        mInitialFirstItemOffset = visibleMiddleItemPos - mItemHeight * mWheelMiddleItemIndex
        mCurrentFirstItemOffset = mInitialFirstItemOffset
    }

    private fun initializeFadingEdges() {
        isVerticalFadingEdgeEnabled = mFadingEdgeEnabled
        if (mFadingEdgeEnabled)
            setFadingEdgeLength((bottom - top - mTextSize) / 2)
    }

    private fun initializeSelectorWheelIndices() {
        mSelectorItemIndices.clear()
        for (i in 0 until mSelectorItemCount) {
            var selectorIndex = i - mWheelMiddleItemIndex
            if (mWrapSelectorWheelPreferred) {
                selectorIndex = getWrappedSelectorIndex(selectorIndex)
            }
            mSelectorItemIndices.add(selectorIndex)
        }
    }

    override fun getBottomFadingEdgeStrength(): Float {
        return TOP_AND_BOTTOM_FADING_EDGE_STRENGTH
    }

    override fun getTopFadingEdgeStrength(): Float {
        return TOP_AND_BOTTOM_FADING_EDGE_STRENGTH
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawVertical(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        onTouchEventVertical(event)
        return true
    }

    private fun onTouchEventVertical(event: MotionEvent) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }

        mVelocityTracker?.addMovement(event)

        val action: Int = event.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mOverScroller!!.isFinished)
                    mOverScroller!!.forceFinished(true)


                mLastY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                var deltaY = event.y - mLastY
                if (!mIsDragging && Math.abs(deltaY) > mTouchSlop) {
                    parent?.requestDisallowInterceptTouchEvent(true)

                    if (deltaY > 0) {
                        deltaY -= mTouchSlop
                    } else {
                        deltaY += mTouchSlop
                    }
                    onScrollStateChange(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    mIsDragging = true
                }

                if (mIsDragging) {
                    scrollBy(0, deltaY.toInt())
                    invalidate()
                    mLastY = event.y
                }
            }
            MotionEvent.ACTION_UP -> {
                if (mIsDragging) {
                    mIsDragging = false;
                    parent?.requestDisallowInterceptTouchEvent(false)

                    mVelocityTracker?.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                    val velocity = mVelocityTracker?.yVelocity?.toInt()

                    if (Math.abs(velocity!!) > mMinimumVelocity) {
                        mPreviousScrollerY = 0
                        mOverScroller?.fling(
                            scrollX, scrollY, 0, velocity, 0, 0, Integer.MIN_VALUE,
                            Integer.MAX_VALUE, 0, (getItemHeight() * 0.7).toInt()
                        )
                        invalidateOnAnimation()
                        onScrollStateChange(OnScrollListener.SCROLL_STATE_FLING)
                    }
                    recyclerVelocityTracker()
                } else {
                    //click event
                    val y = event.y.toInt()
                    handlerClickVertical(y)
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                if (mIsDragging) {
                    mIsDragging = false
                }
                recyclerVelocityTracker()
            }
        }
    }

    private fun handlerClickVertical(y: Int) {
        val selectorIndexOffset = y / mItemHeight - mWheelVisibleItemMiddleIndex
        changeValueBySteps(selectorIndexOffset)
    }

    override fun scrollBy(x: Int, y: Int) {
        if (y == 0)
            return

        val gap = mTextGapHeight

        if (!mWrapSelectorWheelPreferred && y > 0 && mSelectorItemIndices[mWheelMiddleItemIndex] <= mMinIndex) {
            if (mCurrentFirstItemOffset + y - mInitialFirstItemOffset < gap / 2)
                mCurrentFirstItemOffset += y
            else {
                mCurrentFirstItemOffset = mInitialFirstItemOffset + (gap / 2)
                if (!mOverScroller!!.isFinished && !mIsDragging) {
                    mOverScroller!!.abortAnimation()
                }
            }
            return
        }

        if (!mWrapSelectorWheelPreferred && y < 0
            && mSelectorItemIndices[mWheelMiddleItemIndex] >= mMaxIndex
        ) {

            if (mCurrentFirstItemOffset + y - mInitialFirstItemOffset > -(gap / 2))
                mCurrentFirstItemOffset += y
            else {
                mCurrentFirstItemOffset = mInitialFirstItemOffset - (gap / 2)
                if (!mOverScroller!!.isFinished && !mIsDragging) {
                    mOverScroller!!.abortAnimation()
                }
            }
            return
        }

        mCurrentFirstItemOffset += y

        while (mCurrentFirstItemOffset - mInitialFirstItemOffset < -gap) {
            mCurrentFirstItemOffset += mItemHeight
            increaseSelectorsIndex()
            if (!mWrapSelectorWheelPreferred && mSelectorItemIndices[mWheelMiddleItemIndex] >= mMaxIndex) {
                mCurrentFirstItemOffset = mInitialFirstItemOffset
            }
        }

        while (mCurrentFirstItemOffset - mInitialFirstItemOffset > gap) {
            mCurrentFirstItemOffset -= mItemHeight
            decreaseSelectorsIndex()
            if (!mWrapSelectorWheelPreferred && mSelectorItemIndices[mWheelMiddleItemIndex] <= mMinIndex) {
                mCurrentFirstItemOffset = mInitialFirstItemOffset
            }
        }
        onSelectionChanged(mSelectorItemIndices[mWheelMiddleItemIndex], true)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mOverScroller!!.computeScrollOffset()) {
            val x = mOverScroller!!.currX
            val y = mOverScroller!!.currY


            if (mPreviousScrollerY == 0) {
                mPreviousScrollerY = mOverScroller!!.startY
            }
            scrollBy(x, y - mPreviousScrollerY)
            mPreviousScrollerY = y
            invalidate()
        } else {
            if (!mIsDragging)
            //align item
                adjustItemVertical()
        }
    }

    private fun adjustItemVertical() {
        mPreviousScrollerY = 0
        var deltaY = mInitialFirstItemOffset - mCurrentFirstItemOffset

        if (Math.abs(deltaY) > mItemHeight / 2) {
            deltaY += if (deltaY > 0)
                -mItemHeight
            else
                mItemHeight
        }

        if (deltaY != 0) {
            mOverScroller!!.startScroll(scrollX, scrollY, 0, deltaY, 800)
            invalidateOnAnimation()
        }

        onScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE)
    }

    private fun recyclerVelocityTracker() {
        mVelocityTracker?.recycle()
        mVelocityTracker = null
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
    }

    private fun onScrollStateChange(scrollState: Int) {
        if (mScrollState == scrollState) {
            return
        }
        mScrollState = scrollState
        mOnScrollListener?.onScrollStateChange(this, scrollState)
    }

    private fun getItemHeight(): Int {
        return height / (mSelectorItemCount - 2)
    }

    private fun getGapHeight(): Int {
        return getItemHeight() - computeTextHeight()
    }

    private fun computeTextHeight(): Int {
        val metricsInt = mTextPaint!!.fontMetricsInt
        return Math.abs(metricsInt.bottom + metricsInt.top)
    }

    private fun invalidateOnAnimation() {
        if (Build.VERSION.SDK_INT >= 16)
            postInvalidateOnAnimation()
        else
            invalidate()
    }

    private fun drawVertical(canvas: Canvas) {
        if (mSelectorItemIndices.size == 0)
            return
        val itemHeight = getItemHeight()

        val x = when (mTextPaint!!.textAlign) {
            Paint.Align.LEFT -> paddingLeft.toFloat()
            Paint.Align.CENTER -> ((right - left) / 2).toFloat()
            Paint.Align.RIGHT -> (right - left).toFloat() - paddingRight.toFloat()
            else -> ((right - left) / 2).toFloat()
        }

        var y = mCurrentFirstItemOffset.toFloat()


        var i = 0

        val topIndexDiffToMid = mWheelVisibleItemMiddleIndex;
        val bottomIndexDiffToMid = mSelectorVisibleItemCount - mWheelVisibleItemMiddleIndex - 1
        val maxIndexDiffToMid = Math.max(topIndexDiffToMid, bottomIndexDiffToMid)

        while (i < mSelectorItemIndices.size) {
            var scale = 1f

            val offsetToMiddle = Math.abs(y - (mInitialFirstItemOffset + mWheelMiddleItemIndex * itemHeight).toFloat())

            if (maxIndexDiffToMid != 0)
                scale = 0.3f * (itemHeight * maxIndexDiffToMid - offsetToMiddle) / (itemHeight * maxIndexDiffToMid) + 1

            if (offsetToMiddle < mItemHeight / 2) {
                mTextPaint!!.color = mSelectedTextColor
            } else {
                mTextPaint!!.color = mUnSelectedTextColor
            }
            canvas.save()
            canvas.scale(scale, scale, x, y)
            canvas.drawText(getValue(mSelectorItemIndices[i]), x, y, mTextPaint!!)
            canvas.restore()

            y += itemHeight
            i++
        }
    }

    private fun getPosition(value: String): Int = when {
        mAdapter != null -> mAdapter!!.getPosition(value)
        else -> try {
            val position = value.toInt()
            validatePosition(position)
        } catch (e: NumberFormatException) {
            0
        }
    }

    private fun increaseSelectorsIndex() {
        for (i in 0 until (mSelectorItemIndices.size - 1)) {
            mSelectorItemIndices[i] = mSelectorItemIndices[i + 1]
        }
        var nextScrollSelectorIndex = mSelectorItemIndices[mSelectorItemIndices.size - 2] + 1
        if (mWrapSelectorWheelPreferred && nextScrollSelectorIndex > mMaxIndex) {
            nextScrollSelectorIndex = mMinIndex
        }
        mSelectorItemIndices[mSelectorItemIndices.size - 1] = nextScrollSelectorIndex
    }

    private fun decreaseSelectorsIndex() {
        for (i in mSelectorItemIndices.size - 1 downTo 1) {
            mSelectorItemIndices[i] = mSelectorItemIndices[i - 1]
        }
        var nextScrollSelectorIndex = mSelectorItemIndices[1] - 1
        if (mWrapSelectorWheelPreferred && nextScrollSelectorIndex < mMinIndex) {
            nextScrollSelectorIndex = mMaxIndex
        }
        mSelectorItemIndices[0] = nextScrollSelectorIndex
    }

    private fun changeValueBySteps(steps: Int) {
        mPreviousScrollerY = 0
        mOverScroller!!.startScroll(0, 0, 0, -mItemHeight * steps, SNAP_SCROLL_DURATION)
        invalidate()
    }

    private fun onSelectionChanged(current: Int, notifyChange: Boolean) {
        val previous = mCurSelectedItemIndex
        mCurSelectedItemIndex = current
        if (notifyChange && previous != current) {
            notifyChange(previous, current)
        }
    }

    private fun getWrappedSelectorIndex(selectorIndex: Int): Int {
        if (selectorIndex > mMaxIndex) {
            return mMinIndex + (selectorIndex - mMaxIndex) % (mMaxIndex - mMinIndex + 1) - 1
        } else if (selectorIndex < mMinIndex) {
            return mMaxIndex - (mMinIndex - selectorIndex) % (mMaxIndex - mMinIndex + 1) + 1
        }
        return selectorIndex
    }

    private fun notifyChange(previous: Int, current: Int) {
        mOnValueChangeListener?.onValueChange(this, getValue(previous), getValue(current))
    }

    private fun validatePosition(position: Int): Int {
        return if (!mWrapSelectorWheelPreferred) {
            when {
                position > mMaxIndex -> mMaxIndex
                position < mMinIndex -> mMinIndex
                else -> position
            }
        } else {
            getWrappedSelectorIndex(position)
        }
    }

    fun scrollTo(position: Int) {
        if (mCurSelectedItemIndex == position)
            return

        mCurSelectedItemIndex = position
        mSelectorItemIndices.clear()
        for (i in 0 until mSelectorItemCount) {
            var selectorIndex = mCurSelectedItemIndex + (i - mWheelMiddleItemIndex)
            if (mWrapSelectorWheelPreferred) {
                selectorIndex = getWrappedSelectorIndex(selectorIndex)
            }
            mSelectorItemIndices.add(selectorIndex)
        }
    }

    fun setOnValueChangedListener(onValueChangeListener: OnValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener
    }

    fun setOnScrollListener(onScrollListener: OnScrollListener) {
        mOnScrollListener = onScrollListener
    }

    fun smoothScrollTo(position: Int) {
        val realPosition = validatePosition(position)
        changeValueBySteps(realPosition - mCurSelectedItemIndex)
    }

    fun smoothScrollToValue(value: String) {
        smoothScrollTo(getPosition(value))
    }

    fun scrollToValue(value: String) {
        scrollTo(getPosition(value))
    }

    fun setUnselectedTextColor(resourceId: Int) {
        mUnSelectedTextColor = resourceId
    }

    /**
     * Set user define adapter
     *
     * @adapter user define adapter
     * @indexRangeBasedOnAdapterSize specific if the picker's min~max range is based on adapter's size
     */
    fun setAdapter(adapter: WheelAdapter?, indexRangeBasedOnAdapterSize: Boolean = true) {
        mAdapter = adapter
        if (mAdapter == null) {
            invalidate()
            return
        }

        if (adapter!!.getSize() != -1 && indexRangeBasedOnAdapterSize) {
            mMaxIndex = adapter.getSize() - 1
            mMinIndex = 0
        }
        invalidate()

        mAdapter?.picker = this
    }

    /**
     * Sets whether the selector wheel shown during flinging/scrolling should
     * wrap around the {@link NumberPicker#getMinValue()} and
     * {@link NumberPicker#getMaxValue()} values.
     * <p>
     * By default if the range (max - min) is more than the number of items shown
     * on the selector wheel the selector wheel wrapping is enabled.
     * </p>
     * <p>
     * <strong>Note:</strong> If the number of items, i.e. the range (
     * {@link #getMaxValue()} - {@link #getMinValue()}) is less than
     * the number of items shown on the selector wheel, the selector wheel will
     * not wrap. Hence, in such a case calling this method is a NOP.
     * </p>
     *
     * @param wrapSelectorWheel Whether to wrap.
     */
    fun setWrapSelectorWheel(wrap: Boolean) {
        mWrapSelectorWheelPreferred = wrap
        requestLayout()
    }

    /**
     * Gets whether the selector wheel wraps when reaching the min/max value.
     *
     * @return True if the selector wheel wraps.
     *
     * @see .getMinValue
     * @see .getMaxValue
     */
    fun getWrapSelectorWheel(): Boolean {
        return mWrapSelectorWheelPreferred
    }

    /**
     * Set how many visible item show in the picker
     */
    fun setWheelItemCount(count: Int) {
        mSelectorItemCount = count + 2
        mWheelMiddleItemIndex = (mSelectorItemCount - 1) / 2
        mSelectorVisibleItemCount = mSelectorItemCount - 2
        mWheelVisibleItemMiddleIndex = (mSelectorVisibleItemCount - 1) / 2
        mSelectorItemIndices = ArrayList(mSelectorItemCount)
        reset()
        requestLayout()
    }

    /**
     * Set color for current selected item
     */
    fun setSelectedTextColor(colorId: Int) {
        mSelectedTextColor = ContextCompat.getColor(context, colorId);
        requestLayout()
    }

    fun getValue(position: Int): String = when {
        mAdapter != null -> mAdapter!!.getValue(position)
        else -> if (!mWrapSelectorWheelPreferred) {
            when {
                position > mMaxIndex -> ""
                position < mMinIndex -> ""
                else -> position.toString()
            }
        } else {
            getWrappedSelectorIndex(position).toString()
        }
    }

    fun setValue(value: String) {
        scrollToValue(value)
    }

    fun setMaxValue(max: Int) {
        mMaxIndex = max
    }

    fun getMaxValue(): String {
        return if (mAdapter != null) {
            mAdapter!!.getValue(mMaxIndex)
        } else {
            mMaxIndex.toString()
        }
    }

    fun setMinValue(min: Int) {
        mMinIndex = min
    }

    fun getMinValue(): String {
        return if (mAdapter != null) {
            mAdapter!!.getValue(mMinIndex)
        } else {
            mMinIndex.toString()
        }
    }

    fun reset() {
        initializeSelectorWheelIndices()
        initializeSelectorWheel()
        requestLayout()
    }

    fun getCurrentItem(): String {
        return getValue(mCurSelectedItemIndex)
    }
}


internal fun Int.clamp(min: Int, max: Int): Int {
    if (this < min) return min
    return if (this > max) max else this
}