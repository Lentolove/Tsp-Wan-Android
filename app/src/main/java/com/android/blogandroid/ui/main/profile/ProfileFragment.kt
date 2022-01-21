package com.android.blogandroid.ui.main.profile

import android.annotation.SuppressLint
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseFragment
import com.android.blogandroid.base.BaseVmFragment
import com.android.blogandroid.common.ScrollToTop
import com.android.blogandroid.common.bus.Bus
import com.android.blogandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.store.UserInfoStore
import com.android.blogandroid.model.store.isLogin
import com.android.blogandroid.ui.collection.CollectionActivity
import com.android.blogandroid.ui.detail.DetailActivity
import com.android.blogandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.android.blogandroid.ui.history.HistoryActivity
import com.android.blogandroid.ui.setting.SettingActivity
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : 我的界面
 *     version: 1.0
 */
class ProfileFragment : BaseVmFragment<ProfileViewModel>() {

    override fun layoutRes() = R.layout.fragment_profile

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun viewModelClazz() = ProfileViewModel::class.java

    override fun initView() {
        //点击事件
        clHeader.setOnClickListener {
            //todo 检查是否登录
            checkLogin()
        }
        //浏览历史
        llHistory.setOnClickListener {
            ActivityHelper.start(HistoryActivity::class.java)
        }
        //我的收藏
        llMyCollect.setOnClickListener {
            checkLogin { ActivityHelper.start(CollectionActivity::class.java) }
        }

        //关于作者
        llAboutAuthor.setOnClickListener {
            ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(
                            PARAM_ARTICLE to Article(
                                    title = getString(R.string.my_about_author),
                                    link = "https://github.com/Lentolove"
                            )
                    )
            )
        }

        //系统设置
        llSetting.setOnClickListener { ActivityHelper.start(SettingActivity::class.java) }

        //刷新UI
        updateUi()
    }

    override fun observe() {
        super.observe()
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, Observer {
            //登录状态监听
            updateUi()
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        val isLogin = isLogin()
        val userInfo = UserInfoStore.getUserInfo()
        tvLoginRegister.isGone = isLogin
        tvNickName.isVisible = isLogin
        tvId.isVisible = isLogin
        if (isLogin && userInfo != null) {
            tvNickName.text = userInfo.nickname
            tvId.text = "ID: ${userInfo.id}"
        }
    }

}