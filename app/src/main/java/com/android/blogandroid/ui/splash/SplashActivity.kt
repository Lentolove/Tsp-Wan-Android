package com.android.blogandroid.ui.splash

import android.os.Bundle
import com.android.blogandroid.ui.main.MainActivity
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseActivity
import com.android.blogandroid.common.core.ActivityHelper
/**
 *     author : shengping.tian
 *     time   : 2020/12/15
 *     desc   : Splash 页面
 *     version: 1.0
 */
class SplashActivity : BaseActivity() {

    override fun layoutRes() = R.layout.activity_splash


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //暂停一秒后进入主页面
        window.decorView.postDelayed({
            ActivityHelper.start(MainActivity::class.java)
            ActivityHelper.finish(SplashActivity::class.java)
        }, 1000)
    }
}