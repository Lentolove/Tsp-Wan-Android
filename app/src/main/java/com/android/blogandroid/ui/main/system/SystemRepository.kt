package com.android.blogandroid.ui.main.system

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/15
 *     desc   :
 *     version: 1.0
 */
class SystemRepository {

    suspend fun getArticleCategories() = RetrofitClient.apiService.getArticleCategories().apiData()

}