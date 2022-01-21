package com.android.blogandroid.model.bean

import androidx.annotation.Keep

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   : 热词
 *     version: 1.0
 */
/**
 * {
"id": 6,
"link": "",
"name": "面试",
"order": 1,
"visible": 1
},
 */
@Keep
data class HotWord(
    val id: Int,
    val link: String,
    val order: Int,
    val name: String,
    val visible: Int
)