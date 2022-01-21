package com.android.blogandroid.model.bean

import androidx.annotation.Keep

/**
 *     author : shengping.tian
 *     time   : 2020/12/18
 *     desc   : 用户信息 bean
 *     version: 1.0
 */

/**
 *  "data": {
"admin": false,
"chapterTops": [],
"coinCount": 0,
"collectIds": [
16431
],
"email": "",
"icon": "",
"id": 83439,
"nickname": "13257214800",
"password": "",
"publicName": "13257214800",
"token": "",
"type": 0,
"username": "13257214800"
},
"errorCode": 0,
"errorMsg": ""
 */

@Keep
data class UserInfo(
        val admin: Boolean,
        val email: String,
        val icon: String,
        val id: Int,
        val nickname: String,
        val password: String,
        val publicName: String,
        val token: String,
        val type: Int,
        val username: String,
        val collectIds: MutableList<Long>,
        val chapterTops: MutableList<Any>
)