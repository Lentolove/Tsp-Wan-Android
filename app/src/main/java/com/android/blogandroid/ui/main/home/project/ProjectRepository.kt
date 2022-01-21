package com.android.blogandroid.ui.main.home.project

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 项目 模块
 *     version: 1.0
 */
class ProjectRepository {

    suspend fun getProjectCategories() = RetrofitClient.apiService.getProjectCategories().apiData()

    suspend fun getProjectListById(page: Int, cid: Int) = RetrofitClient.apiService.getProjectListById(page, cid).apiData()

}