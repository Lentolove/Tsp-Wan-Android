package com.android.blogandroid.model.room

import androidx.room.*
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.bean.Tag

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */

@Dao
interface ReadHistoryDao {

    @Transaction
    @Insert(entity = Article::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article): Long

    @Transaction
    @Insert(entity = Tag::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag): Long

    @Transaction
    @Query("SELECT * FROM article ORDER BY readTime DESC")
    suspend fun queryAllReadHistory(): List<ReadHistory>

    @Transaction
    @Query("SELECT * FROM article WHERE id = :id")
    suspend fun queryReadHistory(id: Long): ReadHistory?

    @Transaction
    @Query("SELECT * FROM tag WHERE articleId = :articleId")
    suspend fun queryAllTags(articleId:Long):List<Tag>

    @Transaction
    @Delete(entity = Article::class)
    suspend fun deleteArticle(article: Article)

    @Transaction
    @Delete(entity = Tag::class)
    suspend fun deleteTag(tag: Tag)
}