package com.android.blogandroid.ui.collection

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   :
 *     version: 1.0
 */
class CollectionRepository {

    suspend fun getCollectionList(page:Int) = RetrofitClient.apiService.getCollectionList(page).apiData()

}