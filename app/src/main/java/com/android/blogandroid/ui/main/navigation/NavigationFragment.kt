package com.android.blogandroid.ui.main.navigation

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmFragment
import com.android.blogandroid.common.ScrollToTop
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.ui.detail.DetailActivity
import com.android.blogandroid.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : 底部导航界面
 *     version: 1.0
 */
class NavigationFragment : BaseVmFragment<NavigationViewModel>(), ScrollToTop {

    override fun layoutRes() = R.layout.fragment_navigation

    companion object {
        fun newInstance() = NavigationFragment()
    }

    private lateinit var mAdapter: NavigationAdapter
    private var currentPosition = 0

    override fun viewModelClazz() = NavigationViewModel::class.java

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.getNavigationList() }
        }
        mAdapter = NavigationAdapter(R.layout.item_navigation).also {
            it.onItemTagClickListener = { article ->
                ActivityHelper.start(
                        DetailActivity::class.java,
                        mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            recyclerView.adapter = it
        }
        btnReload.setOnClickListener { mViewModel.getNavigationList() }
        recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                //动画 往上滑动显示更多的时候隐藏底部bottomBar
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
            if (scrollY < oldScrollY) {
                tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
            val lm = recyclerView.layoutManager as LinearLayoutManager
            val nextView = lm.findViewByPosition(currentPosition + 1)
            if (nextView != null) {
                tvFloatTitle.y = if (nextView.top < tvFloatTitle.measuredHeight) {
                    (nextView.top - tvFloatTitle.measuredHeight).toFloat()
                } else {
                    0f
                }
            }
            currentPosition = lm.findFirstVisibleItemPosition()
            if (scrollY > oldScrollY) {
                tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            navigationList.observe(viewLifecycleOwner) {
                tvFloatTitle.isGone = it.isEmpty()
                tvFloatTitle.text = it[0].name
                mAdapter.setList(it)
            }
            refreshStatus.observe(viewLifecycleOwner) {
                swipeRefreshLayout.isRefreshing = it
            }
            reloadStatus.observe(viewLifecycleOwner) {
                reloadView.isVisible = it
            }
        }
    }

    override fun initData() {
        mViewModel.getNavigationList()
    }

    override fun scrollToTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

}