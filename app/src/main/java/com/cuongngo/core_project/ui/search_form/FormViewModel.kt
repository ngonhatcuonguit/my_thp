package com.cuongngo.core_project.ui.search_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.FormResponse
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.FormRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormViewModel(private val formRepository: FormRepository) : BaseViewModel() {

    private val _allForm = MutableLiveData<BaseResult<List<FormEntity>>>()
    val allForm: LiveData<BaseResult<List<FormEntity>>> = _allForm

    private val _searchForms = MutableLiveData<BaseResult<List<FormEntity>>>()
    val searchForms: LiveData<BaseResult<List<FormEntity>>> = _searchForms

    private val _form = MutableLiveData<BaseResult<FormEntity>>()
    val form: LiveData<BaseResult<FormEntity>> = _form

    private val _formId = MutableLiveData<BaseResult<Long>>()
    val formId: LiveData<BaseResult<Long>> = _formId

    private val _formUnit = MutableLiveData<BaseResult<Unit>>()
    val formUnit: LiveData<BaseResult<Unit>> = _formUnit

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

    private val _upsertListFormToLocal = MutableLiveData<BaseResult<Unit>>()
    val upsertListFormToLocal: LiveData<BaseResult<Unit>> = _upsertListFormToLocal

    private val _checkCountRecord = MutableLiveData<BaseResult<Int>>()
    val checkCountRecord: LiveData<BaseResult<Int>> = _checkCountRecord


    //-----remote----
    private val _listFormRemote = MutableLiveData<BaseResult<FormResponse>>()
    val listFormRemote: LiveData<BaseResult<FormResponse>> = _listFormRemote


    var category = ""
    var edtSheetName = ""

//    init {
//        getAllForm()
//    }

    //----------local---------

    fun getAllForm() {
        _allForm.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _allForm.postValue(formRepository.getAllForm())
            }
        }
    }

    fun searchForms(keyword: String) {
        _searchForms.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _searchForms.postValue(formRepository.searchForms(keyword))
            }
        }
    }
    fun getCountRecord() {
        _checkCountRecord.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _checkCountRecord.postValue(formRepository.getFormCountLocal())
            }
        }
    }

    fun upsertListForm(listForm: List<FormEntity>){
        _upsertListFormToLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _upsertListFormToLocal.postValue(formRepository.upsertListForm(listForm))
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
                _formUnit.postValue(formRepository.updateForm(formEntity))
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


    //----------remote----------

    fun getListForm(isGetAll: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _listFormRemote.postValue(formRepository.getListForm(isGetAll))
            }
        }
    }


}