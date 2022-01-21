package com.android.blogandroid.ui.main.discovery

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.android.blogandroid.R
import com.android.blogandroid.common.core.load
import com.android.blogandroid.model.bean.Banner
import com.youth.banner.loader.ImageLoader

/**
 *     author : shengping.tian
 *     time   : 2020/12/16
 *     desc   : banner 图片加载器
 *     version: 1.0
 */
class BannerImageLoader(private val fragment: Fragment) : ImageLoader() {

    //用 Coil 代替 Glide加载图片
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        val imagePath = (path as Banner)?.imagePath
        imageView?.load(
            lifecycle = fragment.lifecycle,
            url = imagePath,
            placeholder = R.drawable.shape_bg_image_default
        )
    }
}