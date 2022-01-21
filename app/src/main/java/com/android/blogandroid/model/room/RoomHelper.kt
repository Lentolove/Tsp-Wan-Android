package com.android.blogandroid.model.room

import androidx.room.Room
import com.android.blogandroid.App
import com.android.blogandroid.model.bean.Article

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
object RoomHelper {

    private const val DATABASE_NAME = "android_db"

    private val appDatabase by lazy {
        Room.databaseBuilder(App.instance, AppDatabase::class.java, DATABASE_NAME).build()
    }

    private val readHistoryDao by lazy { appDatabase.readHistoryDao() }

    suspend fun queryAllReadHistory(): List<Article> {
        return readHistoryDao.queryAllReadHistory().map {
            it.article.apply {
                tags = it.tags
            }
        }
    }

    suspend fun addReadHistory(article: Article) {
        article.readTime = System.currentTimeMillis()
       readHistoryDao.insertArticle(article)
        article.tags.forEach {
            readHistoryDao.insertTag(it.apply {
                it.articleId = article.id
            })
        }
    }

    suspend fun deleteReadHistory(article: Article) {
        readHistoryDao.queryReadHistory(article.id)?.let {
            readHistoryDao.deleteArticle(it.article)
            it.tags.forEach { tag ->
                readHistoryDao.deleteTag(tag)
            }
        }
    }

}