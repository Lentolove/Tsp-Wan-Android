package com.android.blogandroid.model.api

import androidx.annotation.Keep

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   :
 *     version: 1.0
 */
@Keep
data class ApiResult<T>(
    val errorCode: Int,
    val errorMsg: String,
    private val data: T?
) {
    fun apiData(): T {
        if (errorCode == 0 && data != null) {
            return data
        } else {
            throw ApiException(errorCode, errorMsg)
        }
    }
}