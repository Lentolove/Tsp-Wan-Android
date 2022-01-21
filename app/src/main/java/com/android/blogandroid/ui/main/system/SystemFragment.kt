package com.android.blogandroid.ui.main.system

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseFragment
import com.android.blogandroid.base.BaseVmFragment
import com.android.blogandroid.common.ScrollToTop
import com.android.blogandroid.common.adapter.SimpleFragmentAdapter
import com.android.blogandroid.ext.toArrayList
import com.android.blogandroid.model.bean.Category
import com.android.blogandroid.ui.main.MainActivity
import com.android.blogandroid.ui.main.system.category.SystemCategoryFragment
import com.android.blogandroid.ui.main.system.pager.SystemPagerFragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_system.*
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : 体系界面
 *     version: 1.0
 */
class SystemFragment : BaseVmFragment<SystemViewModel>(), ScrollToTop {

    override fun layoutRes() = R.layout.fragment_system

    companion object {
        fun newInstance() = SystemFragment()
    }

    private var currentOffset = 0
    private val titles = mutableListOf<String>()
    private val fragments = mutableListOf<SystemPagerFragment>()
    private var categoryFragment: SystemCategoryFragment? = null

    override fun viewModelClazz() = SystemViewModel::class.java

    override fun initView() {
        reloadView.btnReload.setOnClickListener {
            mViewModel.getArticleCategory()
        }
        //开启底部弹窗
        ivFilter.setOnClickListener {
            categoryFragment?.show(childFragmentManager)
        }

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            categories.observe(viewLifecycleOwner) { categories ->
                ivFilter.visibility = View.VISIBLE
                tabLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                val newCategories = categories.filter {
                    it.children.isNotEmpty()
                }.toMutableList()
                //设置数据
                setup(newCategories)
                categoryFragment = SystemCategoryFragment.newInstance(newCategories.toArrayList())
                loadingStatus.observe(viewLifecycleOwner) {
                    progressBar.isVisible = it
                }
                reloadStatus.observe(viewLifecycleOwner) {
                    reloadView.isVisible = it
                }
            }
        }
    }

    private fun setup(categories: MutableList<Category>) {
        titles.clear()
        fragments.clear()
        categories.forEach {
            titles.add(it.name)
            fragments.add(SystemPagerFragment.newInstance(it.children.toArrayList()))
        }
        viewPager.adapter = SimpleFragmentAdapter(childFragmentManager, fragments, titles)
        viewPager.offscreenPageLimit = titles.size
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initData() {
        mViewModel.getArticleCategory()
    }

    override fun scrollToTop() {
        if (fragments.isEmpty() || viewPager == null) return
        fragments[viewPager.currentItem].scrollToTop()
    }

    fun getCurrentChecked(): Pair<Int, Int> {
        if (fragments.isEmpty() || viewPager == null) return 0 to 0
        val first = viewPager.currentItem
        val second = fragments[viewPager.currentItem].checkedPosition
        return first to second
    }

    fun checked(position: Pair<Int, Int>) {
        if (fragments.isEmpty() || viewPager == null) return
        viewPager.currentItem = position.first
        fragments[position.first].checked(position.second)
    }


}