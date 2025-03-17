package com.cuongngo.my_thp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.my_thp.base.viewmodel.BaseViewModel
import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPResponse
import com.cuongngo.my_thp.response.base.AppBaseResponse
import com.cuongngo.my_thp.response.login_response.ApiResponse
import com.cuongngo.my_thp.response.login_response.LoginResponse
import com.cuongngo.my_thp.response.news.HotNewResponse
import com.cuongngo.my_thp.services.network.BaseResult
import com.cuongngo.my_thp.services.repository.UserRepository
import com.cuongngo.my_thp.ui.event_thp.model.ExamResponse
import com.cuongngo.my_thp.ui.event_thp.model.ExaminersResponse
import com.cuongngo.my_thp.ui.event_thp.model.UpdateScoreResponse
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

    private val _activeDevice = MutableLiveData<BaseResult<ApiResponse>>()
    val activeDevice: LiveData<BaseResult<ApiResponse>> = _activeDevice

    //Local

    private val _getAllUserLocal = MutableLiveData<BaseResult<List<UserTHPEntity>>>()
    val getAllUserLocal: LiveData<BaseResult<List<UserTHPEntity>>> get() = _getAllUserLocal

    private val _insertListUserToLocal = MutableLiveData<BaseResult<Unit>>()
    val insertListUserToLocal: LiveData<BaseResult<Unit>> get() = _insertListUserToLocal

    private val _checkUserTable = MutableLiveData<BaseResult<Int>>()
    val checkUserTable: LiveData<BaseResult<Int>> get() = _checkUserTable

    private val _listExaminer = MutableLiveData<BaseResult<ExaminersResponse>>()
    val listExaminer: LiveData<BaseResult<ExaminersResponse>> get() = _listExaminer

    private val _exam = MutableLiveData<BaseResult<ExamResponse>>()
    val exam: LiveData<BaseResult<ExamResponse>> get() = _exam

    private val _updateScore = MutableLiveData<BaseResult<UpdateScoreResponse>>()
    val updateScore: LiveData<BaseResult<UpdateScoreResponse>> get() = _updateScore


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
    fun getListGK() {
        _listExaminer.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _listExaminer.postValue(
                    userRepository.getListGk()
                )
            }
        }
    }
    fun getTietMuc() {
        _exam.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _exam.postValue(
                    userRepository.getTietMuc()
                )
            }
        }
    }
    fun updateScore(
        examinerId: Int?,
        examId: Int?,
        score: Double?,
        composingScore: Double?
    ) {
        _updateScore.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _updateScore.postValue(
                    userRepository.updateSore(
                        examinerId, examId, score, composingScore
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