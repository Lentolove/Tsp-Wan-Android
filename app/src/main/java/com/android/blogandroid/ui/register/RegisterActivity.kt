package com.android.blogandroid.ui.register

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.observe
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmActivity
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/18
 *     desc   : 注册界面
 *     version: 1.0
 */
class RegisterActivity : BaseVmActivity<RegisterViewModel>() {

    override fun layoutRes() = R.layout.activity_register

    override fun viewModelClass() = RegisterViewModel::class.java


    override fun initView() {
        ivBack.setOnClickListener { ActivityHelper.finish(RegisterActivity::class.java) }
        //确认密码软键盘后的go 可以直接转注册按钮
        tietConfirmPssword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                btnRegister.performClick()
                true
            } else {
                false
            }
        }
        btnRegister.setOnClickListener {
            tilAccount.error = ""
            tilPassword.error = ""
            tilConfirmPssword.error = ""
            val account = tietAccount.text.toString()
            val password = tietPassword.text.toString()
            val confirmPassword = tietConfirmPssword.text.toString()
            when {
                account.isEmpty() -> tilAccount.error = getString(R.string.account_can_not_be_empty)
                account.length < 3 -> tilAccount.error =
                        getString(R.string.account_length_over_three)
                password.isEmpty() -> tilPassword.error =
                        getString(R.string.password_can_not_be_empty)
                password.length < 6 -> tilPassword.error =
                        getString(R.string.password_length_over_six)
                confirmPassword.isEmpty() -> tilConfirmPssword.error =
                        getString(R.string.confirm_password_can_not_be_empty)
                password != confirmPassword -> tilConfirmPssword.error =
                        getString(R.string.two_password_are_inconsistent)
                else -> mViewModel.register(account, password, confirmPassword)
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            //正在注册
            submitting.observe(this@RegisterActivity){
                if (it) showProgressDialog(R.string.registerring) else dismissProgressDialog()
            }
            //注册成功后直接结束登录和注册界面
            registerResult.observe(this@RegisterActivity){
                if (it){
                    ActivityHelper.finish(LoginActivity::class.java,RegisterActivity::class.java)
                }
            }
        }
    }

}