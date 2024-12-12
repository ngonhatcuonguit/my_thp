package com.cuongngo.core_project.ui.acb_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.GdEntity
import com.cuongngo.core_project.services.network.BaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class GdViewModel(private val gdRepository: GdRepository) : BaseViewModel() {
    private val _allGD = MutableLiveData<BaseResult<List<GdEntity>>>()
    val allGD: LiveData<BaseResult<List<GdEntity>>> = _allGD

    private val _gd = MutableLiveData<BaseResult<GdEntity>>()
    val gd: LiveData<BaseResult<GdEntity>> = _gd

    private val _gdId = MutableLiveData<BaseResult<Long>>()
    val gdId: LiveData<BaseResult<Long>> = _gdId

    private val _deleteGD = MutableLiveData<BaseResult<Int>>()
    val deleteGD: LiveData<BaseResult<Int>> = _deleteGD

    private val _listFilter = MutableLiveData<BaseResult<List<GdEntity>>>()
    val listFilter: LiveData<BaseResult<List<GdEntity>>> = _listFilter


    fun getAllGD() {
        _allGD.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _allGD.postValue(gdRepository.getAllGD())
            }
        }
    }

    fun upsetGD(gd: GdEntity) {
        _gdId.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _gdId.postValue(gdRepository.upsertGD(gd))
            }
        }
    }

    fun deleteGD(gd: GdEntity) {
        _deleteGD.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _deleteGD.postValue(gdRepository.deleteGD(gd))
            }
        }
    }
    fun getTransactionsByDateRange(startDate: Calendar, endDate: Calendar) {
        _allGD.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _allGD.postValue(
                    gdRepository.getTransactionsByDateRange(
                        startDate.timeInMillis,
                        endDate.timeInMillis
                    )
                )
            }
        }
    }
    fun getLastNDaysTransactions(dayAgo: Calendar) {
        _allGD.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _allGD.postValue(
                    gdRepository.getLastNDaysTransactions(
                        dayAgo.timeInMillis
                    )
                )
            }
        }
    }


}