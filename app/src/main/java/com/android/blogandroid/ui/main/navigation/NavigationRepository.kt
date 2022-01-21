package com.android.blogandroid.ui.main.navigation

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
class NavigationRepository {

    suspend fun getNavigation() = RetrofitClient.apiService.getNavigation().apiData()
}