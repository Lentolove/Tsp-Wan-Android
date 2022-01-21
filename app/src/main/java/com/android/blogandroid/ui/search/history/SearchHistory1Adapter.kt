package com.android.blogandroid.ui.search.history

import com.android.blogandroid.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_search_history.view.*


/**
 *     author : shengping.tian
 *     time   : 2020/12/17
 *     desc   : ListAdapter使用
 *     version: 1.0
 */
class SearchHistory1Adapter(layoutResId:Int = R.layout.item_search_history) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId) {

    var onItemClickListener: ((position: Int) -> Unit)? = null
    var onDeleteClickListener: ((position: Int) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.run {
            tvLabel.text = item
            setOnClickListener {
                onItemClickListener?.invoke(holder.adapterPosition)
            }
            ivDelete.setOnClickListener {
                onDeleteClickListener?.invoke(holder.adapterPosition)
            }
        }
    }

}
