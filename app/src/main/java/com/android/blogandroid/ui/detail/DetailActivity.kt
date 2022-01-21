package com.android.blogandroid.ui.detail

import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmActivity
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_COLLECT_UPDATED
import com.android.blogandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.common.core.whiteHostList
import com.android.blogandroid.ext.htmlToSpanned
import com.android.blogandroid.ext.setBrightness
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.store.SettingsStore
import com.android.blogandroid.util.isNightMode
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import kotlinx.android.synthetic.main.activity_detail.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/14
 *     desc   : 详情界面
 *     version: 1.0
 */
class DetailActivity : BaseVmActivity<DetailViewModel>() {

    companion object {
        const val PARAM_ARTICLE = "param_article"
        const val TAG = "DetailActivity"
    }

    private lateinit var article: Article

    private var agentWeb: AgentWeb? = null

    override fun layoutRes() = R.layout.activity_detail

    override fun viewModelClass() = DetailViewModel::class.java

    override fun initView() {
        //跳转传过来的数据
        article = intent?.getParcelableExtra(PARAM_ARTICLE) ?: return
        setDetailTitle(article.title.htmlToSpanned())
        //返回按钮事件
        ivBack.setOnClickListener {
            ActivityHelper.finish(DetailActivity::class.java)
        }
        //右侧工具栏按钮事件，从底部弹出选择按钮
        ivMore.setOnClickListener {
            ActionFragment.newInstance(article).show(supportFragmentManager)
        }
        if (isNightMode(this)) {
            setBrightness(0.08f)
        }
    }

