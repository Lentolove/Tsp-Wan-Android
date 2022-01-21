package com.android.blogandroid.ui.common

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   : 收藏仓库
 *     version: 1.0
 */
class CollectRepository {

    suspend fun collect(id:Long) = RetrofitClient.apiService.collect(id)
    suspend fun unCollect(id: Long) = RetrofitClient.apiService.uncollect(id)
}