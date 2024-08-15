package com.cuongngo.core_project.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.response.login_response.LoginResponse
import com.cuongngo.core_project.services.THPApi

abstract class AppBaseActivityMVVM<DB: ViewDataBinding, VM: BaseViewModel>: BaseActivity<DB>() {

    abstract val viewModel: VM

    open fun onCreateX(savedInstanceState: Bundle?){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
        onCreateX(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    fun saveUserData(loginResponse: LoginResponse?) {
        with(AppPreferences){
            setUserAccessToken(loginResponse?.token ?: "")
            saveUserInfo(loginResponse)
            THPApi.updateToken(loginResponse?.token ?: "")
        }
    }

    fun clearUserData(){
        with(AppPreferences){
            setUserAccessToken("")
            saveUserInfo(null)
            THPApi.updateToken("")
        }
    }

    open fun showLoading(){

    }

    open fun hideLoading(){

    }
}