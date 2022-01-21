package com.android.blogandroid.common.loadmore

import com.chad.library.adapter.base.module.LoadMoreModuleConfig

object LoadMoreHelper {

    @JvmStatic
    fun init() {
        LoadMoreModuleConfig.defLoadMoreView = CommonLoadMoreView()
    }
}