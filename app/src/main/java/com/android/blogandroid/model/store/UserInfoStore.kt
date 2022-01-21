package com.android.blogandroid.model.store

import com.android.blogandroid.App
import com.android.blogandroid.common.core.MoshiHelper
import com.android.blogandroid.common.core.clearSpValue
import com.android.blogandroid.common.core.getSpValue
import com.android.blogandroid.common.core.putSpValue
import com.android.blogandroid.model.bean.UserInfo

/**
 *     author : shengping.tian
 *     time   : 2020/12/18
 *     desc   : 用户信息保存
 *     version: 1.0
 */
object UserInfoStore {

    private const val SP_USER_INFO = "sp_user_info"
    private const val KEY_USER_INFO = "userInfo"

    /**
     * 获取保存的用户信息
     */
    fun getUserInfo(): UserInfo? {
        val userInfo = getSpValue(SP_USER_INFO, App.instance, KEY_USER_INFO, "")
        return if (userInfo.isNotEmpty()) {
            MoshiHelper.fromJson<UserInfo>(userInfo)
        } else {
            null
        }
    }

    /**
     * 保存用户信息
     */
    fun setUserInfo(userInfo: UserInfo) = putSpValue(SP_USER_INFO, App.instance, KEY_USER_INFO, MoshiHelper.toJson(userInfo))

    /**
     * 清除用户信息
     */
    fun clearUserInfo() {
        clearSpValue(SP_USER_INFO, App.instance)
    }

    /**
     * 清楚userInfo中收藏文章的id
     */
    fun removeCollectId(collectId: Long) {
        getUserInfo()?.let {
            if (collectId in it.collectIds) {
                it.collectIds.remove(collectId)
            }
        }
    }

    fun addCollectId(collectId: Long) {
        getUserInfo()?.let {
            if (collectId !in it.collectIds) {
                it.collectIds.add(collectId)
                setUserInfo(it)
            }
        }
    }

}