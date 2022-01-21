package com.android.blogandroid.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.ViewPropertyAnimator
import androidx.fragment.app.Fragment
import com.android.blogandroid.R
import com.android.blogandroid.R.id.*
import com.android.blogandroid.base.BaseActivity
import com.android.blogandroid.common.ScrollToTop
import com.android.blogandroid.ext.showToast
import com.android.blogandroid.ui.main.discovery.DiscoveryFragment
import com.android.blogandroid.ui.main.home.HomeFragment
import com.android.blogandroid.ui.main.navigation.NavigationFragment
import com.android.blogandroid.ui.main.profile.ProfileFragment
import com.android.blogandroid.ui.main.system.SystemFragment
import com.google.android.material.animation.AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException

/**
 * 底部状态栏五个
 * 首页 homeFragment
 * 体系 ProfileFragment
 * 发现 DiscoveryFragment
 * 导航 NavigationFragment
 * 我的 SystemFragment
 */
class MainActivity : BaseActivity() {


    private lateinit var fragments: Map<Int, Fragment>

    //设置底部动画 隐藏
    private var bottomNavigationViewAnimator: ViewPropertyAnimator? = null
    private var currentBottomNavigationStatus = true


    //记录两次返回键提示退出应用
    private var previousTimeMillis = 0L

    override fun layoutRes() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragments = mapOf(
                home to createFragment(HomeFragment::class.java),
                system to createFragment(SystemFragment::class.java),
                discovery to createFragment(DiscoveryFragment::class.java),
                navigation to createFragment(NavigationFragment::class.java),
                mine to createFragment(ProfileFragment::class.java)
        )

        //底部导航
        bottomNavigationView.run {
            setOnNavigationItemSelectedListener { menuItem ->
                showFragment(menuItem.itemId)
                true
            }
            //重新被选中
            setOnNavigationItemReselectedListener { menuItem ->
                val fragment = fragments[menuItem.itemId]
                if (fragment is ScrollToTop) {
                    fragment.scrollToTop()
                }
            }
        }

        //第一次加载时候 默认选择首页
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = home
            showFragment(home)
        }
    }

    fun animateBottomNavigationView(show: Boolean) {
        if (currentBottomNavigationStatus == show) return
        if (bottomNavigationViewAnimator != null) {
            bottomNavigationViewAnimator?.cancel()
            bottomNavigationView.clearAnimation()
        }
        currentBottomNavigationStatus = show
        //隐藏和显示动画
        val targetY = if (show) 0F else bottomNavigationView.measuredHeight.toFloat()
        val duration = if (show) 225L else 175L
        bottomNavigationViewAnimator = bottomNavigationView.animate()
                .translationY(targetY)
                .setDuration(duration)
                .setInterpolator(LINEAR_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        bottomNavigationViewAnimator = null
                    }
                })
    }

    /**
     * 显示fragment
     * 开启fragment实务后一定要commit 哈哈
     */
    private fun showFragment(menuItemId: Int) {
        val currentFragment = supportFragmentManager.fragments.find {
            it.isVisible && it in fragments.values
        }
        val targetFragment = fragments[menuItemId]
        supportFragmentManager.beginTransaction().apply {
            currentFragment?.let { if (it.isVisible) hide(it) }
            targetFragment?.let { if (it.isAdded) show(it) else add(R.id.fl, it) }
        }.commit()
    }


    private fun createFragment(clazz: Class<out Fragment>): Fragment {
        var fragment = supportFragmentManager.fragments.find { it.javaClass == clazz }
        if (fragment == null) {
            fragment = when (clazz) {
                HomeFragment::class.java -> HomeFragment.newInstance()
                SystemFragment::class.java -> SystemFragment.newInstance()
                DiscoveryFragment::class.java -> DiscoveryFragment.newInstance()
                NavigationFragment::class.java -> NavigationFragment.newInstance()
                ProfileFragment::class.java -> ProfileFragment.newInstance()
                else -> throw IllegalArgumentException("argument ${clazz.simpleName} is illegal")
            }
        }
        return fragment
    }

    /**
     * 在MainActivity中按一次backPress提示用户再按一次退出
     */
    override fun onBackPressed() {
        val currentTimeMills = System.currentTimeMillis()
        if (currentTimeMills - previousTimeMillis < 2000) {
            super.onBackPressed()
        } else {
            showToast(R.string.press_again_to_exit)
            previousTimeMillis = currentTimeMills
        }
    }

    //清除动画
    override fun onDestroy() {
        bottomNavigationViewAnimator?.cancel()
        bottomNavigationView.clearAnimation()
        bottomNavigationViewAnimator == null
        super.onDestroy()

    }

}


/**
 * 首页：热门文章、最新项目、广场、项目分类、公众号
 * 体系：体系
 * 发现：Banner、搜索热词、常用网站
 * 导航：导航
 * 我的：登录、注册、积分排行、我的积分、我的分享、稍后阅读、我的收藏、浏览历史、关于作者、开源项目、系统设置
 * 详情：文章详情（收藏、分享、浏览器打开、复制链接、刷新页面）
 * 搜索：搜索历史、热门搜索
 * 设置：日夜间模式、调整亮度、字体大小、清除缓存、检查版本、关于玩安卓、退出登录
 */
