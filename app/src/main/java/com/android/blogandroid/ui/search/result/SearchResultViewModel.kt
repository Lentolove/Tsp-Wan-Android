package com.android.blogandroid.ui.search.result

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseCollectViewModel
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.common.loadmore.LoadMoreStatus
import com.android.blogandroid.ext.concat
import com.android.blogandroid.model.bean.Article

/**
 *     author : shengping.tian
 *     time   : 2020/12/17
 *     desc   :
 *     version: 1.0
 */
class SearchResultViewModel : BaseCollectViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private val searchResultRepository by lazy { SearchResultRepository() }
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()
    val emptyStatus = MutableLiveData<Boolean>()

    private var currentKeywords = ""
    private var page = INITIAL_PAGE


    fun search(word: String = currentKeywords) {
        launch(
                block = {
                    if (currentKeywords != word) {
                        currentKeywords = word
                        articleList.value = emptyList<Article>().toMutableList()
                    }
                    refreshStatus.value = true
                    reloadStatus.value = false
                    emptyStatus.value = false
                    val pagination = searchResultRepository.search(word, INITIAL_PAGE)
                    page = pagination.curPage
                    articleList.value = pagination.datas
                    refreshStatus.value = false
                    emptyStatus.value = pagination.datas.isEmpty()
                },
                error = {
                    refreshStatus.value = false
                    reloadStatus.value = page == INITIAL_PAGE
                }
        )
    }

    fun loadMore() {
        launch(
                block = {
                    loadMoreStatus.value = LoadMoreStatus.LOADING
                    val pagination = searchResultRepository.search(currentKeywords, page)
                    page = pagination.curPage
                    articleList.value = articleList.value.concat(pagination.datas)
                    loadMoreStatus.value = if (pagination.offset >= pagination.total) {
                        LoadMoreStatus.END
                    } else {
                        LoadMoreStatus.COMPLETED
                    }
                },
                error = {
                    loadMoreStatus.value = LoadMoreStatus.ERROR
                }
        )
    }

}