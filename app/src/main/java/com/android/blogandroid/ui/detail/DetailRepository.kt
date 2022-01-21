package com.android.blogandroid.ui.detail

import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.room.RoomHelper

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
class DetailRepository {

    suspend fun saveReadHistory(article: Article) = RoomHelper.addReadHistory(article)
}