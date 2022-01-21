package com.android.blogandroid.ui.main.home.latest

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
import kotlinx.android.synthetic.main.fragment_latest.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 最新模块 界面跟 热门 一样
 *     version: 1.0
 */
class LatestFragment : BaseVmFragment<LatestViewModel>(), ScrollToTop {

    companion object {

        private const val TAG = "LatestFragment"

        fun newInstance() = LatestFragment()
    }

    private lateinit var mAdapter: ArticleAdapter

    override fun layoutRes() = R.layout.fragment_latest

    override fun viewModelClazz(): Class<LatestViewModel> = LatestViewModel::class.java

    override fun initView() {
        initRefresh()
        initAdapter()
        initListener()
    }

    private fun initRefresh(){
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshProjectList() }
        }
    }

    private fun initAdapter(){
        mAdapter = ArticleAdapter(R.layout.item_article).also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreProjectList()
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

    private fun initListener(){
        //刷新按钮事件
        btnReload.setOnClickListener {
            mViewModel.refreshProjectList()
        }
    }

    /**
     * livedata数据监听
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
        mViewModel.reloadStatus.observe(viewLifecycleOwner){
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
        mViewModel.refreshProjectList()
    }



    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }
}