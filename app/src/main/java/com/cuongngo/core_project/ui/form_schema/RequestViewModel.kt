package com.cuongngo.core_project.ui.form_schema

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Sheet
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.RequestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RequestViewModel(private val requestRepository: RequestRepository): BaseViewModel() {

    private val _allRequest = MutableLiveData<BaseResult<List<RequestEntity>>>()
    val allRequest: LiveData<BaseResult<List<RequestEntity>>> = _allRequest

    private val _request = MutableLiveData<BaseResult<RequestEntity>>()
    val request: LiveData<BaseResult<RequestEntity>> = _request

    private val _requestId = MutableLiveData<BaseResult<Long>>()
    val requestId: LiveData<BaseResult<Long>> = _requestId

    private val _requestUpdate = MutableLiveData<BaseResult<Unit>>()
    val requestUpdate: LiveData<BaseResult<Unit>> = _requestUpdate

    fun getRequestByName(name: String) {
        _request.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _request.postValue(requestRepository.getRequestByName(name))
            }
        }
    }
    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
    fun updateListSheet(requestID: Long, listSheet: List<Sheet>){
        _requestUpdate.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _requestUpdate.postValue(requestRepository.updateListSheet(requestID = requestID, listSheet = listSheet, currentTime = getCurrentTimestamp()))
            }
        }
    }

}