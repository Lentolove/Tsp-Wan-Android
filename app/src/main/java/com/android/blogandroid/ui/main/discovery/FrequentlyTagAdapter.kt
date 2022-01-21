package com.android.blogandroid.ui.main.discovery

import android.view.LayoutInflater
import android.view.View
import com.android.blogandroid.R
import com.android.blogandroid.model.bean.Frequently
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_nav_tag.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
class FrequentlyTagAdapter(private val frequentlyList: List<Frequently>) :
    TagAdapter<Frequently>(frequentlyList) {

    override fun getView(parent: FlowLayout?, position: Int, t: Frequently?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_nav_tag, parent, false)
            .apply {
                tvTag.text = frequentlyList[position].name
            }
    }
}