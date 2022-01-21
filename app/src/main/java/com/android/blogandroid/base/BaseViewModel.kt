package com.android.blogandroid.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.android.blogandroid.App
import com.android.blogandroid.R
import com.android.blogandroid.ext.showToast
import com.android.blogandroid.model.api.ApiException
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 *     author : shengping.tian
 *     time   : 2020/12/12
 *     desc   : viewModel 基类
 *     version: 1.0
 */

typealias Block<T> = suspend (CoroutineScope) -> T
typealias Error = suspend (Exception) -> Unit
typealias Cancel = suspend (Exception) -> Unit

open class BaseViewModel : ViewModel() {

    //全局是否登录状态liveData
    val loginStatesInvalid: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 创建并执行协程
     */
    protected fun launch(
        block: Block<Unit>,
        error: Error? = null,
        cancel: Cancel? = null,
        showErrorMessage: Boolean = true
    ): Job {
        return viewModelScope.launch {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        onError(e, showErrorMessage)
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    /**
     * 创建并执行协程
     * Deferred value is a non-blocking cancellable future &mdash; it is a [Job] with a result.
     */
    protected fun <T> async(block: Block<T>): Deferred<T> {
        return viewModelScope.async { block.invoke(this) }
    }

    /**
     * 取消协程
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCancelled){
            job.cancel()
        }
    }

    /**
     * 统一错误处理
     */
    private fun onError(e: Exception, showErrorToast: Boolean) {
        when (e) {
            is ApiException -> {
                when (e.code) {
                    -1001 -> {
                        //todo 登录失效，清楚用户信息，清楚cookie/token等

                        loginStatesInvalid.value = true
                    }
                    //其他 api 错误
                    -1 -> if (showErrorToast) App.instance.showToast(e.message)
                    //其它未知错误
                    else -> if (showErrorToast) App.instance.showToast(e.message)
                }
            }
            //网络请求错误
            is ConnectException,
            is SocketTimeoutException,
            is UnknownHostException,
            is HttpException,
            is SSLHandshakeException -> if (showErrorToast) App.instance.showToast(R.string.network_request_failed)
            //数据解析错误
            is JsonDataException,
            is JsonEncodingException -> if (showErrorToast) App.instance.showToast(R.string.api_data_parse_error)
            else -> if (showErrorToast) App.instance.showToast(e.message ?: return)

        }
    }

}