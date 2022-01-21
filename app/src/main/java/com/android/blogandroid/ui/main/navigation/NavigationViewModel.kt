package com.android.blogandroid.ui.main.navigation

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.model.bean.Navigation

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
class NavigationViewModel : BaseViewModel() {

    private val navigationRepository by lazy { NavigationRepository() }

    val navigationList = MutableLiveData<List<Navigation>>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getNavigationList() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false
                navigationList.value = navigationRepository.getNavigation()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = navigationList.value.isNullOrEmpty()
            }
        )
    }


}