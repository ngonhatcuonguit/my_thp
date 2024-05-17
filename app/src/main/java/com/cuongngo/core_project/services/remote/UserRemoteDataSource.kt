package com.cuongngo.core_project.services.remote
import com.cuongngo.core_project.services.THPApi
import com.cuongngo.core_project.services.network.BaseRemoteDataSource

class UserRemoteDataSource(private val apiService: THPApi): BaseRemoteDataSource() {

    suspend fun login(
        user_name: String,
        password: String,
        grant_type: String
    ) = getResult {
        apiService.loginWithAccount(username = user_name, password =  password, grant_type = grant_type)
    }
    suspend fun getHotNew() = getResult {
        apiService.getHotNew()
    }

}