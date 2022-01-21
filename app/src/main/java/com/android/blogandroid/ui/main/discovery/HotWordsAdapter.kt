package com.android.blogandroid.ui.main.discovery

import com.android.blogandroid.R
import com.android.blogandroid.model.bean.HotWord
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_hot_word.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :  热词布局
 *     version: 1.0
 */
class HotWordsAdapter(layoutRes: Int = R.layout.item_hot_word) :
    BaseQuickAdapter<HotWord, BaseViewHolder>(layoutRes) {

    override fun convert(holder: BaseViewHolder, item: HotWord) {
        holder.itemView.tvTag.text = item.name
    }
}