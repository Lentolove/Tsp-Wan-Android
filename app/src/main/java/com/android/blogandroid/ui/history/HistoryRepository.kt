package com.android.blogandroid.ui.history

import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.room.RoomHelper

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
class HistoryRepository {

    suspend fun getReadHistory() = RoomHelper.queryAllReadHistory()

    suspend fun deleteHistory(article: Article) = RoomHelper.deleteReadHistory(article)
}