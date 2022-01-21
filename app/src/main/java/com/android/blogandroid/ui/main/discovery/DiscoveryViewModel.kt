package com.android.blogandroid.ui.main.discovery

import androidx.lifecycle.MutableLiveData
import com.android.blogandroid.base.BaseViewModel
import com.android.blogandroid.model.bean.Banner
import com.android.blogandroid.model.bean.Frequently
import com.android.blogandroid.model.bean.HotWord


/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   :
 *     version: 1.0
 */
class DiscoveryViewModel : BaseViewModel() {

    private val discoveryRepository by lazy { DiscoveryRepository() }

    val banners = MutableLiveData<List<Banner>>()
    val hotWords = MutableLiveData<List<HotWord>>()
    val frequentlyList = MutableLiveData<List<Frequently>>()

    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()


    fun getData() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false
                banners.value = discoveryRepository.getBanners()
                hotWords.value = discoveryRepository.getHotWords()
                frequentlyList.value = discoveryRepository.getFrequentlyWebsites()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value =
                    banners.value.isNullOrEmpty() && hotWords.value.isNullOrEmpty() && frequentlyList.value.isNullOrEmpty()
            }
        )
    }

}