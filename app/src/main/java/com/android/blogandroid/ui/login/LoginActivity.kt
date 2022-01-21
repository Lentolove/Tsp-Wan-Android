package com.android.blogandroid.ui.login

import android.view.inputmethod.EditorInfo.IME_ACTION_GO
import androidx.lifecycle.observe
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmActivity
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   :
 *     version: 1.0
 */
class LoginActivity : BaseVmActivity<LoginViewModel>() {

    override fun layoutRes() = R.layout.activity_login

    override fun viewModelClass() = LoginViewModel::class.java

    override fun initView() {
        ivClose.setOnClickListener { ActivityHelper.finish(LoginActivity::class.java) }
        //转注册界面
        tvGoRegister.setOnClickListener {
            ActivityHelper.start(RegisterActivity::class.java)
        }
        tietPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == IME_ACTION_GO) {
                btnLogin.performClick()
                true
            } else {
                false
            }
        }
        btnLogin.setOnClickListener {
            tilAccount.error = ""
            tilPassword.error = ""
            val account = tietAccount.text.toString()
            val password = tietPassword.text.toString()
            when {
                account.isEmpty() -> tilAccount.error = getString(R.string.account_can_not_be_empty)
                password.isEmpty() -> tilPassword.error =
                        getString(R.string.password_can_not_be_empty)
                else -> mViewModel.login(account, password)
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            submitting.observe(this@LoginActivity) {
                if (it) showProgressDialog(R.string.logging_in) else dismissProgressDialog()
            }
            loginResult.observe(this@LoginActivity) {
                if (it) {
                    ActivityHelper.finish(LoginActivity::class.java)
                }
            }
        }
    }

}