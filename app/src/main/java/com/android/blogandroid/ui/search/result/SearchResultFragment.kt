package com.android.blogandroid.ui.search.result

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmFragment
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_COLLECT_UPDATED
import com.android.blogandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.common.loadmore.setLoadMoreStatus
import com.android.blogandroid.ui.detail.DetailActivity
import com.android.blogandroid.ui.main.home.ArticleAdapter
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/17
 *     desc   : 搜索结果界面
 *     version: 1.0
 */
class SearchResultFragment : BaseVmFragment<SearchResultViewModel>() {

    companion object {
        fun newInstance() = SearchResultFragment()
    }

    override fun viewModelClazz() = SearchResultViewModel::class.java

    override fun layoutRes() = R.layout.fragment_search_result

    private lateinit var searchArticleAdapter: ArticleAdapter

    override fun initView() {
        initRefresh()
        initAdapter()
        initListener()
    }

    /**
     * 搜过结果下拉刷新
     */
    private fun initRefresh() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.search() }
        }
    }

    private fun initAdapter() {
        searchArticleAdapter = ArticleAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMore()
            }
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.start(DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article))
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = it.data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        mViewModel.unCollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }
                }
            }
            recyclerView.adapter = it
        }
    }

    private fun initListener() {
        btnReload.setOnClickListener { mViewModel.search() }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            articleList.observe(viewLifecycleOwner) {
                searchArticleAdapter.setList(it)
            }
            refreshStatus.observe(viewLifecycleOwner) {
                swipeRefreshLayout.isRefreshing = it
            }
            emptyStatus.observe(viewLifecycleOwner) {
                emptyView.isVisible = it
            }
            loadMoreStatus.observe(viewLifecycleOwner) {
                searchArticleAdapter.loadMoreModule.setLoadMoreStatus(it)
            }
            reloadStatus.observe(viewLifecycleOwner) {
                reloadView.isVisible = it
            }
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, Observer{
            mViewModel.updateListCollectState()
        })
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, Observer{
            mViewModel.updateItemCollectState(it)
        })
    }

    fun doSearch(word: String) {
        mViewModel.search(word)
    }
}