package com.cuongngo.core_project.ui.test_room_db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.RecordProcessEntity
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.RecordProcessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordProcessViewModel(
    private val recordProcessRepository: RecordProcessRepository
): BaseViewModel() {

    private val _recordProcessAll =  MutableLiveData<BaseResult<List<RecordProcessEntity>>>()
    val recordProcessAll: LiveData<BaseResult<List<RecordProcessEntity>>> =  _recordProcessAll

    private val _recordProcess =  MutableLiveData<BaseResult<RecordProcessEntity>>()
    val recordProcess: LiveData<BaseResult<RecordProcessEntity>> =  _recordProcess

    private val _recordProcessId =  MutableLiveData<BaseResult<Long>>()
    val recordProcessId: LiveData<BaseResult<Long>> =  _recordProcessId


    fun getRecordProcessAll(name: String, id: Int){
        _recordProcessAll.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _recordProcessAll.postValue(recordProcessRepository.getAllRecordProcess())
            }
        }
    }
    fun getRecordProcess(name: String){
        _recordProcess.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _recordProcess.postValue(recordProcessRepository.getRecordProcessByName(
                    name = name
                ))
            }
        }
    }

    fun upsertRecordProcess(recordProcessEntity: RecordProcessEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _recordProcessId.postValue(recordProcessRepository.upsertRecordProcessInfo(recordProcessEntity))
            }
        }
    }

}