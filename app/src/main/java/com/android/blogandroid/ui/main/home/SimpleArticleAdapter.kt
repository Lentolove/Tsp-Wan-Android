package com.android.blogandroid.ui.main.home

import androidx.core.view.isVisible
import com.android.blogandroid.R
import com.android.blogandroid.common.loadmore.BaseLoadMoreAdapter
import com.android.blogandroid.ext.htmlToSpanned
import com.android.blogandroid.model.bean.Article
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_article_simple.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   :
 *     version: 1.0
 */
class SimpleArticleAdapter(layoutResId: Int = R.layout.item_article_simple) : BaseLoadMoreAdapter<Article, BaseViewHolder>(layoutResId) {

    init {
        addChildClickViewIds(R.id.iv_collect)
    }

    override fun convert(holder: BaseViewHolder, item: Article) {
        holder.run {
            itemView.run {
                tv_author.text = when {
                    !item.author.isNullOrEmpty() -> {
                        item.author
                    }
                    !item.shareUser.isNullOrEmpty() -> {
                        item.shareUser
                    }
                    else -> context.getString(R.string.anonymous)
                }
                tv_fresh.isVisible = item.fresh
                tv_title.text = item.title.htmlToSpanned()
                tv_time.text = item.niceDate
                iv_collect.isSelected = item.collect
            }
        }
    }
}