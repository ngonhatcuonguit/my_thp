package com.cuongngo.core_project.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPResponse
import com.cuongngo.core_project.response.base.AppBaseResponse
import com.cuongngo.core_project.response.login_response.ActiveDeviceResponse
import com.cuongngo.core_project.response.login_response.LoginResponse
import com.cuongngo.core_project.response.news.HotNewResponse
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    //Remote
    private val _login = MutableLiveData<BaseResult<AppBaseResponse<LoginResponse>>>()
    val login: LiveData<BaseResult<AppBaseResponse<LoginResponse>>> get() = _login

    private val _hotNew = MutableLiveData<BaseResult<HotNewResponse>>()
    val hotNew: LiveData<BaseResult<HotNewResponse>> get() = _hotNew

    private val _getListUser = MutableLiveData<BaseResult<UserTHPResponse>>()
    val getListUser: LiveData<BaseResult<UserTHPResponse>> get() = _getListUser

    private val _activeDevice = MutableLiveData<BaseResult<ActiveDeviceResponse>>()
    val activeDevice: LiveData<BaseResult<ActiveDeviceResponse>> = _activeDevice

    //Local

    private val _getAllUserLocal = MutableLiveData<BaseResult<List<UserTHPEntity>>>()
    val getAllUserLocal: LiveData<BaseResult<List<UserTHPEntity>>> get() = _getAllUserLocal

    private val _insertListUserToLocal = MutableLiveData<BaseResult<Unit>>()
    val insertListUserToLocal: LiveData<BaseResult<Unit>> get() = _insertListUserToLocal

    private val _checkUserTable = MutableLiveData<BaseResult<Int>>()
    val checkUserTable: LiveData<BaseResult<Int>> get() = _checkUserTable


    var loginData : LoginResponse? = null

    fun login(
        user_name: String,
        password: String,
        device_code: String
    ) {
        _login.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _login.postValue(
                    userRepository.login(
                        user_name = user_name,
                        password = password,
                        device_code = device_code
                    )
                )
            }
        }
    }

    fun activeDevice(
        device_id: String,
        manufacturer: String?,
        model: String?,
        brand: String?,
        product: String?,
        os_version: String?,
        apiLevel: String?,
        hardware: String?,
        user: String?,
        host: String?,
        display: String?,
        device: String?
    ) {
        _activeDevice.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _activeDevice.postValue(
                    userRepository.activeDevice(
                        device_id = device_id,
                        manufacturer = manufacturer,
                        model = model,
                        brand = brand,
                        product = product,
                        os_version = os_version,
                        apiLevel = apiLevel,
                        hardware = hardware,
                        user = user,
                        host = host,
                        display = display,
                        device = device
                    )
                )
            }
        }
    }

    fun getListUser(isGetAll: Boolean) {
        _getListUser.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _getListUser.postValue(
                    userRepository.getListUser(isGetAll)
                )
            }
        }
    }

    fun getHotNew(
    ) {
        _hotNew.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _hotNew.postValue(userRepository.getHotNew())
            }
        }

    }

    //Local

    fun getAllUserLocal() {
        _getAllUserLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _getAllUserLocal.postValue(userRepository.getAllUserLocal())
            }
        }
    }

    fun searchUsers(keyword: String) {
        _getAllUserLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _getAllUserLocal.postValue(userRepository.searchUsers(keyword))
            }
        }
    }

    fun getCountUserLocal() {
        _checkUserTable.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _checkUserTable.postValue(userRepository.getUserCountLocal())
            }
        }
    }

    fun addListUser(listUser: List<UserTHPEntity>) {
        _insertListUserToLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _insertListUserToLocal.postValue(userRepository.addListUser(listUser))
            }
        }
    }


}