package com.cuongngo.my_thp.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cuongngo.my_thp.common.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {
    private var job: Job = Job()
    var ioContext: CoroutineContext = Dispatchers.IO + job

    private val _isLoading = SingleLiveEvent<Boolean>().apply { value = false }
    var isLoading: LiveData<Boolean> = _isLoading

    open fun onCreate() {}

    open fun onDestroy() {
        job.cancel()
    }

    open fun hideLoading() {
        _isLoading.postValue(false)
    }

    open fun showLoading() {
        _isLoading.postValue(true)
    }

    open fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}