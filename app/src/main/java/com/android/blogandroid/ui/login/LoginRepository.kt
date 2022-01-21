package com.android.blogandroid.ui.login

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   :
 *     version: 1.0
 */
class LoginRepository {

    suspend fun login(username:String,password:String) = RetrofitClient.apiService.login(username,password).apiData()
}