    override fun initData() {
        if (article.id != 0L) {
            mViewModel.saveReadHistory(article)
        }
        //web页面展示相关 : https://www.jianshu.com/p/c80da1c41af7
        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(webContainer, ViewGroup.LayoutParams(-1, -1))
            .useDefaultIndicator(getColor(R.color.textColorPrimary), 2)
            .interceptUnkownUrl()
            .setMainFrameErrorView(R.layout.include_reload, R.id.btnReload)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
            .setWebChromeClient(webChromeClient)
            .setWebViewClient(webViewClient)
            .createAgentWeb()
            .ready()
            .get()
        agentWeb?.webCreator?.webView?.run {
            overScrollMode = WebView.OVER_SCROLL_NEVER
            settings.run {
                javaScriptCanOpenWindowsAutomatically = false
                loadsImagesAutomatically = true
                useWideViewPort = true
                loadWithOverviewMode = true
                textZoom = SettingsStore.getWebTextZoom()
            }
        }
        agentWeb?.urlLoader?.loadUrl(article.link)
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            setDetailTitle(title, true)
            super.onReceivedTitle(view, title)
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            Log.d(TAG, "onConsoleMessage: ${consoleMessage?.message()}")
            return super.onConsoleMessage(consoleMessage)
        }
    }

    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return !whiteHostList().contains(request?.url?.host)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            view?.loadUrl(customJs(url))
        }
    }

    fun changeCollect() {
        if (article.collect) {
            mViewModel.unCollect(article.id)
        } else {
            mViewModel.collect(article.id)
        }
    }


    /**
     * 加载js，去掉掘金、简书、CSDN等H5页面的Title、底部操作栏，以及部分广告
     */
    private fun customJs(url: String? = article.link): String {
        val js = StringBuilder()
        js.append("javascript:(function(){")
        when (Uri.parse(url).host) {
            "juejin.im", "juejin.cn" -> {
                js.append("var followButtonList = document.getElementsByClassName('follow-button');")
                js.append("if(followButtonList&&followButtonList.length){followButtonList[0].parentNode.removeChild(followButtonList[0])}")
                js.append("var articleBannerList = document.getElementsByClassName('article-banner');")
                js.append("if(articleBannerList&&articleBannerList.length){articleBannerList[0].parentNode.removeChild(articleBannerList[0])}")
                js.append("var subscribeBtnList = document.getElementsByClassName('subscribe-btn');")
                js.append("if(subscribeBtnList&&subscribeBtnList.length){subscribeBtnList[0].parentNode.removeChild(subscribeBtnList[0])}")
                js.append("var headerList = document.getElementsByClassName('main-header-box');")
                js.append("if(headerList&&headerList.length){headerList[0].parentNode.removeChild(headerList[0])}")
                js.append("var openAppList = document.getElementsByClassName('open-in-app');")
                js.append("if(openAppList&&openAppList.length){openAppList[0].parentNode.removeChild(openAppList[0])}")
                js.append("var actionBox = document.getElementsByClassName('action-box');")
                js.append("if(actionBox&&actionBox.length){actionBox[0].parentNode.removeChild(actionBox[0])}")
                js.append("var actionBarList = document.getElementsByClassName('action-bar');")
                js.append("if(actionBarList&&actionBarList.length){actionBarList[0].parentNode.removeChild(actionBarList[0])}")
                js.append("var columnViewList = document.getElementsByClassName('column-view');")
                js.append("if(columnViewList&&columnViewList.length){columnViewList[0].style.margin = '0px'}")
            }
            "www.jianshu.com" -> {
                js.append("var badgeItemList = document.getElementsByClassName('badge-item');")
                js.append("if(badgeItemList&&badgeItemList.length){badgeItemList[0].parentNode.removeChild(badgeItemList[0])}")
                js.append("var appOpenList = document.getElementsByClassName('app-open');")
                js.append("if(appOpenList&&appOpenList.length){appOpenList[0].parentNode.removeChild(appOpenList[0])}")
                js.append("var jianshuHeader = document.getElementById('jianshu-header');")
                js.append("if(jianshuHeader){jianshuHeader.parentNode.removeChild(jianshuHeader)}")
                js.append("var commentMain = document.getElementById('comment-main');")
                js.append("if(commentMain){commentMain.parentNode.removeChild(commentMain)}")
                js.append("var footer = document.getElementById('footer');")
                js.append("if(footer){footer.parentNode.removeChild(footer)}")
                js.append("var revealAd = document.getElementById('reveal-ad');")
                js.append("if(revealAd){revealAd.parentNode.removeChild(revealAd)}")
                js.append("var headerShimList = document.getElementsByClassName('header-shim');")
                js.append("if(headerShimList&&headerShimList.length){headerShimList[0].parentNode.removeChild(headerShimList[0])}")
                js.append("var fubiaoList = document.getElementsByClassName('fubiao-dialog');")
                js.append("if(fubiaoList&&fubiaoList.length){fubiaoList[0].parentNode.removeChild(fubiaoList[0])}")
                js.append("var ads = document.getElementsByClassName('note-comment-above-ad-wrap');")
                js.append("if(ads&&ads.length){ads[0].parentNode.removeChild(ads[0])}")
                js.append("var lazyShimList = document.getElementsByClassName('v-lazy-shim');")
                js.append("if(lazyShimList&&lazyShimList.length&&lazyShimList[0]){lazyShimList[0].parentNode.removeChild(lazyShimList[0])}")
                js.append("if(lazyShimList&&lazyShimList.length&&lazyShimList[1]){lazyShimList[1].parentNode.removeChild(lazyShimList[1])}")
                js.append("var callAppBtnList = document.getElementsByClassName('call-app-btn');")
                js.append("if(callAppBtnList&&callAppBtnList.length){callAppBtnList[0].parentNode.removeChild(callAppBtnList[0])}")
            }
            "blog.csdn.net" -> {
                js.append("var detailFollow = document.getElementById('detailFollow');")
                js.append("if(detailFollow){detailFollow.parentNode.removeChild(detailFollow)}")
                js.append("var csdnToolBar = document.getElementById('csdn-toolbar');")
                js.append("if(csdnToolBar){csdnToolBar.parentNode.removeChild(csdnToolBar)}")
                js.append("var csdnMain = document.getElementById('main');")
                js.append("if(csdnMain){csdnMain.style.margin='0px'}")
                js.append("var operate = document.getElementById('operate');")
                js.append("if(operate){operate.parentNode.removeChild(operate)}")
                js.append("var haveHeartCountList = document.getElementsByClassName('have-heart-count');")
                js.append("if(haveHeartCountList&&haveHeartCountList.length){haveHeartCountList[0].parentNode.removeChild(haveHeartCountList[0])}")
                js.append("var asideHeaderFixedList = document.getElementsByClassName('aside-header-fixed');")
                js.append("if(asideHeaderFixedList&&asideHeaderFixedList.length){asideHeaderFixedList[0].parentNode.removeChild(asideHeaderFixedList[0])}")
                js.append("var feedSignSpanList = document.getElementsByClassName('feed-Sign-span');")
                js.append("if(feedSignSpanList&&feedSignSpanList.length){feedSignSpanList[0].parentNode.removeChild(feedSignSpanList[0])}")
            }
        }
        js.append("})()")
        return js.toString()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (agentWeb?.handleKeyEvent(keyCode, event) == true) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }


    fun refreshPage() {
        agentWeb?.urlLoader?.reload()
    }

    override fun observe() {
        super.observe()
        mViewModel.collect.observe(this){
            onCollectStatusChanged(it)
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, this, Observer{
            mViewModel.updateCollectStatus(article.id)
        })
    }



    private fun onCollectStatusChanged(collect: Boolean) {
        if (article.collect != collect) {
            article.collect = collect
            // 收藏状态变化，通知其他更新
            Bus.post(USER_COLLECT_UPDATED, article.id to collect)
        }
    }

    /**
     * 设置详情界面的title
     */
    private fun setDetailTitle(title: CharSequence?, isSelected: Boolean = false) {
        tvTitle.text = title
        tvTitle.postDelayed({ tvTitle.isSelected = isSelected }, 500)
    }


}