package com.android.blogandroid.ui.main.home.latest

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_COLLECT_UPDATED
import com.android.blogandroid.common.loadmore.LoadMoreStatus
import com.android.blogandroid.ext.concat
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.store.UserInfoStore
import com.android.blogandroid.model.store.isLogin
import com.android.blogandroid.ui.common.CollectRepository

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 最新列表数据
 *     version: 1.0
 */
class LatestViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private val latestRepository by lazy { LatestRepository() }

    //数据列表
    val articleList = MutableLiveData<MutableList<Article>>(mutableListOf())

    private val collectRepository by lazy { CollectRepository() }


    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE

    fun refreshProjectList() {
        launch(
                block = {
                    refreshStatus.value = true
                    reloadStatus.value = false
                    val pagination = latestRepository.getProjectList(INITIAL_PAGE)
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

    fun loadMoreProjectList() {
        launch(
                block = {
                    loadMoreStatus.value = LoadMoreStatus.LOADING
                    val pagination = latestRepository.getProjectList(page)
                    page = pagination.curPage
                    articleList.value = articleList.value.concat(pagination.datas)
                    loadMoreStatus.value = if (pagination.offset >= pagination.total){
                        LoadMoreStatus.END
                    }else{
                        LoadMoreStatus.COMPLETED
                    }
                },
                error = {
                    loadMoreStatus.value = LoadMoreStatus.ERROR
                }
        )
    }


    fun collect(id: Long) {
        launch(
                block = {
                    collectRepository.collect(id)
                    UserInfoStore.addCollectId(id)
                    updateItemCollectState(id to true)
                    Bus.post(USER_COLLECT_UPDATED, id to true)
                },
                error = {
                    updateItemCollectState(id to false)
                }
        )
    }

    fun unCollect(id: Long) {
        launch(
                block = {
                    collectRepository.unCollect(id)
                    UserInfoStore.removeCollectId(id)
                    updateItemCollectState(id to false)
                    Bus.post(USER_COLLECT_UPDATED, id to false)
                },
                error = {
                    updateItemCollectState(id to true)
                }
        )
    }

    /**
     * 更新列表收藏状态
     */
    fun updateListCollectState() {
        val list = articleList.value
        if (list.isNullOrEmpty()) return
        if (isLogin()) {
            val collectIds = UserInfoStore.getUserInfo()?.collectIds ?: return
            list.forEach { it.collect = collectIds.contains(it.id) }
        } else {
            list.forEach { it.collect = false }
        }
        articleList.value = list
    }

    /**
     * 更新Item的收藏状态
     */
    fun updateItemCollectState(target: Pair<Long, Boolean>) {
        val list = articleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        articleList.value = list
    }

}