package com.android.blogandroid.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.android.blogandroid.common.dialog.ProgressDialogFragment

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : activity 基类，控制界面加载弹框 Dialog
 *     version: 1.0
 */
abstract class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialogFragment: ProgressDialogFragment

    open fun layoutRes() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())
    }

    /**
     * 显示加载(旋转)对话框
     */
    fun showProgressDialog(@StringRes message: Int) {
        if (!this::progressDialogFragment.isInitialized) {
            progressDialogFragment = ProgressDialogFragment.newInstance()
        }
        if (!progressDialogFragment.isAdded) {
            progressDialogFragment.show(supportFragmentManager, message, false)
        }
    }

    /**
     * 隐藏对话框
     */
    fun dismissProgressDialog() {
        if (this::progressDialogFragment.isInitialized && progressDialogFragment.isVisible) {
            progressDialogFragment.dismissAllowingStateLoss()
        }
    }

}