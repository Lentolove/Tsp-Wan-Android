package com.android.blogandroid.common.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 *     author : shengping.tian
 *     time   : 2020/12/18
 *     desc   : 全局 login 状态监听
 *     version: 1.0
 */
object Bus {
    /**
     * 发布LiveDataEventBus消息
     * inline 的工作原理就是将内联函数的函数体复制到调用处实现内联。
     */
    inline fun <reified T> post(channel: String, value: T) {
        LiveEventBus.get(channel, T::class.java).post(value)
    }

    /**
     * 订阅消息
     */
    inline fun <reified T> observe(channel: String, owner: LifecycleOwner, observer: Observer<T>) {
        LiveEventBus.get(channel, T::class.java).observe(owner, observer)
    }
}