package com.android.blogandroid.model.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   :
 *     version: 1.0
 */
/**
"data": [
{
"children": [],
"courseId": 13,
"id": 294,
"name": "完整项目",
"order": 145000,
"parentChapterId": 293,
"userControlSetTop": false,
"visible": 0
},
...]
"errorCode": 0,
"errorMsg": ""
 */

@Keep
@Parcelize
data class Category(
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int,
        val children: MutableList<Category>
) : Parcelable