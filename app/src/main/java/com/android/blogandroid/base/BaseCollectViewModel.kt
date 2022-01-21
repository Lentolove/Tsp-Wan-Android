package com.android.blogandroid.base

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_COLLECT_UPDATED
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.store.UserInfoStore
import com.android.blogandroid.model.store.isLogin
import com.android.blogandroid.ui.common.CollectRepository

/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   :
 *     version: 1.0
 */
open class BaseCollectViewModel:BaseViewModel() {

    private val collectRepository by lazy { CollectRepository() }

    val articleList: MutableLiveData<MutableList<Article>> = MutableLiveData()

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