package com.cuongngo.my_thp.services.repository

import com.cuongngo.my_thp.data.database.data_source.UserLocalDataSource
import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPResponse
import com.cuongngo.my_thp.response.base.AppBaseResponse
import com.cuongngo.my_thp.response.login_response.ApiResponse
import com.cuongngo.my_thp.response.login_response.LoginResponse
import com.cuongngo.my_thp.response.news.HotNewResponse
import com.cuongngo.my_thp.services.network.BaseResult
import com.cuongngo.my_thp.services.remote.UserRemoteDataSource
import com.cuongngo.my_thp.ui.event_thp.model.ExamResponse
import com.cuongngo.my_thp.ui.event_thp.model.ExaminersResponse
import com.cuongngo.my_thp.ui.event_thp.model.UpdateScoreResponse

class UserRepository(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) {
    suspend fun login(
        user_name: String,
        password: String,
        device_code: String
    ): BaseResult<AppBaseResponse<LoginResponse>> {
        return userRemoteDataSource.login(
            user_name = user_name,
            password = password,
            device_code = device_code
        )
    }

    suspend fun getListGk(): BaseResult<ExaminersResponse> {
        return userRemoteDataSource.getListGK()
    }
    suspend fun getTietMuc(): BaseResult<ExamResponse> {
        return userRemoteDataSource.getTietMuc()
    }
    suspend fun updateSore(
        examinerId: Int?,
        examId: Int?,
        score: Double?,
        composingScore: Double?
    ): BaseResult<UpdateScoreResponse> {
        return userRemoteDataSource.updateScore(
            examinerId, examId, score, composingScore
        )
    }



    suspend fun activeDevice(
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
    ): BaseResult<ApiResponse> {
        return userRemoteDataSource.activeDevice(
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
    }

    suspend fun getListUser(
        isGetAll: Boolean
    ): BaseResult<UserTHPResponse> {
        return userRemoteDataSource.getListUser(isGetAll)
    }

    suspend fun getHotNew(
    ): BaseResult<HotNewResponse> {
        return userRemoteDataSource.getHotNew()
    }


    //Local

    suspend fun getAllUserLocal(): BaseResult<List<UserTHPEntity>> {
        return userLocalDataSource.getAllUser()
    }

    suspend fun searchUsers(keyword: String): BaseResult<List<UserTHPEntity>> {
        return userLocalDataSource.searchUsers(keyword)
    }

    suspend fun getUserCountLocal(): BaseResult<Int> {
        return userLocalDataSource.getUserCount()
    }

    suspend fun addListUser(listUser: List<UserTHPEntity>): BaseResult<Unit> {
        return userLocalDataSource.upsertListUser(listUser)
    }

    suspend fun formatUserTable(): BaseResult<Unit> {
        return userLocalDataSource.formatUserTable()
    }

}