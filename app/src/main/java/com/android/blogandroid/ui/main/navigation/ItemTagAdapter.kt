package com.android.blogandroid.ui.main.navigation

import android.view.LayoutInflater
import android.view.View
import com.android.blogandroid.R
import com.android.blogandroid.model.bean.Article
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_nav_tag.view.*


/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
class ItemTagAdapter(private val articles: List<Article>) : TagAdapter<Article>(articles) {
    override fun getView(parent: FlowLayout?, position: Int, article: Article?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_nav_tag, parent, false)
            .apply {
                tvTag.text = articles[position].title
            }
    }
}