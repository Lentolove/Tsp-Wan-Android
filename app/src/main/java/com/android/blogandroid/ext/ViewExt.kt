package com.android.blogandroid.ext

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 *     author : shengping.tian
 *     time   : 2020/12/17
 *     desc   : 软键盘管理
 *     version: 1.0
 */

/**
 * 弹出软键盘
 */
fun View.showSoftInput() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * 隐藏软键盘
 */
fun View.hideSoftInput() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}