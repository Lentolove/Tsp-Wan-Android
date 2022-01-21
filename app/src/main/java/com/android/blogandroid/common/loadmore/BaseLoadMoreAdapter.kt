package com.android.blogandroid.common.loadmore

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 实现 LoadMoreModule 才能加载更多
 *     version: 1.0
 */
abstract class BaseLoadMoreAdapter<T, VH : BaseViewHolder> @JvmOverloads constructor(
    @LayoutRes private val layoutResId: Int,
    data: MutableList<T>? = null
) : BaseQuickAdapter<T, VH>(layoutResId,data), LoadMoreModule {
}