package com.cuongngo.core_project.ui.home
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPResponse
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

    private val _formId = MutableLiveData<BaseResult<Long>>()
    val formId: LiveData<BaseResult<Long>> = _formId


    private val _getListUser = MutableLiveData<BaseResult<UserTHPResponse>>()
    val getListUser: LiveData<BaseResult<UserTHPResponse>> get() = _getListUser

    //Local

    private val _getAllUserLocal = MutableLiveData<BaseResult<List<UserTHPEntity>>>()
    val getAllUserLocal: LiveData<BaseResult<List<UserTHPEntity>>>get() = _getAllUserLocal

    private val _insertListUserToLocal = MutableLiveData<BaseResult<Unit>>()
    val insertListUserToLocal: LiveData<BaseResult<Unit>>get() = _insertListUserToLocal

    private val _checkUserTable = MutableLiveData<BaseResult<Int>>()
    val checkUserTable: LiveData<BaseResult<Int>>get() = _checkUserTable




    var page: Int = 1
    var keyword: String? = null


    init {
        getHotNew()
        getAllForm()
    }

    fun insertForm(formEntity: FormEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _formId.postValue(formRepository.insertForm(formEntity))
            }
        }
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

    fun getListUser(isGetAll: Boolean){
        _getListUser.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _getListUser.postValue(
                    userRepository.getListUser(isGetAll)
                )
            }
        }
    }

    //Local

    fun getAllUserLocal(){
        _getAllUserLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _getAllUserLocal.postValue(userRepository.getAllUserLocal())
            }
        }
    }
    fun addListUser(listUser: List<UserTHPEntity>){
        _insertListUserToLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _insertListUserToLocal.postValue(userRepository.addListUser(listUser))
            }
        }
    }


}