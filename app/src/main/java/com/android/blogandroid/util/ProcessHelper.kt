package com.android.blogandroid.util

import android.app.ActivityManager
import android.content.Context
import android.os.Process.myPid


/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : 进程工具类
 *     version: 1.0
 */

/**
 * 是否是主进程
 */
fun isMainProcess(context: Context) = context.packageName == currentProcessName(context)

/**
 * 获取当前进程名称
 */
private fun currentProcessName(context: Context): String {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (process in manager.runningAppProcesses) {
        if (process.pid == myPid()) {
            return process.processName
        }
    }
    return ""
}

