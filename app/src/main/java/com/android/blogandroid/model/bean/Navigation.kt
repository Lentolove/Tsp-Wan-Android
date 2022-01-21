package com.android.blogandroid.model.bean

import androidx.annotation.Keep

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   : 导航 bean https://www.wanandroid.com/navi/json
 *     version: 1.0
 */
@Keep
data class Navigation(
    val cid: Int,
    val name: String,
    val articles: MutableList<Article>
)