package com.cuongngo.core_project.services.remote
import com.cuongngo.core_project.services.THPApi
import com.cuongngo.core_project.services.network.BaseRemoteDataSource

class UserRemoteDataSource(private val apiService: THPApi): BaseRemoteDataSource() {

    suspend fun login(
        user_name: String,
        password: String,
        device_code: String
    ) = getResult {
        apiService.loginWithAccount(username = user_name, password =  password, device_code = device_code)
    }

    suspend fun getListUser(
        isGetAll: Boolean
    ) = getResult {
        apiService.getListUser(isGetAll)
    }
    suspend fun getHotNew() = getResult {
        apiService.getHotNew()
    }

}