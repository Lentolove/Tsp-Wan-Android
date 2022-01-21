package com.android.blogandroid.ui.detail

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.store.UserInfoStore
import com.android.blogandroid.model.store.isLogin
import com.android.blogandroid.ui.common.CollectRepository

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   : 详情界面
 *     version: 1.0
 */
class DetailViewModel : BaseViewModel() {

    private val detailRepository by lazy { DetailRepository() }

    private val collectRepository by lazy { CollectRepository() }


    val collect = MutableLiveData<Boolean>()


    fun collect(id: Long) {
        launch(
                block = {
                    collectRepository.collect(id)
                    // 收藏成功，更新userInfo
                    UserInfoStore.addCollectId(id)
                    collect.value = true
                },
                error = {
                    collect.value = false
                }
        )
    }

    fun unCollect(id: Long) {
        launch(
                block = {
                    collectRepository.unCollect(id)
                    // 取消收藏成功，更新userInfo
                    UserInfoStore.removeCollectId(id)
                    collect.value = false
                },
                error = {
                    collect.value = true
                }
        )
    }


    fun updateCollectStatus(id: Long) {
        collect.value = if (isLogin()) {
            UserInfoStore.getUserInfo()?.collectIds?.contains(id)
        } else {
            false
        }
    }

    fun saveReadHistory(article: Article) {
        launch(block = { detailRepository.saveReadHistory(article) })
    }

}