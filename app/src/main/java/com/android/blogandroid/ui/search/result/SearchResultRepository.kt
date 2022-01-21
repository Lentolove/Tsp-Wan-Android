package com.android.blogandroid.ui.search.result

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/17
 *     desc   :
 *     version: 1.0
 */
class SearchResultRepository {

    suspend fun search(keyword: String, page: Int) = RetrofitClient.apiService.search(keyword, page).apiData()

}