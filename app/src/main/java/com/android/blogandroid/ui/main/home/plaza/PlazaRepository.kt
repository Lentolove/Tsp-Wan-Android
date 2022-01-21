package com.android.blogandroid.ui.main.home.plaza

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 广场界面数据仓库
 *     version: 1.0
 */
class PlazaRepository {
    suspend fun getUserArticleList(page: Int) = RetrofitClient.apiService.getUserArticleList(page).apiData()
}