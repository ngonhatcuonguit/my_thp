package com.cuongngo.core_project.ui.search_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.FormSchemaEntity
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.FormRepository
import com.cuongngo.core_project.services.repository.FormSchemaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFormViewModel(private val formRepository: FormRepository, private val formSchemaRepository: FormSchemaRepository) : BaseViewModel() {

    private val _allForm = MutableLiveData<BaseResult<List<FormEntity>>>()
    val allForm: LiveData<BaseResult<List<FormEntity>>> = _allForm

    private val _form = MutableLiveData<BaseResult<FormEntity>>()
    val form: LiveData<BaseResult<FormEntity>> = _form

    private val _formId = MutableLiveData<BaseResult<Long>>()
    val formId: LiveData<BaseResult<Long>> = _formId

    private val _formInt = MutableLiveData<BaseResult<Int>>()
    val formInt: LiveData<BaseResult<Int>> = _formInt


    private val _allFormSchema = MutableLiveData<BaseResult<List<FormSchemaEntity>>>()
    val allFormSchema: LiveData<BaseResult<List<FormSchemaEntity>>> = _allFormSchema

    private val _formSchema = MutableLiveData<BaseResult<FormSchemaEntity>>()
    val formSchema: LiveData<BaseResult<FormSchemaEntity>> = _formSchema

    private val _formSchemaId = MutableLiveData<BaseResult<Long>>()
    val formSchemaId: LiveData<BaseResult<Long>> = _formSchemaId

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

    fun getFormSchemaByCode(code: String) {
        _formSchema.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _formSchema.postValue(formSchemaRepository.getFormSchemaByCode(code))
            }
        }

    }

    fun insertFormSchema(formSchemaEntity: FormSchemaEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _formSchemaId.postValue(formSchemaRepository.insertFormSchema(formSchemaEntity))
            }
        }
    }

}