package com.android.blogandroid.ext

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   :Collection集合转换为ArrayList，以方便序列化
 *     version: 1.0
 */

/**
 * Collection集合转换为ArrayList，以方便序列化
 */
fun <T> Collection<T>.toArrayList() = ArrayList<T>(this)




/**
 * 当前列表与给定的数据连接，返回信息的列表
 * @param [data] 给定的列表
 * @return 连接后的列表
 */
fun <T> MutableList<T>?.concat(data: Collection<T>): MutableList<T> {
    val currentList = this?.toList() ?: emptyList()
    val newList = currentList + data
    return newList.toMutableList()
}