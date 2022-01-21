package com.android.blogandroid.common.core

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.android.blogandroid.ext.putExtras

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : Activity 管理类
 *     version: 1.0
 */
object ActivityHelper {

    private val activities = mutableListOf<Activity>()

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksAdapter(
            onActivityCreated = { activity, _ ->
                activities.add(activity)
            },

            onActivityDestroyed = { activity ->
                activities.remove(activity)
            }
        ))
    }

    /**
     * 启动 activity 带参数启动
     */
    fun start(clazz: Class<out Activity>, params: Map<String, Any> = emptyMap()) {
        //当前activity
        val currentActivity = activities[activities.lastIndex]
        val intent = Intent(currentActivity, clazz)
        params.forEach {
            intent.putExtras(it.key to it.value)
        }
        currentActivity.startActivity(intent)
    }

    /**
     * 关闭 Activity
     */
    fun finish(vararg clazz: Class<out Activity>) {
        activities.forEach {
            if (clazz.contains(it::class.java)) {
                it.finish()
            }
        }
    }

}