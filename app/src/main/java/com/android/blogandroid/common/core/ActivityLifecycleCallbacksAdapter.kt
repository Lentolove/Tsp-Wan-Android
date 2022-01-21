package com.android.blogandroid.common.core

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : Activity 生命周期回调
 *     version: 1.0
 */
class ActivityLifecycleCallbacksAdapter(
    private val onActivityCreated: ((activity: Activity, savedInstanceState: Bundle?) -> Unit)? = null,
    private var onActivityStarted: ((activity: Activity) -> Unit)? = null,
    private var onActivityResumed: ((activity: Activity) -> Unit)? = null,
    private var onActivitySaveInstanceState: ((activity: Activity, outState: Bundle) -> Unit)? = null,
    private var onActivityPaused: ((activity: Activity) -> Unit)? = null,
    private var onActivityStopped: ((activity: Activity) -> Unit)? = null,
    private var onActivityDestroyed: ((activity: Activity) -> Unit)? = null
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        onActivityCreated?.invoke(activity, savedInstanceState)
    }

    override fun onActivityStarted(activity: Activity) {
        onActivityStarted?.invoke(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        onActivityResumed?.invoke(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        onActivitySaveInstanceState?.invoke(activity, outState)
    }

    override fun onActivityPaused(activity: Activity) {
        onActivityPaused?.invoke(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        onActivityStopped?.invoke(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        onActivityDestroyed?.invoke(activity)
    }
}