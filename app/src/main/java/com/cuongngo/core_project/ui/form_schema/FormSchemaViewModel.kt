package com.cuongngo.core_project.ui.form_schema

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormSchemaEntity
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.FormSchemaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormSchemaViewModel(private val formSchemaRepository: FormSchemaRepository): BaseViewModel() {

    private val _allFormSchema = MutableLiveData<BaseResult<List<FormSchemaEntity>>>()
    val allFormSchema: LiveData<BaseResult<List<FormSchemaEntity>>> = _allFormSchema

    private val _formSchema = MutableLiveData<BaseResult<FormSchemaEntity>>()
    val formSchema: LiveData<BaseResult<FormSchemaEntity>> = _formSchema

    private val _formSchemaId = MutableLiveData<BaseResult<Long>>()
    val formSchemaId: LiveData<BaseResult<Long>> = _formSchemaId

    fun getFormSchemaByCode(code: String) {
        _formSchema.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _formSchema.postValue(formSchemaRepository.getFormSchemaByCode(code))
            }
        }

    }

}