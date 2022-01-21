package com.android.blogandroid.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.bean.Tag

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
@Database(entities = [Article::class, Tag::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readHistoryDao(): ReadHistoryDao
}