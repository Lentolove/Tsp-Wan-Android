package com.android.blogandroid.ui.main.home.wechat

import androidx.core.view.isGone
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
import com.android.blogandroid.model.bean.Category
import com.android.blogandroid.ui.detail.DetailActivity
import com.android.blogandroid.ui.main.home.CategoryAdapter
import com.android.blogandroid.ui.main.home.SimpleArticleAdapter
import kotlinx.android.synthetic.main.fragment_wechat.*
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   :
 *     version: 1.0
 */
class WeChatFragment : BaseVmFragment<WeChatViewModel>(), ScrollToTop {

    companion object {
        fun newInstance() = WeChatFragment()
    }

    private lateinit var mAdapter:SimpleArticleAdapter
    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun layoutRes() = R.layout.fragment_wechat

    override fun viewModelClazz() = WeChatViewModel::class.java

    override fun initView() {
        initRefresh()
        initCategoryAdapter()
        initArticleAdapter()
        initListeners()
    }

    private fun initRefresh(){
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.refreshWeChatArticleList()
            }
        }
    }

    private fun initCategoryAdapter() {
        mCategoryAdapter = CategoryAdapter(R.layout.item_category_sub).also {
            it.onCheckedListener = { position ->
                mViewModel.refreshWeChatArticleList(position)
            }
            rvCategory.adapter = it
        }
    }
    private fun initArticleAdapter() {
        mAdapter = SimpleArticleAdapter(R.layout.item_article_simple).also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreWeChatArticleList()
            }
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.start(
                        DetailActivity::class.java,
                        mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
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

    private fun initListeners() {
        reloadListView.btnReload.setOnClickListener {
            mViewModel.refreshWeChatArticleList()
        }
        reloadView.btnReload.setOnClickListener {
            mViewModel.getWeChatCategory()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.categories.observe(viewLifecycleOwner) {
            updateCategory(it)
        }
        mViewModel.checkedCategory.observe(viewLifecycleOwner) {
            mCategoryAdapter.checked(it)
        }
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
        mViewModel.reloadListStatus.observe(viewLifecycleOwner) {
            reloadListView.isVisible = it
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, Observer {
            mViewModel.updateListCollectState()
        })
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, Observer {
            mViewModel.updateItemCollectState(it)
        })
    }

    private fun updateCategory(categoryList: MutableList<Category>) {
        rvCategory.isGone = categoryList.isEmpty()
        mCategoryAdapter.setList(categoryList)
    }

    override fun lazyLoadData() {
        mViewModel.getWeChatCategory()
    }

    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }
}