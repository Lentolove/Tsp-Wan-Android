package com.android.blogandroid.util

import android.app.Activity
import androidx.core.app.ShareCompat
import com.android.blogandroid.R

/**
 *     author : shengping.tian
 *     time   : 2020/12/21
 *     desc   : 分享界面
 *     version: 1.0
 */

fun share(
        activity: Activity,
        title: String? = activity.getString(R.string.app_name),
        content: String?
) {
    ShareCompat.IntentBuilder.from(activity)
            .setType("text/plain")
            .setSubject(title)
            .setText(content)
            .setChooserTitle(title)
            .startChooser()
}