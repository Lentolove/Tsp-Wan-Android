package com.android.blogandroid.ui.main.system.pager

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   :
 *     version: 1.0
 */
class SystemPagerRepository {

    suspend fun getArticleListByCid(page: Int, cid: Int) =
            RetrofitClient.apiService.getArticleListByCid(page, cid).apiData()

}