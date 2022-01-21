package com.android.blogandroid.ui.main.home.popular

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseCollectViewModel
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.common.loadmore.LoadMoreStatus
import com.android.blogandroid.ext.concat
import com.android.blogandroid.model.bean.Article

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   :
 *     version: 1.0
 */

class PopularViewModel : BaseCollectViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private val popularRepository by lazy { PopularRepository() }

    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE


    fun refreshArticleList() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val topArticleListDeffer = async {
                    popularRepository.getTopArticleList()
                }
                val paginationDeffer = async {
                    popularRepository.getArticleList(INITIAL_PAGE)
                }
                //等待数据加载
                val topArtList = topArticleListDeffer.await().apply {
                    forEach { it.top = true }
                }
                val pagination = paginationDeffer.await()
                articleList.value = (topArtList + pagination.datas).toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }


    fun loadMoreArticleList() {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING
                val pagination = popularRepository.getArticleList(page)
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