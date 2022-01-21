package com.android.blogandroid.ui.main.home

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.android.blogandroid.R
import com.android.blogandroid.common.loadmore.BaseLoadMoreAdapter
import com.android.blogandroid.ext.htmlToSpanned
import com.android.blogandroid.model.bean.Article
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_article.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   :
 *     version: 1.0
 */
class ArticleAdapter(
    layoutRes:Int = R.layout.item_article
) : BaseLoadMoreAdapter<Article, BaseViewHolder>(layoutRes) {


    init {
        /**
         * 设置需要点击事件的子view
         * 设置item 收藏按钮事件
         */
        addChildClickViewIds(R.id.iv_collect)
    }

    override fun convert(holder: BaseViewHolder, item: Article) {
        holder.run {
            itemView.run {
                tv_author.text = when{
                    !item.author.isNullOrEmpty() -> {
                        item.author
                    }
                    !item.shareUser.isNullOrEmpty() -> {
                        item.shareUser
                    }
                    else -> context.getString(R.string.anonymous)
                }
                tv_top.isVisible = item.top
                tv_fresh.isVisible = item.fresh && !item.top
                tv_tag.visibility = if (item.tags.isNotEmpty()) {
                    tv_tag.text = item.tags[0].name
                    View.VISIBLE
                } else {
                    View.GONE
                }
                tv_chapter.text = when {
                    !item.superChapterName.isNullOrEmpty() && !item.chapterName.isNullOrEmpty() ->
                        "${item.superChapterName.htmlToSpanned()}/${item.chapterName.htmlToSpanned()}"
                    item.superChapterName.isNullOrEmpty() && !item.chapterName.isNullOrEmpty() ->
                        item.chapterName.htmlToSpanned()
                    !item.superChapterName.isNullOrEmpty() && item.chapterName.isNullOrEmpty() ->
                        item.superChapterName.htmlToSpanned()
                    else -> ""

                }
                tv_title.text = item.title.htmlToSpanned()
                tv_desc.text = item.desc.htmlToSpanned()
                tv_desc.isGone = item.desc.isNullOrEmpty()
                tv_time.text = item.niceDate
                iv_collect.isSelected = item.collect
            }
        }
    }
}