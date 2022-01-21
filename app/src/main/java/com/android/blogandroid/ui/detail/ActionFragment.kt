package com.android.blogandroid.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.android.blogandroid.R
import com.android.blogandroid.ext.copyTextIntoClipboard
import com.android.blogandroid.ext.openInExplorer
import com.android.blogandroid.ext.showToast
import com.android.blogandroid.model.bean.Article
import com.android.blogandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.android.blogandroid.util.share
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_detail_acitons.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   : 底部弹窗
 *     version: 1.0
 */
class ActionFragment :BottomSheetDialogFragment(){

    companion object {
        fun newInstance(article: Article): ActionFragment {
            return ActionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_ARTICLE, article)
                }
            }
        }
    }

    private var behavior: BottomSheetBehavior<View>? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_acitons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            val article = getParcelable<Article>(PARAM_ARTICLE) ?: return@run
            llCollect.visibility = if (article.id != 0L) View.VISIBLE else View.GONE
            ivCollect.isSelected = article.collect
            tvCollect.text = getString(if (article.collect) R.string.cancel_collect else R.string.add_collect)
            llCollect.setOnClickListener {
                val detailActivity = (activity as? DetailActivity) ?: return@setOnClickListener
                if (detailActivity.checkLogin()){
                    ivCollect.isSelected = !article.collect
                    detailActivity.changeCollect()
                    //隐藏底部弹窗
                    behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }else{
                    view.postDelayed({ dismiss() },300)
                }
            }
            //分享
            llShare.setOnClickListener {
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                share(
                        activity = activity ?: return@setOnClickListener,
                        content = article.title + article.link
                )
            }
            //浏览器打开
            llExplorer.setOnClickListener {
                openInExplorer(article.link)
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
            //复制链接
            llCopy.setOnClickListener {
                context?.copyTextIntoClipboard(article.link,article.title)
                context?.showToast(R.string.copy_success)
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
            llRefresh.setOnClickListener {
                (activity as? DetailActivity)?.refreshPage()
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
                .findViewById(com.google.android.material.R.id.design_bottom_sheet)
                ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun show(manager: FragmentManager) {
        if (!this.isAdded) {
            super.show(manager, "ActionFragment")
        }
    }
}