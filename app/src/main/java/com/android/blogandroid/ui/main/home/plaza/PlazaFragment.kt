package com.android.blogandroid.ui.main.home.plaza

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmFragment
import com.android.blogandroid.common.ScrollToTop
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_COLLECT_UPDATED
import com.android.blogandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.common.loadmore.setLoadMoreStatus
import com.android.blogandroid.ui.detail.DetailActivity
import com.android.blogandroid.ui.main.home.SimpleArticleAdapter
import kotlinx.android.synthetic.main.fragment_plaza.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : home -> 广场
 *     version: 1.0
 */
class PlazaFragment : BaseVmFragment<PlazaViewModel>(), ScrollToTop {

    companion object {
        fun newInstance() = PlazaFragment()
    }

    override fun viewModelClazz() = PlazaViewModel::class.java

    override fun layoutRes() = R.layout.fragment_plaza

    private lateinit var mAdapter: SimpleArticleAdapter


    override fun initView() {
        initRefresh()
        initAdapter()
        initListener()
    }

    private fun initRefresh() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshArticleList() }
        }
    }

    private fun initAdapter() {
        mAdapter = SimpleArticleAdapter(R.layout.item_article_simple).also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreArticleList()
            }
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.start(
                        DetailActivity::class.java,
                        mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            // 收藏
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
        btnReload.setOnClickListener {
            mViewModel.refreshArticleList()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.articleList.observe(viewLifecycleOwner) {
            mAdapter.setList(it)
        }
        mViewModel.refreshStatus.observe(viewLifecycleOwner) {
            swipeRefreshLayout.isRefreshing = it
        }
        mViewModel.loadMoreStatus.observe(viewLifecycleOwner) {
            mAdapter.loadMoreModule.setLoadMoreStatus(it)
        }
        mViewModel.reloadStatus.observe(viewLifecycleOwner) {
            reloadView.isVisible = it
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, Observer {
            mViewModel.updateListCollectState()
        })
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, Observer{
            mViewModel.updateItemCollectState(it)
        })
    }

    override fun lazyLoadData() {
        mViewModel.refreshArticleList()
    }

    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }
}