package com.cuongngo.core_project.ui.search_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Sheet
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.FormRepository
import com.cuongngo.core_project.services.repository.RequestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormViewModel(private val formRepository: FormRepository, private val requestRepository: RequestRepository) : BaseViewModel() {

    private val _allForm = MutableLiveData<BaseResult<List<FormEntity>>>()
    val allForm: LiveData<BaseResult<List<FormEntity>>> = _allForm

    private val _form = MutableLiveData<BaseResult<FormEntity>>()
    val form: LiveData<BaseResult<FormEntity>> = _form

    private val _formId = MutableLiveData<BaseResult<Long>>()
    val formId: LiveData<BaseResult<Long>> = _formId

    private val _formInt = MutableLiveData<BaseResult<Int>>()
    val formInt: LiveData<BaseResult<Int>> = _formInt


    private val _allRequest = MutableLiveData<BaseResult<List<RequestEntity>>>()
    val allRequest: LiveData<BaseResult<List<RequestEntity>>> = _allRequest

    private val _listRequest = MutableLiveData<BaseResult<List<RequestEntity>>>()
    val listRequest: LiveData<BaseResult<List<RequestEntity>>> = _listRequest

    private val _request = MutableLiveData<BaseResult<RequestEntity>>()
    val request: LiveData<BaseResult<RequestEntity>> = _request

    private val _requestUpdate = MutableLiveData<BaseResult<Unit>>()
    val requestUpdate: LiveData<BaseResult<Unit>> = _requestUpdate

    private val _requestId = MutableLiveData<BaseResult<Long>>()
    val requestId: LiveData<BaseResult<Long>> = _requestId

    var category = ""

//    init {
//        getAllForm()
//    }

    fun getAllForm() {
        _allForm.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _allForm.postValue(formRepository.getAllForm())
            }
        }
    }
    fun getFormById(id: String) {
        _form.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _form.postValue(formRepository.getFormById(id))
            }
        }
    }

    fun getFormByTitle(title: String) {
        _form.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _form.postValue(formRepository.getFormByTitle(title))
            }
        }
    }

    fun getFormByCode(code: String) {
        _form.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _form.postValue(formRepository.getFormByCode(code))
            }
        }
    }

    fun updateForm(formEntity: FormEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _formInt.postValue(formRepository.updateForm(formEntity))
            }
        }
    }

    fun upsertForm(formEntity: FormEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _formId.postValue(formRepository.upsertForm(formEntity))
            }
        }
    }

    fun insertForm(formEntity: FormEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _formId.postValue(formRepository.insertForm(formEntity))
            }
        }
    }
//    fun deleteForm(formEntity: FormEntity) {
//        viewModelScope.launch {
//            _formInt.postValue(formRepository.deleteForm(formEntity))
//        }
//    }
//    fun deleteFormById(id: String) {
//        viewModelScope.launch {
//            _form.postValue(formRepository.deleteFormById(id))
//        }
//    }

    fun insertRequest(requestEntity: RequestEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _requestId.postValue(formRepository.insertRequest(requestEntity))
            }
        }
    }
    fun upsertRequest(requestEntity: RequestEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _requestId.postValue(formRepository.upsertRequest(requestEntity))
            }
        }
    }
    fun getRequestByID(requestID: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _request.postValue(formRepository.getRequestByID(requestID))
            }
        }
    }
    fun getRequestByCode(requestCode: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _request.postValue(formRepository.getRequestByCode(requestCode))
            }
        }
    }
    fun getAllRequest() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _allRequest.postValue(formRepository.getAllRequest())
            }
        }
    }

    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
    fun updateListSheet(requestID: Long, listSheet: List<Sheet>?){
        _requestUpdate.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _requestUpdate.postValue(requestRepository.updateListSheet(requestID = requestID, listSheet = listSheet, currentTime = getCurrentTimestamp()))
            }
        }
    }

}