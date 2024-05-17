package com.cuongngo.core_project.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.response.login_response.LoginResponse
import com.cuongngo.core_project.response.news.HotNewResponse
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userRepository: UserRepository): BaseViewModel() {

    private val _login = MutableLiveData<BaseResult<LoginResponse>>()
    val login: LiveData<BaseResult<LoginResponse>> get() = _login

    private val _hotNew = MutableLiveData<BaseResult<HotNewResponse>>()
    val hotNew: LiveData<BaseResult<HotNewResponse>> get() = _hotNew

    fun login(
        user_name: String,
        password: String,
        grant_type: String
    ){
        _login.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _login.postValue(userRepository.login(user_name=user_name, password=password, grant_type=grant_type))
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

}