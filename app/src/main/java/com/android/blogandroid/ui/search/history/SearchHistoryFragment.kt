package com.android.blogandroid.ui.search.history

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.observe
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmFragment
import com.android.blogandroid.model.bean.HotWord
import com.android.blogandroid.ui.search.SearchActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.fragment_search_history.*
import kotlinx.android.synthetic.main.item_hot_word.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/17
 *     desc   :
 *     version: 1.0
 */
class SearchHistoryFragment : BaseVmFragment<SearchHistoryViewModel>() {

    companion object {
        fun newInstance() = SearchHistoryFragment()
    }

    override fun viewModelClazz() = SearchHistoryViewModel::class.java

    override fun layoutRes() = R.layout.fragment_search_history

    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    override fun initView() {
        val fragmentActivity = activity as? SearchActivity ?: return
        searchHistoryAdapter = SearchHistoryAdapter(fragmentActivity).also {
            //搜索 todo
            it.onItemClickListener = {position->
                fragmentActivity.fillSearchInput(it.data[position])
            }
            //删除
            it.onDeleteClickListener = { position ->
                mViewModel.deleteSearchHistory(it.data[position])
            }
            rvSearchHistory.adapter = it
        }
    }

    override fun initData() {
        mViewModel.getHotSearch()
        mViewModel.getSearchHistory()
    }

    override fun observe() {
        super.observe()
        mViewModel.hotWords.observe(viewLifecycleOwner) {
            tvHotSearch.visibility = View.VISIBLE
            setHotWords(it)
        }
        mViewModel.searchHistory.observe(viewLifecycleOwner) {
            tvSearchHistory.isGone = it.isEmpty()
            searchHistoryAdapter.submitList(it)
        }
    }

    /**
     * 设置热词的tag界面
     */
    private fun setHotWords(hotWords: List<HotWord>) {
        tflHotSearch.run {
            adapter = object : TagAdapter<HotWord>(hotWords) {
                override fun getView(parent: FlowLayout?, position: Int, t: HotWord?): View {
                    return LayoutInflater.from(parent?.context).inflate(R.layout.item_hot_word, parent, false).apply {
                        this.tvTag.text = t?.name
                    }
                }
            }
            setOnTagClickListener { _, position, _ ->
                (activity as? SearchActivity)?.fillSearchInput(hotWords[position].name)
                false
            }
        }
    }

    fun addSearchHistory(keywords: String) {
        mViewModel.addSearchHistory(keywords)
    }
}