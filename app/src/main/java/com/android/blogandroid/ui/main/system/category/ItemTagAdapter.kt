package com.android.blogandroid.ui.main.system.category

import android.view.LayoutInflater
import android.view.View
import androidx.core.text.htmlEncode
import com.android.blogandroid.R
import com.android.blogandroid.model.bean.Category
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_system_category_tag.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/15
 *     desc   : https://github.com/hongyangAndroid/FlowLayout 流式布局
 *     version: 1.0
 */
class ItemTagAdapter(private val categoryList: List<Category>) :
    TagAdapter<Category>(categoryList) {

    override fun getView(parent: FlowLayout?, position: Int, t: Category?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_system_category_tag, parent, false).apply {
            tvTag.text = categoryList[position].name.htmlEncode()
        }
    }
}