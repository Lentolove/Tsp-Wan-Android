package com.android.blogandroid.ui.main.home


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseFragment
import com.android.blogandroid.common.ScrollToTop
import com.android.blogandroid.common.adapter.SimpleFragmentAdapter
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.ui.main.MainActivity
import com.android.blogandroid.ui.main.home.latest.LatestFragment
import com.android.blogandroid.ui.main.home.plaza.PlazaFragment
import com.android.blogandroid.ui.main.home.popular.PopularFragment
import com.android.blogandroid.ui.main.home.project.ProjectFragment
import com.android.blogandroid.ui.main.home.wechat.WeChatFragment
import com.android.blogandroid.ui.search.SearchActivity
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_home.*


/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : HomeFragment,有 热门，最新，广场，项目，公众号 五大页面
 *     version: 1.0
 */
class HomeFragment : BaseFragment(), ScrollToTop {


    override fun layoutRes() = R.layout.fragment_home

    private lateinit var fragments: List<Fragment>

    private var currentOffset = 0


    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragments = listOf(
                PopularFragment.newInstance(),
                LatestFragment.newInstance(),
                PlazaFragment.newInstance(),
                ProjectFragment.newInstance(),
                WeChatFragment.newInstance()
        )
        val titles = listOf<CharSequence>(
                getString(R.string.popular_articles),
                getString(R.string.latest_project),
                getString(R.string.plaza),
                getString(R.string.project_category),
                getString(R.string.wechat_public)
        )
        viewPager.adapter = SimpleFragmentAdapter(
                fm = childFragmentManager,
                fragments = fragments,
                titles = titles
        )
        //设置viewpager页数
        viewPager.offscreenPageLimit = fragments.size
        tabLayout.setupWithViewPager(viewPager)

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })
        llSearch.setOnClickListener { ActivityHelper.start(SearchActivity::class.java) }
    }

    override fun scrollToTop() {
        if (!this::fragments.isInitialized) return
        val currentFragment = fragments[viewPager.currentItem]
        if (currentFragment is ScrollToTop && currentFragment.isVisible) {
            currentFragment.scrollToTop()
        }
    }
}