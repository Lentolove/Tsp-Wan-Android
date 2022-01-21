package com.android.blogandroid.ui.search.history

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.model.bean.HotWord

/**
 *     author : shengping.tian
 *     time   : 2020/12/17
 *     desc   :
 *     version: 1.0
 */
class SearchHistoryViewModel : BaseViewModel() {


    private val searchHistoryRepository by lazy { SearchHistoryRepository() }

    val hotWords = MutableLiveData<List<HotWord>>()
    val searchHistory = MutableLiveData<MutableList<String>>()

    fun getHotSearch() {
        launch(block = { hotWords.value = searchHistoryRepository.getHotSearch() })
    }

    fun getSearchHistory() {
        searchHistory.value = searchHistoryRepository.getSearchHistory()
    }

    fun addSearchHistory(word: String) {
        val history = searchHistory.value ?: mutableListOf()
        if (history.contains(word)) {
            history.remove(word)
        }
        history.add(0, word)
        searchHistory.value = history
        searchHistoryRepository.saveSearchHistory(word)
    }

    fun deleteSearchHistory(searchWords: String) {
        val history = searchHistory.value ?: mutableListOf()
        if (history.contains(searchWords)) {
            history.remove(searchWords)
            searchHistory.value = history
            searchHistoryRepository.deleteSearchHistory(searchWords)
        }
    }

}