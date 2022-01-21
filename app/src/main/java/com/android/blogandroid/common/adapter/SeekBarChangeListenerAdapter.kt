package com.android.blogandroid.common.adapter

import android.widget.SeekBar

/**
 *     author : shengping.tian
 *     time   : 2020/12/22
 *     desc   : 只实现想用的方法
 *     version: 1.0
 */
class SeekBarChangeListenerAdapter(
        private val onProgressChanged: ((seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit)? = null,
        private val onStartTrackingTouch: ((seekBar: SeekBar?) -> Unit)? = null,
        private val onStopTrackingTouch: ((seekBar: SeekBar?) -> Unit)? = null
) : SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onProgressChanged?.invoke(seekBar,progress,fromUser)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        onStartTrackingTouch?.invoke(seekBar)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        onStopTrackingTouch?.invoke(seekBar)
    }
}