package com.android.blogandroid.model.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.bean.Tag

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   : 阅读历史
 *     version: 1.0
 */
@Entity
data class ReadHistory(
    @Embedded
    var article: Article,
    @Relation(
        parentColumn = "id",
        entityColumn = "articleId"
    )
    var tags: MutableList<Tag>
)