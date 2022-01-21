package com.android.blogandroid.ui.setting

import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.android.blogandroid.model.api.RetrofitClient
import com.android.blogandroid.model.store.UserInfoStore

/**
 *     author : shengping.tian
 *     time   : 2020/12/22
 *     desc   :
 *     version: 1.0
 */
class SettingViewModel:BaseViewModel() {

    /**
     * 退出登录
     */
    fun logout(){
        UserInfoStore.clearUserInfo()
        RetrofitClient.clearCookie()
        Bus.post(USER_LOGIN_STATE_CHANGED,false)
    }
}