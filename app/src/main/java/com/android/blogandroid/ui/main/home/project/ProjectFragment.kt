package com.android.blogandroid.ui.main.home.project

import android.util.Log
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
import com.android.blogandroid.ui.main.home.ArticleAdapter
import com.android.blogandroid.ui.main.home.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 项目列表
 *     version: 1.0
 */
class ProjectFragment : BaseVmFragment<ProjectViewModel>(), ScrollToTop {

    companion object {
        fun newInstance() = ProjectFragment()
    }

    override fun layoutRes() = R.layout.fragment_project

    private lateinit var mAdapter: ArticleAdapter

    //横栏Adapter
    private lateinit var mCategoryAdapter: CategoryAdapter


    override fun viewModelClazz() = ProjectViewModel::class.java

    override fun initView() {
        initRefresh()
        initCategoryAdapter()
        initArticleAdapter()
        initListener()
    }

    private fun initRefresh() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.refreshProjectList()
            }
        }
    }

    private fun initCategoryAdapter() {
        mCategoryAdapter = CategoryAdapter(R.layout.item_category_sub).also {
            it.onCheckedListener = { position ->
                mViewModel.refreshProjectList(position)
            }
            rvCategory.adapter = it
        }
    }

    private fun initArticleAdapter() {
        mAdapter = ArticleAdapter(R.layout.item_article).also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreProjectList()
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

    private fun initListener() {
        reloadListView.btnReload.setOnClickListener {
            mViewModel.refreshProjectList()
        }
        reloadView.btnReload.setOnClickListener {
            mViewModel.getProjectCategory()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.categories.observe(viewLifecycleOwner){
            updateCategory(it)
        }
        mViewModel.checkedCategory.observe(viewLifecycleOwner){
            mCategoryAdapter.checked(it)
        }
        mViewModel.articleList.observe(viewLifecycleOwner){
            Log.i("tsp","articleList ${it}")
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
        mViewModel.reloadListStatus.observe(viewLifecycleOwner) {
            reloadListView.isVisible = it
        }

        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, Observer{
            mViewModel.updateListCollectState()
        })
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, Observer{
            mViewModel.updateItemCollectState(it)
        })
    }

    private fun updateCategory(categoryList: MutableList<Category>) {
        rvCategory.isGone = categoryList.isEmpty()
        mCategoryAdapter.setList(categoryList)
    }

    override fun lazyLoadData() {
        mViewModel.getProjectCategory()
    }

    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }
}