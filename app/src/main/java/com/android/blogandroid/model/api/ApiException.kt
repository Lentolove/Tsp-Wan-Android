package com.android.blogandroid.model.api

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : 网络请求异常类
 *     version: 1.0
 */
class ApiException(var code: Int, override var message: String) : RuntimeException()