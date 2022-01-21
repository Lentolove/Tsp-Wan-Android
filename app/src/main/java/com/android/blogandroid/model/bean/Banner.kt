package com.android.blogandroid.model.bean

import androidx.annotation.Keep

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   : banner数据
 *     version: 1.0
 */

/**
 *  {
"desc": "享学~",
"id": 29,
"imagePath": "https://wanandroid.com/blogimgs/45251caf-79aa-4a35-89e1-99a5454a21aa.png",
"isVisible": 1,
"order": 0,
"title": "让一堆Android开发栽倒的，腾讯算法面试题，到底问了你什么？",
"type": 0,
"url": "https://www.bilibili.com/video/BV1Fy4y1B7hF"
},
 */
@Keep
data class Banner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)