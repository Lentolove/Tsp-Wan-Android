package com.android.blogandroid.ui.main.discovery

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmFragment
import com.android.blogandroid.common.ScrollToTop
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.bean.Banner
import com.android.blogandroid.ui.detail.DetailActivity
import com.android.blogandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.android.blogandroid.ui.main.MainActivity
import com.android.blogandroid.ui.search.SearchActivity
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_discovery.*
import kotlinx.android.synthetic.main.include_reload.*
import kotlinx.android.synthetic.main.item_navigation.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : 发现页面
 *     version: 1.0
 */
class DiscoveryFragment : BaseVmFragment<DiscoveryViewModel>(), ScrollToTop {

    override fun layoutRes() = R.layout.fragment_discovery

    override fun viewModelClazz() = DiscoveryViewModel::class.java

    companion object {
        fun newInstance() = DiscoveryFragment()
    }

    private lateinit var hotWordsAdapter: HotWordsAdapter


    override fun initView() {
        //todo 分享按钮事件 search 界面
        ivAdd.setOnClickListener {

        }
        ivSearch.setOnClickListener {
            ActivityHelper.start(SearchActivity::class.java)
        }
        //界面刷新 progressBar 属性设置
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.getData() }
        }

        hotWordsAdapter = HotWordsAdapter(R.layout.item_hot_word).apply {
            //点击热词 直接跳转到搜索界面
            setOnItemClickListener { _, _, position ->
                ActivityHelper.start(SearchActivity::class.java, mapOf(SearchActivity.SEARCH_WORD to data[position].name))
            }
            rvHotWord.adapter = this
        }
        btnReload.setOnClickListener { mViewModel.getData() }
        //底部按钮动画
        nestedScollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
        }
    }


    override fun observe() {
        super.observe()
        mViewModel.run {
            banners.observe(viewLifecycleOwner){
                //设置banner数据
                setupBanner(it)
            }
            hotWords.observe(viewLifecycleOwner){
                hotWordsAdapter.setList(it)
                tvHotWordTitle.isVisible = it.isNotEmpty()
            }
            frequentlyList.observe(viewLifecycleOwner){
                tagFlowLayout.adapter = FrequentlyTagAdapter(it)
                tagFlowLayout.setOnTagClickListener { _, position, _ ->
                    val frequently = it[position]
                    ActivityHelper.start(DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to Article(title = frequently.name,link = frequently.link)))
                    false
                }
                tvFrquently.isGone = it.isEmpty()
            }
            refreshStatus.observe(viewLifecycleOwner){
                swipeRefreshLayout.isRefreshing = it
            }
            reloadStatus.observe(viewLifecycleOwner){
                reloadView.isVisible = it
            }
        }
    }

    private fun setupBanner(banners:List<Banner>){
        bannerView.run {
            setBannerStyle(BannerConfig.NOT_INDICATOR)
            setImageLoader(BannerImageLoader(this@DiscoveryFragment))
            setImages(banners)
            setBannerAnimation(Transformer.BackgroundToForeground)
            start()
            setOnBannerListener {
                val banner = banners[it]
                ActivityHelper.start(
                        DetailActivity::class.java,
                        mapOf(PARAM_ARTICLE to Article(title = banner.title, link = banner.url))
                )
            }
        }
    }

    override fun initData() {
        mViewModel.getData()
    }

    override fun onResume() {
        super.onResume()
        //开启banner动画
        bannerView.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        bannerView.stopAutoPlay()
    }

    override fun scrollToTop() {
        nestedScollView?.smoothScrollTo(0, 0)
    }

}