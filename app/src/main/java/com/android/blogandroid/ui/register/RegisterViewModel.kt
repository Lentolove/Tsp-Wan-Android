package com.android.blogandroid.ui.register

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.android.blogandroid.model.store.UserInfoStore

/**
 *     author : shengping.tian
 *     time   : 2020/12/18
 *     desc   :
 *     version: 1.0
 */
class RegisterViewModel : BaseViewModel() {


    private val registerRepository by lazy { RegisterRepository() }

    val submitting = MutableLiveData<Boolean>()

    val registerResult = MutableLiveData<Boolean>()

    fun register(account: String, password: String, rePassword: String) {
        launch(
                block = {
                    submitting.value = true
                    val userInfo = registerRepository.register(account, password, rePassword)
                    //保存用于信息
                    UserInfoStore.setUserInfo(userInfo)
                    // 全局监听用户登录信息改变,liveEventBus发送事件
                    Bus.post(USER_LOGIN_STATE_CHANGED, true)
                    registerResult.value = true
                    submitting.value = false
                },
                error = {
                    registerResult.value = false
                    submitting.value = false
                }
        )
    }

}