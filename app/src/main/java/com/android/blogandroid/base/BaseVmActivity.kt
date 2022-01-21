package com.android.blogandroid.base

import android.os.Bundle
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
 *     desc   : 带 viewModel的基类
 *     version: 1.0
 */
abstract class BaseVmActivity<VM : BaseViewModel> : BaseActivity() {

    protected open lateinit var mViewModel: VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        observe()
        initView()
        initData()
    }


    //初始化ViewModel
    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }

    /**
     * 获取ViewModel的clazz
     */
    protected abstract fun viewModelClass(): Class<VM>

    /**
     * 订阅 LIveData Bus 等
     *
     */
    open fun observe() {
        //登录失效，挑战到登录界面
        mViewModel.loginStatesInvalid.observe(this) {
            if (it) {
                Bus.post(USER_LOGIN_STATE_CHANGED, false)
                ActivityHelper.start(LoginActivity::class.java)
            }
        }
    }

    /**
     * 初始化数据
     */
    open fun initView() {
    }

    /**
     * 懒加载数据
     */
    open fun initData() {

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