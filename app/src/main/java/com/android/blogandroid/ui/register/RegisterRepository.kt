package com.android.blogandroid.ui.register

import com.android.blogandroid.model.api.RetrofitClient

/**
 *     author : shengping.tian
 *     time   : 2020/12/18
 *     desc   :
 *     version: 1.0
 */
class RegisterRepository {

    suspend fun register(username: String, password: String, rePassword: String) = RetrofitClient.apiService.register(username,password,rePassword).apiData()

}