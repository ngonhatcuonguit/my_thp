package com.cuongngo.core_project.ui.form_schema

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Body
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.FormRepository
import com.cuongngo.core_project.services.repository.RequestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RequestViewModel(
    private val requestRepository: RequestRepository,
    private val formRepository: FormRepository
) : BaseViewModel() {

    private val _allRequest = MutableLiveData<BaseResult<List<RequestEntity>>>()
    val allRequest: LiveData<BaseResult<List<RequestEntity>>> = _allRequest

    private val _request = MutableLiveData<BaseResult<RequestEntity>>()
    val request: LiveData<BaseResult<RequestEntity>> = _request

    private val _requestId = MutableLiveData<BaseResult<Long>>()
    val requestId: LiveData<BaseResult<Long>> = _requestId

    private val _requestUpdate = MutableLiveData<BaseResult<Unit>>()
    val requestUpdate: LiveData<BaseResult<Unit>> = _requestUpdate

    //form
    private val _form = MutableLiveData<BaseResult<FormEntity>>()
    val form: LiveData<BaseResult<FormEntity>> = _form

    private val _formId = MutableLiveData<BaseResult<Long>>()
    val formId: LiveData<BaseResult<Long>> = _formId

    private val _formUnit = MutableLiveData<BaseResult<Unit>>()
    val formUnit: LiveData<BaseResult<Unit>> = _formUnit

    //variable
    var formEntity: FormEntity? = null
    var requestEntity: RequestEntity? = null
    var edtSheetName: String? = null

    fun insertRequest(requestEntity: RequestEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _requestId.postValue(requestRepository.insertRequest(requestEntity))
            }
        }
    }

    fun upsertRequest(requestEntity: RequestEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _requestId.postValue(requestRepository.upsertRequest(requestEntity))
            }
        }
    }

    fun getRequestByID(requestID: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _request.postValue(requestRepository.getRequestByID(requestID))
            }
        }
    }

    fun getRequestByCode(requestCode: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _request.postValue(requestRepository.getRequestByCode(requestCode))
            }
        }
    }

    fun getAllRequest() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _allRequest.postValue(requestRepository.getAllRequest())
            }
        }
    }

    fun getFormByCode(code: String) {
        _form.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _form.postValue(formRepository.getFormByCode(code))
            }
        }
    }

    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun updateListSheet(requestID: Long, listBody: List<Body>) {
        _requestUpdate.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _requestUpdate.postValue(
                    requestRepository.updateListSheet(
                        requestID = requestID,
                        listBody = listBody,
                        currentTime = getCurrentTimestamp()
                    )
                )
            }
        }
    }

}