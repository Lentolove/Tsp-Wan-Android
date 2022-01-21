package com.android.blogandroid.ui.main.system.category

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.blogandroid.App
import com.android.blogandroid.R
import com.android.blogandroid.ext.dpToPx
import com.android.blogandroid.model.bean.Category
import com.android.blogandroid.ui.main.system.SystemFragment
import com.android.blogandroid.util.getScreenHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_system_category.*

/**
 *     author : shengping.tian
 *     time   : 2020/12/15
 *     desc   : 体系界面底部弹窗
 *     version: 1.0
 */
class SystemCategoryFragment : BottomSheetDialogFragment() {

    companion object {
        const val CATEGORY_LIST = "categoryList"
        fun newInstance(categoryList: ArrayList<Category>): SystemCategoryFragment {
            return SystemCategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_LIST, categoryList)
                }
            }
        }
    }

    private var height: Int? = null
    private var behavior: BottomSheetBehavior<View>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_system_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryList: ArrayList<Category> = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        val checked = (parentFragment as SystemFragment).getCurrentChecked()
        SystemCategoryAdapter(R.layout.item_system_category, categoryList, checked).also {
            it.onCheckedListener = { position ->
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                view.postDelayed({
                    (parentFragment as SystemFragment).checked(position)
                },300)
            }
            recyclerView.adapter = it
        }
        view.post {
            (recyclerView.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(checked.first, 0)
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        height?.let { behavior?.peekHeight = it }
        dialog?.window?.let {
            it.setGravity(Gravity.BOTTOM)
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, height ?: ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }


    fun show(manager: FragmentManager, height: Int? = null) {
        this.height = height ?: getScreenHeight(App.instance) - 48.dpToPx().toInt()
        if (!this.isAdded) {
            super.show(manager, "SystemCategoryFragment")
        }
    }
}