package com.android.blogandroid.ui.history

import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.android.blogandroid.R
import com.android.blogandroid.base.BaseVmActivity
import com.android.blogandroid.common.core.ActivityHelper
import com.android.blogandroid.ui.detail.DetailActivity
import com.android.blogandroid.ui.main.home.ArticleAdapter
import kotlinx.android.synthetic.main.activity_history.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
class HistoryActivity : BaseVmActivity<HistoryViewModel>() {

    companion object {
        fun newInstance() = HistoryActivity()
    }

    private lateinit var mAdapter: ArticleAdapter

    override fun layoutRes() = R.layout.activity_history

    override fun viewModelClass() = HistoryViewModel::class.java

    override fun initView() {
        initAdapter()
        initListeners()
    }

    private fun initAdapter() {
        mAdapter = ArticleAdapter().also {
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.start(DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article))
            }
            //长按删除该条浏览记录
            it.setOnItemLongClickListener { _, _, position ->
                AlertDialog.Builder(this@HistoryActivity)
                        .setMessage(R.string.confirm_delete_history)
                        .setNegativeButton(R.string.cancel) { _, _ -> }
                        .setPositiveButton(R.string.confirm) { _, _ ->
                            mViewModel.deleteHistory(it.data[position])
                            mAdapter.removeAt(position)
                            this@HistoryActivity.emptyView.isVisible = it.data.isEmpty()
                        }.show()
                true
            }
            recyclerView.adapter = it
        }
    }

    private fun initListeners() {
        ivBack.setOnClickListener {
            ActivityHelper.finish(HistoryActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getData()
    }

    override fun observe() {
        super.observe()
        mViewModel.articleList.observe(this) {
            mAdapter.setList(it)
        }
        mViewModel.emptyStatus.observe(this) {
            emptyView.isVisible = it
        }
    }

}