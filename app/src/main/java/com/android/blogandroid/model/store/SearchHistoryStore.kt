package com.android.blogandroid.model.store

import com.android.blogandroid.App
import com.android.blogandroid.common.core.MoshiHelper
import com.android.blogandroid.common.core.getSpValue
import com.android.blogandroid.common.core.putSpValue

/**
 *     author : shengping.tian
 *     time   : 2020/12/17
 *     desc   : 搜索历史  SharedPreferences 保存
 *     version: 1.0
 */
object SearchHistoryStore {

    private const val SP_SEARCH_HISTORY = "sp_search_history"
    private const val KEY_SEARCH_HISTORY = "searchHistory"

    /**
     * 保存搜索的关键词
     */
    fun saveSearchHistory(word: String) {
        val history = getSearchHistory()
        if (history.contains(word)) {
            //有的话就删除，每次保持最近的搜索在第一个位置
            history.remove(word)
        }
        history.add(0, word)
        val lisStr = MoshiHelper.toJson(history)
        putSpValue(SP_SEARCH_HISTORY, App.instance, KEY_SEARCH_HISTORY, lisStr)
    }

    /**
     * 删除搜索历史
     */
    fun deleteSearchHistory(word: String){
        val history = getSearchHistory()
        history.remove(word)
        val listStr = MoshiHelper.toJson(history)
        putSpValue(SP_SEARCH_HISTORY, App.instance, KEY_SEARCH_HISTORY, listStr)
    }


    /**
     * 获取所有保存的搜索地址
     */
    fun getSearchHistory(): MutableList<String> {
        val listStr = getSpValue(SP_SEARCH_HISTORY, App.instance, KEY_SEARCH_HISTORY, "")
        return if (listStr.isEmpty()) {
            mutableListOf()
        } else {
            //todo 记录文档 将sp类容转成list
            MoshiHelper.fromJson<MutableList<String>>(
                    json = listStr,
                    type = (object : MoshiHelper.TypeToken<MutableList<String>>() {}).type
            ) ?: mutableListOf()
        }
    }

}