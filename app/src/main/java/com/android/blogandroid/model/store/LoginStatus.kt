package com.android.blogandroid.model.store

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   : 从sp中查询是否已经登录
 *     version: 1.0
 */

fun isLogin() = UserInfoStore.getUserInfo() != null && RetrofitClient.hasCookie()