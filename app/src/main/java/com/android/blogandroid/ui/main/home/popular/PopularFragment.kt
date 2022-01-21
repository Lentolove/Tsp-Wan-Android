package com.android.blogandroid.ui.main.home.popular

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
import com.android.blogandroid.ui.main.home.ArticleAdapter
import kotlinx.android.synthetic.main.fragment_popular.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : 热门 Fragment 界面
 *     version: 1.0
 */
class PopularFragment : BaseVmFragment<PopularViewModel>(), ScrollToTop {


    override fun viewModelClazz(): Class<PopularViewModel> = PopularViewModel::class.java

    companion object {
        fun newInstance() = PopularFragment()
    }

    private lateinit var mAdapter: ArticleAdapter

    override fun layoutRes() = R.layout.fragment_popular

    override fun initView() {
        initRefresh()
        initAdapter()
        initListener()
    }

    /**
     * 初始化刷新
     */
    private fun initRefresh(){
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshArticleList() }
        }
    }


    private fun initAdapter(){
        mAdapter = ArticleAdapter(R.layout.item_article).also {
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
            //todo 点击收藏逻辑按钮
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

            recyclerView.adapter =  it
        }
    }

    /**
     * 刷新事件
     */
    private fun initListener(){
        btnReload.setOnClickListener {
            mViewModel.refreshArticleList()
        }
    }

    /**
     * lifecycle监听
     */
    override fun observe() {
        super.observe()
        mViewModel.articleList.observe(viewLifecycleOwner){
            mAdapter.setList(it)
        }
        mViewModel.refreshStatus.observe(viewLifecycleOwner){
            swipeRefreshLayout.isRefreshing = it
        }
        mViewModel.loadMoreStatus.observe(viewLifecycleOwner){
            mAdapter.loadMoreModule.setLoadMoreStatus(it)
        }
        mViewModel.reloadStatus.observe(viewLifecycleOwner) {
            reloadView.isVisible = it
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, Observer{
            mViewModel.updateListCollectState()
        })
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner,Observer {
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

