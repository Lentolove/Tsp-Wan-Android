package com.android.blogandroid

import android.app.Application
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.common.core.CoilHelper
import com.android.blogandroid.common.core.DayNightHelper
import com.android.blogandroid.common.loadmore.LoadMoreHelper
import com.android.blogandroid.util.isMainProcess

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   :
 *     version: 1.0
 */
class App : Application() {

    //1.单例
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (isMainProcess(this)) {
            init()
        }
    }

    private fun init() {
        LoadMoreHelper.init()
        DayNightHelper.init()
        CoilHelper.init(this)
        ActivityHelper.init(this)

    }

}