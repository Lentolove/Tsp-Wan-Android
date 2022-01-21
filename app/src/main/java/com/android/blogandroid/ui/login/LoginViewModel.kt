package com.android.blogandroid.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.android.blogandroid.model.store.UserInfoStore

/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   : 登录界面ViewModel
 *     version: 1.0
 */
class LoginViewModel : BaseViewModel() {

    private val loginRepository by lazy { LoginRepository() }
    val submitting = MutableLiveData<Boolean>()
    val loginResult = MutableLiveData<Boolean>()

    fun login(account: String, password: String) {
        launch(
                block = {
                    submitting.value = true
                    //只有登录成功才会返回，登录错误则走ApiException
                    val userInfo = loginRepository.login(account, password)
                    UserInfoStore.setUserInfo(userInfo)
                    //通知全局登录状态改变
                    Bus.post(USER_LOGIN_STATE_CHANGED, true)
                    submitting.value = false
                    loginResult.value = true
                },
                error = {
                    submitting.value = false
                    loginResult.value = false
                }
        )
    }

}