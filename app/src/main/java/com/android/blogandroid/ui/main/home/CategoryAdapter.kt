package com.android.blogandroid.ui.main.home

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.android.blogandroid.R
import com.android.blogandroid.ext.dpToPx
import com.android.blogandroid.ext.htmlToSpanned
import com.android.blogandroid.model.bean.Category
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_category_sub.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 横栏Adapter
 *     version: 1.0
 */
class CategoryAdapter(layoutResId: Int = R.layout.item_category_sub) : BaseQuickAdapter<Category, BaseViewHolder>(layoutResId) {

    private var checkedPosition = 0

    var onCheckedListener: ((position: Int) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: Category) {
        holder.itemView.run {
            ctvCategory.text = item.name.htmlToSpanned()
            ctvCategory.isChecked = checkedPosition == holder.adapterPosition
            setOnClickListener {
                val position = holder.adapterPosition
                checked(position)
                onCheckedListener?.invoke(position)
            }
            updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = if (holder.adapterPosition == 0) 8.dpToPx().toInt() else 0.dpToPx().toInt()
            }
        }
    }

    fun checked(position: Int) {
        checkedPosition = position
        notifyDataSetChanged()
    }
}