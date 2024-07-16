package com.cuongngo.core_project.ui.search_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.FormRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFormViewModel(private val formRepository: FormRepository) : BaseViewModel() {

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

    private val _request = MutableLiveData<BaseResult<Long>>()
    val request: LiveData<BaseResult<Long>> = _request

    private val _requestId = MutableLiveData<BaseResult<Long>>()
    val requestId: LiveData<BaseResult<Long>> = _requestId

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
                _request.postValue(formRepository.insertRequest(requestEntity))
            }
        }
    }

}