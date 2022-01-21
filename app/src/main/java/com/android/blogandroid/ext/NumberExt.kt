package com.android.blogandroid.ext

import com.android.blogandroid.util.density
import com.android.blogandroid.util.scaledDensity

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 像素转换工具
 *     version: 1.0
 */

/**
 * dp、sp、px相互换算
 */
internal fun Number?.dpToPx() = (this?.toFloat() ?: 0f) * density
internal fun Number?.spToPx() = (this?.toFloat() ?: 0f) * scaledDensity
internal fun Number?.pxToDp() = (this?.toFloat() ?: 0f) / density
internal fun Number?.pxToSp() = (this?.toFloat() ?: 0f) / scaledDensity