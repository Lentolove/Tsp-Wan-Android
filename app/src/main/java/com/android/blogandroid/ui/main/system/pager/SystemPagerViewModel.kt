package com.android.blogandroid.ui.main.system.pager

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseCollectViewModel
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.common.loadmore.LoadMoreStatus
import com.android.blogandroid.ext.concat
import com.android.blogandroid.model.bean.Article
import kotlinx.coroutines.Job

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   :
 *     version: 1.0
 */
class SystemPagerViewModel : BaseCollectViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private val systemPagerRepository by lazy { SystemPagerRepository() }
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE
    private var id: Int = -1
    private var refreshJob: Job? = null

    fun refreshArticleList(cid: Int) {
        if (cid != id) {
            //当前请求的数据id 更新
            cancelJob(refreshJob)
            id = cid
            articleList.value = mutableListOf()
        }
        refreshJob = launch(
                block = {
                    refreshStatus.value = true
                    reloadStatus.value = false
                    val pagination = systemPagerRepository.getArticleListByCid(INITIAL_PAGE, cid)
                    page = pagination.curPage
                    articleList.value = pagination.datas
                    refreshStatus.value = false
                },
                error = {
                    refreshStatus.value = false
                    reloadStatus.value = articleList.value?.isEmpty()
                }
        )
    }

    fun loadMoreArticleList(cid: Int) {
        launch(
                block = {
                    loadMoreStatus.value = LoadMoreStatus.LOADING
                    val pagination = systemPagerRepository.getArticleListByCid(page, cid)
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