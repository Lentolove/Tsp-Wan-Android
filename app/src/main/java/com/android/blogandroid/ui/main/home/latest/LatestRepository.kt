package com.android.blogandroid.ui.main.home.latest

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 获取最新Fragment
 *     version: 1.0
 */
class LatestRepository {
    suspend fun getProjectList(page:Int) = RetrofitClient.apiService.getProjectList(page).apiData()
}