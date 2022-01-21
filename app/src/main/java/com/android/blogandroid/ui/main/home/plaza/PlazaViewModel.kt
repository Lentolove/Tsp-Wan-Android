package com.android.blogandroid.ui.main.home.plaza

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseCollectViewModel
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.common.loadmore.LoadMoreStatus
import com.android.blogandroid.ext.concat
import com.android.blogandroid.model.bean.Article

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 广场界面ViewModel
 *     version: 1.0
 */
class PlazaViewModel : BaseCollectViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private val plazaRepository by lazy { PlazaRepository() }

    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE

    fun refreshArticleList() {
        launch(
                block = {
                    refreshStatus.value = true
                    reloadStatus.value = false
                    val pagination = plazaRepository.getUserArticleList(INITIAL_PAGE)
                    page = pagination.curPage
                    articleList.value = pagination.datas
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
                    val pagination = plazaRepository.getUserArticleList(page)
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