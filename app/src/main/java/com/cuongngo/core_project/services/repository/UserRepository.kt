package com.cuongngo.core_project.services.repository
import com.cuongngo.core_project.response.login_response.LoginResponse
import com.cuongngo.core_project.response.news.HotNewResponse
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.remote.UserRemoteDataSource

class UserRepository(private val userRemoteDataSource: UserRemoteDataSource) {
    suspend fun login(
        user_name: String,
        password: String,
        grant_type: String
    ): BaseResult<LoginResponse>{
        return userRemoteDataSource.login(user_name = user_name, password = password, grant_type = grant_type)
    }
    suspend fun getHotNew(
    ): BaseResult<HotNewResponse>{
        return userRemoteDataSource.getHotNew()
    }
}