package com.android.blogandroid.ui.main.system

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.model.bean.Category

/**
 *     author : shengping.tian
 *     time   : 2020/12/15
 *     desc   :
 *     version: 1.0
 */
class SystemViewModel : BaseViewModel() {

    private val systemRepository by lazy { SystemRepository() }
    val categories: MutableLiveData<MutableList<Category>> = MutableLiveData()
    val loadingStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getArticleCategory() {
        launch(
            block = {
                loadingStatus.value = true
                reloadStatus.value = false
                categories.value = systemRepository.getArticleCategories()
                loadingStatus.value = false
            },
            error = {
                loadingStatus.value = false
                reloadStatus.value = true
            }
        )
    }

}