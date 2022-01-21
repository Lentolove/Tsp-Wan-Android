package com.android.blogandroid.ui.main.home.wechat

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 公众号数据
 *     version: 1.0
 */
class WeChatRepository {

    suspend fun getWeChatCategories() = RetrofitClient.apiService.getWeChatCategory().apiData()

    suspend fun getWeChatArticleList(page: Int, id: Int) = RetrofitClient.apiService.getWeChatArticleList(page, id).apiData()

}