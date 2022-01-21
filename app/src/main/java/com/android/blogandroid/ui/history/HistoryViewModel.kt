package com.android.blogandroid.ui.history

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.model.bean.Article

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
class HistoryViewModel : BaseViewModel() {

    private val historyRepository by lazy { HistoryRepository() }

    val articleList = MutableLiveData<MutableList<Article>>()
    val emptyStatus = MutableLiveData<Boolean>()

    fun getData() {
        launch(
                block = {
                    emptyStatus.value = false
                    val readHistory = historyRepository.getReadHistory()
                    //是否收藏 todo

                    articleList.value = readHistory.toMutableList()
                    emptyStatus.value = readHistory.isEmpty()
                }
        )
    }


    fun deleteHistory(article: Article) {
        launch(block = { historyRepository.deleteHistory(article) })
    }


}