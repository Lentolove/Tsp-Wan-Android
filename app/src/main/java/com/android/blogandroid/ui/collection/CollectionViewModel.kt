package com.android.blogandroid.ui.collection

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseCollectViewModel
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_COLLECT_UPDATED
import com.android.blogandroid.common.loadmore.LoadMoreStatus
import com.android.blogandroid.ext.concat
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.store.UserInfoStore
import com.android.blogandroid.ui.common.CollectRepository


/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   :
 *     version: 1.0
 */
class CollectionViewModel : BaseCollectViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private val collectionRepository by lazy { CollectionRepository() }


    val refreshStatus = MutableLiveData<Boolean>()

    val loadStatus = MutableLiveData<LoadMoreStatus>()

    val reloadStatus = MutableLiveData<Boolean>()

    val emptyStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE

    fun refresh() {
        launch(
                block = {
                    refreshStatus.value = true
                    emptyStatus.value = false
                    reloadStatus.value = false
                    val pagination = collectionRepository.getCollectionList(INITIAL_PAGE)
                    pagination.datas.forEach { it.collect = true }
                    page = pagination.curPage
                    articleList.value = pagination.datas
                    emptyStatus.value = pagination.datas.isEmpty()
                    refreshStatus.value = false
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
                    loadStatus.value = LoadMoreStatus.LOADING

                    val pagination = collectionRepository.getCollectionList(page)
                    pagination.datas.forEach { it.collect = true }
                    page = pagination.curPage
                    articleList.value = articleList.value.concat(pagination.datas)

                    loadStatus.value = if (pagination.offset >= pagination.total) {
                        LoadMoreStatus.END
                    } else {
                        LoadMoreStatus.COMPLETED
                    }
                },
                error = {
                    loadStatus.value = LoadMoreStatus.ERROR
                }
        )
    }



}