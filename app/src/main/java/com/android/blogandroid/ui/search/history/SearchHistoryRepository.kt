package com.android.blogandroid.ui.search.history

import com.android.blogandroid.model.api.RetrofitClient
import com.android.blogandroid.model.store.SearchHistoryStore

/**
 *     author : shengping.tian
 *     time   : 2020/12/17
 *     desc   : 搜索历史和热词仓库
 *     version: 1.0
 */
class SearchHistoryRepository {

    suspend fun getHotSearch() = RetrofitClient.apiService.getHotWords().apiData()

    fun saveSearchHistory(searchWord: String) {
        SearchHistoryStore.saveSearchHistory(searchWord)
    }

    fun deleteSearchHistory(searchWord: String) {
        SearchHistoryStore.deleteSearchHistory(searchWord)
    }

    fun getSearchHistory() = SearchHistoryStore.getSearchHistory()

}