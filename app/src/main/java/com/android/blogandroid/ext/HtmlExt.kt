package com.android.blogandroid.ext

import androidx.core.text.HtmlCompat

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 将String 转成富文本
 *     version: 1.0
 */

fun String?.htmlToSpanned() = if (this.isNullOrEmpty()) "" else HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)