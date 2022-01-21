package com.android.blogandroid.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.model.store.isLogin
import com.android.blogandroid.ui.login.LoginActivity

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   :
 *     version: 1.0
 */
abstract class BaseVmFragment<VM : BaseViewModel> : BaseFragment() {

    protected lateinit var mViewModel: VM

    private var lazyLoaded = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observe()
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        //实现懒加载
        if (!lazyLoaded) {
            lazyLoadData()
            lazyLoaded = true
        }
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClazz())
    }

    //获取vm类型 clazz
    abstract fun viewModelClazz(): Class<VM>

    /**
     * View初始化相关
     */
    open fun initView() {
        // Override if need
    }

    /**
     * 数据初始化相关
     */
    open fun initData() {
        // Override if need
    }

    /**
     * 懒加载数据
     */
    open fun lazyLoadData() {
        // Override if need
    }

    /**
     * 订阅 LIveData Bus 等
     *
     */
    open fun observe() {
        //todo 登录失效，挑战到登录界面
        mViewModel.loginStatesInvalid.observe(this) {
            if (it) {
                Bus.post(USER_LOGIN_STATE_CHANGED, false)
                ActivityHelper.start(LoginActivity::class.java)
            }
        }
    }

    /**
     * 检查是否登录，如果登录了就执行then，没有登录就直接跳转登录界面
     */
    fun checkLogin(then: (() -> Unit)? = null): Boolean {
        return if (isLogin()) {
            then?.invoke()
            true
        } else {
            ActivityHelper.start(LoginActivity::class.java)
            false
        }
    }
}