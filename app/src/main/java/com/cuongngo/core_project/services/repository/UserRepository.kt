package com.cuongngo.core_project.services.repository
import com.cuongngo.core_project.data.database.data_source.UserLocalDataSource
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPResponse
import com.cuongngo.core_project.response.base.AppBaseResponse
import com.cuongngo.core_project.response.login_response.LoginResponse
import com.cuongngo.core_project.response.news.HotNewResponse
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.remote.UserRemoteDataSource

class UserRepository(private val userRemoteDataSource: UserRemoteDataSource, private val userLocalDataSource: UserLocalDataSource) {
    suspend fun login(
        user_name: String,
        password: String,
        device_code: String
    ): BaseResult<AppBaseResponse<LoginResponse>>{
        return userRemoteDataSource.login(user_name = user_name, password = password, device_code = device_code)
    }

    suspend fun getListUser(
        isGetAll: Boolean
    ): BaseResult<UserTHPResponse>{
        return userRemoteDataSource.getListUser(isGetAll)
    }

    suspend fun getHotNew(
    ): BaseResult<HotNewResponse>{
        return userRemoteDataSource.getHotNew()
    }


    //Local

    suspend fun getAllUserLocal():BaseResult<List<UserTHPEntity>>{
        return userLocalDataSource.getAllUser()
    }

    suspend fun getUserCountLocal():BaseResult<Int>{
        return userLocalDataSource.getUserCount()
    }

}