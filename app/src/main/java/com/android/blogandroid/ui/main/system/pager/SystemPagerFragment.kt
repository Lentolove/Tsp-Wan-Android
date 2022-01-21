package com.android.blogandroid.ui.main.system.pager

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmFragment
import com.android.blogandroid.common.ScrollToTop
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_COLLECT_UPDATED
import com.android.blogandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.common.loadmore.setLoadMoreStatus
import com.android.blogandroid.ext.dpToPx
import com.android.blogandroid.model.bean.Category
import com.android.blogandroid.ui.detail.DetailActivity
import com.android.blogandroid.ui.main.home.CategoryAdapter
import com.android.blogandroid.ui.main.home.SimpleArticleAdapter
import kotlinx.android.synthetic.main.fragment_system_pager.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 体系界面
 *     version: 1.0
 */
class SystemPagerFragment : BaseVmFragment<SystemPagerViewModel>(), ScrollToTop {

    companion object {
        private const val CATEGORY_LIST = "CATEGORY_LIST"

        fun newInstance(categoryList: ArrayList<Category>): SystemPagerFragment {
            return SystemPagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_LIST, categoryList)
                }
            }
        }
    }

    private lateinit var categoryList: List<Category>
    var checkedPosition = 0
    private lateinit var mAdapter: SimpleArticleAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun viewModelClazz() = SystemPagerViewModel::class.java

    override fun layoutRes() = R.layout.fragment_system_pager

    override fun initView() {
        super.initView()
        categoryList = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        checkedPosition = 0

        initRefresh()
        initCategoryAdapter()
        initArticleAdapter()
        initListeners()
    }

    private fun initRefresh() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }
    }

    private fun initCategoryAdapter() {
        categoryAdapter = CategoryAdapter(R.layout.item_category_sub).also {
            it.setList(categoryList)
            it.onCheckedListener = { position ->
                checkedPosition = position
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
            rvCategory.adapter = it
        }
    }

    private fun initArticleAdapter() {
        mAdapter = SimpleArticleAdapter(R.layout.item_article_simple).also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreArticleList(categoryList[checkedPosition].id)
            }
            it.setOnItemChildClickListener { _, _, position ->
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
        btnReload.setOnClickListener {
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
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
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, Observer{
            mViewModel.updateListCollectState()
        })
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, Observer{
            mViewModel.updateItemCollectState(it)
        })
    }

    override fun lazyLoadData() {
        mViewModel.refreshArticleList(categoryList[checkedPosition].id)
    }


    override fun scrollToTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    fun checked(position: Int) {
        if (position != checkedPosition){
            checkedPosition = position
            categoryAdapter.checked(position)
            (rvCategory.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(position,8.dpToPx().toInt())
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
    }
}