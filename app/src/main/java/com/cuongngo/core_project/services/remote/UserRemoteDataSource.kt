package com.cuongngo.core_project.services.remote

import com.cuongngo.core_project.services.THPApi
import com.cuongngo.core_project.services.network.BaseRemoteDataSource
import retrofit2.http.Field

class UserRemoteDataSource(private val apiService: THPApi) : BaseRemoteDataSource() {

    suspend fun login(
        user_name: String,
        password: String,
        device_code: String
    ) = getResult {
        apiService.loginWithAccount(
            username = user_name,
            password = password,
            device_code = device_code
        )
    }

    suspend fun getListGK() = getResult {
        apiService.getListGK()
    }
    suspend fun getTietMuc() = getResult {
        apiService.getTietMuc()
    }
     suspend fun updateScore(
         examinerId: Int?,
         examId: Int?,
         score: Float?,
         composingScore: Float?
     ) = getResult {
        apiService.updateScore(
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
    ) = getResult {
        apiService.activeDevice(
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
    ) = getResult {
        apiService.getListUser(isGetAll)
    }

    suspend fun getHotNew() = getResult {
        apiService.getHotNew()
    }

}