package com.cuongngo.my_thp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.my_thp.base.viewmodel.BaseViewModel
import com.cuongngo.my_thp.data.database.roomdb.entity.FormEntity
import com.cuongngo.my_thp.services.network.BaseResult
import com.cuongngo.my_thp.services.repository.FormRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val formRepository: FormRepository) : BaseViewModel() {

    private val _allForm = MutableLiveData<BaseResult<List<FormEntity>>>()
    val allForm: LiveData<BaseResult<List<FormEntity>>> = _allForm

    private val _upsertListFormToLocal = MutableLiveData<BaseResult<Unit>>()
    val upsertListFormToLocal: LiveData<BaseResult<Unit>> = _upsertListFormToLocal

    private val _checkCountRecord = MutableLiveData<BaseResult<Int>>()
    val checkCountRecord: LiveData<BaseResult<Int>> = _checkCountRecord

    private val _formatFormTable = MutableLiveData<BaseResult<Unit>>()
    val formatFormTable: LiveData<BaseResult<Unit>> = _formatFormTable

    fun getAllForm() {
        _allForm.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _allForm.postValue(formRepository.getAllForm())
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
    fun formatFormTable () {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _formatFormTable.postValue(formRepository.formatFormTable())
            }
        }
    }

}