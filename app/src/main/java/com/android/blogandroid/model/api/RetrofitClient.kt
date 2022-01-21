package com.android.blogandroid.model.api

import com.android.blogandroid.App
import com.android.blogandroid.common.core.Logger
import com.android.blogandroid.common.core.MoshiHelper
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : Retrofit网络请求工具
 *     version: 1.0
 */
object RetrofitClient {

    /**
     * OkHttp3中，对cookie而言，新增了两个类Cookiejar、Cookie两个类. 在OkHttpClient创建时，传入这个CookieJar的实现，就能完成对Cookie的自动管理
     *  https://www.jianshu.com/p/2e474f769569
     */
    private val cookiePersistor = SharedPrefsCookiePersistor(App.instance)
    private val cookieJar = PersistentCookieJar(SetCookieCache(), cookiePersistor)

    /** retrofit log 日志*/
    private val logger = HttpLoggingInterceptor.Logger.DEFAULT
    private val loggingInterceptor = HttpLoggingInterceptor(logger).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**okHttpClient*/
    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .cookieJar(cookieJar)
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(MoshiHelper.moshi))
        .build()

    /**ApiService */
    val apiService: ApiService = retrofit.create(ApiService::class.java)

    /**清除cookie*/
    fun clearCookie() = cookieJar.clear()

    /**检查是否有cookie*/
    fun hasCookie() = cookiePersistor.loadAll().isNotEmpty()
}