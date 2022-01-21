package com.android.blogandroid.ui.setting

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmActivity
import com.android.blogandroid.common.adapter.SeekBarChangeListenerAdapter
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.ext.setNavigationBarColor
import com.android.blogandroid.ext.showToast
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.model.store.SettingsStore
import com.android.blogandroid.model.store.isLogin
import com.android.blogandroid.ui.detail.DetailActivity
import com.android.blogandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.android.blogandroid.ui.login.LoginActivity
import com.android.blogandroid.util.clearCache
import com.android.blogandroid.util.getCacheSize
import com.android.blogandroid.util.isNightMode
import com.android.blogandroid.util.setNightMode
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.view_change_text_zoom.view.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/22
 *     desc   : 应用设置界面
 *     version: 1.0
 */
class SettingActivity : BaseVmActivity<SettingViewModel>() {

    override fun viewModelClass() = SettingViewModel::class.java

    override fun layoutRes() = R.layout.activity_setting


    override fun initView() {
        //设置状态栏颜色
        setNavigationBarColor(getColor(R.color.bgColorSecondary))
        //夜间模式按钮
        scDayNight.isChecked = isNightMode(this)
        scDayNight.setOnCheckedChangeListener { _, checked ->
            setNightMode(checked)
            SettingsStore.setNightMode(checked)
        }
        //设置字体大小
        tvFontSize.text = "${SettingsStore.getWebTextZoom()}%"
        llFontSize.setOnClickListener {
            setFontSize()
        }

        //返回按钮
        ivBack.setOnClickListener { ActivityHelper.finish(SettingActivity::class.java) }


        //清楚缓存按钮
        llClearCache.setOnClickListener {
            //弹窗确认
            AlertDialog.Builder(this)
                    .setMessage(R.string.confirm_clear_cache)
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        clearCache(this)
                        tvClearCache.text = getCacheSize(this)
                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .show()
        }
        llCheckVersion.setOnClickListener {
            showToast(getString(R.string.stay_tuned))
        }
        //关于我们
        llAboutUs.setOnClickListener {
            ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(
                            PARAM_ARTICLE to Article(
                                    title = getString(R.string.abount_us),
                                    link = "https://github.com/Lentolove"
                            )
                    )
            )
        }
        //根据登录状态判断该按钮是否显示
        tvLogout.isVisible = isLogin()

        //退出登录
        tvLogout.setOnClickListener {
            AlertDialog.Builder(this)
                    .setMessage(R.string.confirm_logout)
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        mViewModel.logout()
                        ActivityHelper.start(LoginActivity::class.java)
                        ActivityHelper.finish(SettingActivity::class.java)
                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .show()
        }

    }


    /**
     * 设置网页界面字体大小
     */
    private fun setFontSize() {
        val textZoom = SettingsStore.getWebTextZoom()
        var tempTextZoom = textZoom
        //设置弹窗
        AlertDialog.Builder(this)
                .setTitle(R.string.font_size)
                .setView(LayoutInflater.from(this).inflate(R.layout.view_change_text_zoom, null).apply {
                    this.dialogSeekBar?.progress = tempTextZoom - 50
                    this.dialogSeekBar?.setOnSeekBarChangeListener(SeekBarChangeListenerAdapter(
                            onProgressChanged = { _, progress, _ ->
                                tempTextZoom = 50 + progress
                                tvFontSize.text = "$tempTextZoom%"
                            }
                    ))
                })
                .setNegativeButton(R.string.cancel) { _, _ -> tvFontSize.text = "$textZoom%" }
                .setPositiveButton(R.string.confirm) { _, _ -> SettingsStore.setWebTextZoom(tempTextZoom) }
                .show()
    }
}
