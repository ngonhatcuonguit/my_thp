package com.cuongngo.core_project.ui.home
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.response.news.HotNewResponse
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.FormRepository
import com.cuongngo.core_project.services.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val userRepository: UserRepository, private val formRepository: FormRepository) : BaseViewModel() {

    private val _hotNew = MutableLiveData<BaseResult<HotNewResponse>>()
    val hotNew: LiveData<BaseResult<HotNewResponse>> get() = _hotNew

    private val _allForm = MutableLiveData<BaseResult<List<FormEntity>>>()
    val allForm: LiveData<BaseResult<List<FormEntity>>> = _allForm

    var page: Int = 1
    var keyword: String? = null


    init {
        getHotNew()
        getAllForm()
    }

    fun getHotNew(
    ){
        _hotNew.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _hotNew.postValue(userRepository.getHotNew())
            }
        }

    }
    fun getAllForm() {
        _allForm.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _allForm.postValue(formRepository.getAllForm())
            }
        }
    }


}