package com.android.blogandroid.model.bean

import androidx.annotation.Keep

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
/**
 *  {
"category": "源码",
"icon": "",
"id": 22,
"link": "https://www.androidos.net.cn/sourcecode",
"name": "androidos",
"order": 11,
"visible": 1
},
 */
@Keep
data class Frequently(
    val icon: String,
    val id: Int,
    val name: String,
    val link: String,
    val order: Int,
    val visible: Int
)