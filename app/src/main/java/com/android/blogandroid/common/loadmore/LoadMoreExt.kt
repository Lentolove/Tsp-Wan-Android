package com.android.blogandroid.common.loadmore

import com.chad.library.adapter.base.module.BaseLoadMoreModule

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : todo BaseLoadMoreModule
 *     version: 1.0
 */

fun BaseLoadMoreModule.setLoadMoreStatus(loadMoreStatus: LoadMoreStatus){
    when(loadMoreStatus){
        LoadMoreStatus.COMPLETED -> loadMoreComplete()
        LoadMoreStatus.ERROR -> loadMoreFail()
        LoadMoreStatus.END -> loadMoreEnd()
        else -> return
    }
}