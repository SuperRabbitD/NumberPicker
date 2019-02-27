package com.super_rabbit.wheel_picker

/**
 * Created by wanglu on 3/28/18.
 */
abstract class WheelAdapter {

    abstract fun getValue(position: Int): String

    abstract fun getPosition(vale: String): Int

    /**
     * get the text with potential maximum print length for support "WRAP_CONTENT" attribute
     * if not sure, return empty("") string, in that case "WRAP_CONTENT" will behavior like "MATCH_PARENT"
     */
    abstract fun getTextWithMaximumLength(): String

    /**
     * get the elements size of the adapter, if the adapter does not contains an element array, default is -1
     */
    fun getSize(): Int = -1

    var picker: WheelPicker? = null

    fun notifyDataSetChanged() {
        picker?.requestLayout()
    }
}