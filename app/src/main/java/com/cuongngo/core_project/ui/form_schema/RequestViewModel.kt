package com.cuongngo.core_project.ui.form_schema

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.RequestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestViewModel(private val requestRepository: RequestRepository): BaseViewModel() {

    private val _allRequest = MutableLiveData<BaseResult<List<RequestEntity>>>()
    val allRequest: LiveData<BaseResult<List<RequestEntity>>> = _allRequest

    private val _request = MutableLiveData<BaseResult<RequestEntity>>()
    val request: LiveData<BaseResult<RequestEntity>> = _request

    private val _requestId = MutableLiveData<BaseResult<Long>>()
    val requestId: LiveData<BaseResult<Long>> = _requestId

    fun getRequestByName(name: String) {
        _request.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _request.postValue(requestRepository.getRequestByName(name))
            }
        }

    }

}