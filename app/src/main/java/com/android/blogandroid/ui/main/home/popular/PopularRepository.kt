package com.android.blogandroid.ui.main.home.popular

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : 获取 Popular 数据 仓库
 *     version: 1.0
 */
class PopularRepository {

    //获取列表
    suspend fun getTopArticleList() = RetrofitClient.apiService.getTopArticleList().apiData()

    suspend fun getArticleList(page:Int) = RetrofitClient.apiService.getArticleList(page).apiData()

}