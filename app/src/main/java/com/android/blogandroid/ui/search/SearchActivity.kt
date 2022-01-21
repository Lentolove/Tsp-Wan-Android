package com.android.blogandroid.ui.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseActivity
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.ext.hideSoftInput
import com.android.blogandroid.ui.search.history.SearchHistoryFragment
import com.android.blogandroid.ui.search.result.SearchResultFragment
import kotlinx.android.synthetic.main.activity_search.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/17
 *     desc   : 搜索界面 ：performClick
 *     version: 1.0
 */
class SearchActivity : BaseActivity() {

    companion object {
        //其他界面跳转过来的搜索词key
        const val SEARCH_WORD = "search_word"
    }

    override fun layoutRes() = R.layout.activity_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val historyFragment = SearchHistoryFragment.newInstance()
        val resultFragment = SearchResultFragment.newInstance()
        //开启fragment事物,显示搜索历史界面
        supportFragmentManager.beginTransaction()
                .add(R.id.flContainer, historyFragment)
                .add(R.id.flContainer, resultFragment)
                .show(historyFragment)
                .hide(resultFragment)
                .commit()
        //返回按钮事件,如果当前fragment是historyFragment,则返回是直接关闭SearchActivity，否则隐藏resultFragment，显示historyFragment
        ivBack.setOnClickListener {
            if (resultFragment.isVisible) {
                supportFragmentManager.beginTransaction()
                        .hide(resultFragment)
                        .commit()
            } else {
                ActivityHelper.finish(SearchActivity::class.java)
            }
        }
        //搜索按钮 如果输入词为kong 则不执行操作
        ivDone.setOnClickListener {
            val searchWord = acetInput.text.toString()
            if (searchWord.isEmpty()) return@setOnClickListener
            //隐藏软键盘
            it.hideSoftInput()
            historyFragment.addSearchHistory(searchWord)
            resultFragment.doSearch(searchWord)
            supportFragmentManager.beginTransaction()
                    .show(resultFragment)
                    .commit()
        }
        //输入框中的清除按钮
        acetInput.run {
            addTextChangedListener(afterTextChanged = {
                //有文字,则显示清楚按钮图标
                ivClearSearch.isGone = it.isNullOrEmpty()
            })
            //软键盘输入完成事件
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ivDone.performClick()
                    true
                } else {
                    false
                }
            }
        }
        ivClearSearch.setOnClickListener { acetInput.setText("") }

        // 其它界面跳转搜索 todo 记录
        intent.getStringExtra(SEARCH_WORD)?.let { window.decorView.post { fillSearchInput(it) } }
    }

    fun fillSearchInput(keywords: String) {
        acetInput.setText(keywords)
        acetInput.setSelection(keywords.length)
        ivDone.performClick()
    }

    override fun onBackPressed() {
        ivBack.performClick()
    }

}