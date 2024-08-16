package com.cuongngo.core_project.ui.sync_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.FormResponse
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPResponse
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.FormRepository
import com.cuongngo.core_project.services.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SyncDataViewModel(
    private val formRepository: FormRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _allForm = MutableLiveData<BaseResult<List<FormEntity>>>()
    val allForm: LiveData<BaseResult<List<FormEntity>>> = _allForm

    private val _upsertListFormToLocal = MutableLiveData<BaseResult<Unit>>()
    val upsertListFormToLocal: LiveData<BaseResult<Unit>> = _upsertListFormToLocal

    private val _checkCountRecord = MutableLiveData<BaseResult<Int>>()
    val checkCountRecord: LiveData<BaseResult<Int>> = _checkCountRecord

    private val _formatFormTable = MutableLiveData<BaseResult<Unit>>()
    val formatFormTable: LiveData<BaseResult<Unit>> = _formatFormTable


    //user table

    private val _getAllUserLocal = MutableLiveData<BaseResult<List<UserTHPEntity>>>()
    val getAllUserLocal: LiveData<BaseResult<List<UserTHPEntity>>>get() = _getAllUserLocal

    private val _upsertListUserToLocal = MutableLiveData<BaseResult<Unit>>()
    val upsertListUserToLocal: LiveData<BaseResult<Unit>>get() = _upsertListUserToLocal

    private val _checkUserTable = MutableLiveData<BaseResult<Int>>()
    val checkUserTable: LiveData<BaseResult<Int>>get() = _checkUserTable

    private val _formatUserTable = MutableLiveData<BaseResult<Unit>>()
    val formatUserTable: LiveData<BaseResult<Unit>> = _formatUserTable


    //------remote-------

    private val _getListUser = MutableLiveData<BaseResult<UserTHPResponse>>()
    val getListUser: LiveData<BaseResult<UserTHPResponse>> get() = _getListUser

    private val _listFormRemote = MutableLiveData<BaseResult<FormResponse>>()
    val listFormRemote: LiveData<BaseResult<FormResponse>> = _listFormRemote


    var listGetFormRemote : List<FormEntity>? = emptyList()
    var listGetUserRemote : List<UserTHPEntity>? = emptyList()

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

    //---------user-------
    fun formatUserTable () {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _formatUserTable.postValue(userRepository.formatUserTable())
            }
        }
    }

    fun getAllUserLocal(){
        _getAllUserLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _getAllUserLocal.postValue(userRepository.getAllUserLocal())
            }
        }
    }

    fun getCountUserLocal(){
        _checkUserTable.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _checkUserTable.postValue(userRepository.getUserCountLocal())
            }
        }
    }
    fun addListUser(listUser: List<UserTHPEntity>){
        _upsertListUserToLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _upsertListUserToLocal.postValue(userRepository.addListUser(listUser))
            }
        }
    }


    //------get remote------
    fun getAllUserRemote(isGetAll: Boolean){
        _getListUser.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _getListUser.postValue(
                    userRepository.getListUser(isGetAll)
                )
            }
        }
    }

    fun getAllFormRemote() {
        _listFormRemote.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _listFormRemote.postValue(formRepository.getListForm())
            }
        }
    }

}