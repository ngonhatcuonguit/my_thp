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

class GdViewModel(private val gdRepository: GdRepository) : BaseViewModel() {
    private val _allGD = MutableLiveData<BaseResult<List<GdEntity>>>()
    val allGD: LiveData<BaseResult<List<GdEntity>>> = _allGD

    private val _gd = MutableLiveData<BaseResult<GdEntity>>()
    val gd: LiveData<BaseResult<GdEntity>> = _gd

    fun getAllGD() {
        _allGD.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _allGD.postValue(gdRepository.getAllGD())
            }
        }
    }
}