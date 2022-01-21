package com.android.blogandroid.ui.main.navigation

import com.android.blogandroid.R
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.bean.Navigation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_navigation.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
class NavigationAdapter(layoutRes: Int = R.layout.item_navigation) :
    BaseQuickAdapter<Navigation, BaseViewHolder>(layoutRes) {

    var onItemTagClickListener: ((article: Article) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: Navigation) {
        holder.itemView.run {
            title.text = item.name
            tagFlawLayout.adapter = ItemTagAdapter(item.articles)
            tagFlawLayout.setOnTagClickListener { _, position, _ ->
                onItemTagClickListener?.invoke(item.articles[position])
                true
            }
        }
    }
}