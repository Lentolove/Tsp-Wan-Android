package com.android.blogandroid.ui.main.system.category

import com.android.blogandroid.R
import com.android.blogandroid.ext.htmlToSpanned
import com.android.blogandroid.model.bean.Category
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_system_category.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/15
 *     desc   :
 *     version: 1.0
 */
class SystemCategoryAdapter(
    layoutRes: Int = R.layout.item_system_category,
    categoryList: MutableList<Category>,
    var checked: Pair<Int, Int>
) : BaseQuickAdapter<Category, BaseViewHolder>(layoutRes, categoryList) {

    var onCheckedListener: ((checked: Pair<Int, Int>) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: Category) {
        holder.itemView.run {
            title.text = item.name.htmlToSpanned()
            tagFlowLayout.adapter = ItemTagAdapter(item.children)
            if (checked.first == holder.adapterPosition){
                tagFlowLayout.adapter.setSelectedList(checked.second)
            }
            tagFlowLayout.setOnTagClickListener { _, position, _ ->
                checked = holder.adapterPosition to position
                notifyDataSetChanged()
                tagFlowLayout.postDelayed({
                    onCheckedListener?.invoke(checked)
                },300)
                true
            }
        }
    }
}