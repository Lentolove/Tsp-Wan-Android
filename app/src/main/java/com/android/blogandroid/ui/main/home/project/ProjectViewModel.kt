package com.android.blogandroid.ui.main.home.project

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseCollectViewModel
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.common.loadmore.LoadMoreStatus
import com.android.blogandroid.ext.concat
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.bean.Category

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 项目列表 viewModel
 *     version: 1.0
 */
class ProjectViewModel : BaseCollectViewModel() {

    companion object {
        const val INITIAL_CHECKED = 0
        const val INITIAL_PAGE = 1
    }

    private val projectRepository by lazy { ProjectRepository() }

    val categories: MutableLiveData<MutableList<Category>> = MutableLiveData()
    val checkedCategory: MutableLiveData<Int> = MutableLiveData()

    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()
    val reloadListStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE + 1

    fun getProjectCategory() {
        launch(
                block = {
                    refreshStatus.value = true
                    reloadStatus.value = false
                    val categoryList = projectRepository.getProjectCategories()
                    val checkedPosition = INITIAL_CHECKED
                    val cid = categoryList[checkedPosition].id
                    val pagination = projectRepository.getProjectListById(INITIAL_PAGE, cid)
                    page = pagination.curPage
                    categories.value = categoryList

                    checkedCategory.value = checkedPosition
                    articleList.value = pagination.datas
                    refreshStatus.value = false
                },
                error = {
                    refreshStatus.value = false
                    reloadStatus.value = true
                }
        )
    }

    fun refreshProjectList(checkedPosition: Int = checkedCategory.value ?: INITIAL_CHECKED) {
        launch(
                block = {
                    refreshStatus.value = true
                    reloadStatus.value = false

                    if (checkedPosition != checkedCategory.value) {
                        articleList.value = mutableListOf()
                        checkedCategory.value = checkedPosition
                    }
                    val categoryList = categories.value ?: return@launch
                    val cid = categoryList[checkedPosition].id
                    val pagination = projectRepository.getProjectListById(INITIAL_PAGE, cid)
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

    fun loadMoreProjectList() {
        launch(
                block = {
                    loadMoreStatus.value = LoadMoreStatus.LOADING
                    val categoryList = categories.value ?: return@launch
                    val checkedPosition = checkedCategory.value ?: return@launch
                    val cid = categoryList[checkedPosition].id
                    val pagination = projectRepository.getProjectListById(page + 1, cid)
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