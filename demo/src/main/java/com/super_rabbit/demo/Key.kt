package com.super_rabbit.demo

/**
 * Created by wanglu on 4/7/18.
 */
class Key(val value: Int) {
    companion object {
        @JvmField
        val COMPARATOR: Comparator<Key> = compareBy<Key> { it.value }
    }
}