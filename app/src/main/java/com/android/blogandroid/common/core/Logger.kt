package com.android.blogandroid.common.core

import android.util.Log
import com.android.blogandroid.BuildConfig

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : 日志工具类
 *     version: 1.0
 */
object Logger {

    private const val defaultTag = "Logger"
    private const val nullStr = "__NULL__"

    @JvmStatic
    @JvmOverloads
    fun d(tag: String? = null, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(tag ?: defaultTag, msg ?: nullStr)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun i(tag: String? = null, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.i(tag ?: defaultTag, msg ?: nullStr)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun e(tag: String? = null, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.e(tag ?: defaultTag, msg ?: nullStr)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun w(tag: String? = null, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.w(tag ?: defaultTag, msg ?: nullStr)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun v(tag: String? = null, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.v(tag ?: defaultTag, msg ?: nullStr)
        }
    }

}