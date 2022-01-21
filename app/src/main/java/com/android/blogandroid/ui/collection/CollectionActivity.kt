package com.android.blogandroid.ui.collection

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmActivity
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_COLLECT_UPDATED
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.common.loadmore.setLoadMoreStatus
import com.android.blogandroid.ui.detail.DetailActivity
import com.android.blogandroid.ui.main.home.ArticleAdapter
import kotlinx.android.synthetic.main.activity_collect.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   :
 *     version: 1.0
 */
class CollectionActivity : BaseVmActivity<CollectionViewModel>() {

    override fun layoutRes() = R.layout.activity_collect

    override fun viewModelClass() = CollectionViewModel::class.java

    private lateinit var adapter: ArticleAdapter

    override fun initView() {
        initAdapter()
        initRefresh()
        initListener()
    }

    private fun initAdapter() {
        adapter = ArticleAdapter().also {
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.start(
                        DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = it.data[position]
                if (view.id == R.id.iv_collect) {
                    mViewModel.unCollect(article.originId)
                    removeItem(position)
                }
            }
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMore()
            }
            recyclerView.adapter = it
        }
    }

    private fun initRefresh() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }
    }

    private fun initListener() {
        btnReload.setOnClickListener { mViewModel.refresh() }
        ivBack.setOnClickListener { ActivityHelper.finish(CollectionActivity::class.java) }
    }

    private fun removeItem(position: Int) {
        adapter.removeAt(position)
        emptyView.isVisible = adapter.data.isEmpty()
    }

    override fun initData() {
        mViewModel.refresh()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            articleList.observe(this@CollectionActivity) {
                adapter.setList(it)
            }
            refreshStatus.observe(this@CollectionActivity) {
                swipeRefreshLayout.isRefreshing = it
            }
            emptyStatus.observe(this@CollectionActivity) {
                emptyView.isVisible = it
            }
            loadStatus.observe(this@CollectionActivity) {
                adapter.loadMoreModule.setLoadMoreStatus(it)
            }
            reloadStatus.observe(this@CollectionActivity) {
                reloadView.isVisible = it
            }
            Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, this@CollectionActivity, Observer {
                onCollectUpdate(it.first,it.second)
            })
        }
    }

    private fun onCollectUpdate(id: Long, collect: Boolean) {
        if (collect) {
            mViewModel.refresh()
        } else {
            val position = adapter.data.indexOfFirst {
                it.originId == id
            }
            if (position != -1){
                removeItem(position)
            }
        }
    }

}