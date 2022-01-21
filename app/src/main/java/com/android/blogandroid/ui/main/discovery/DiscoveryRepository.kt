package com.android.blogandroid.ui.main.discovery

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   : 发现仓库
 *     version: 1.0
 */
class DiscoveryRepository {

    suspend fun getBanners() = RetrofitClient.apiService.getBanners().apiData()

    suspend fun getHotWords() = RetrofitClient.apiService.getHotWords().apiData()

    suspend fun getFrequentlyWebsites() =
        RetrofitClient.apiService.getFrequentlyWebsites().apiData()

}