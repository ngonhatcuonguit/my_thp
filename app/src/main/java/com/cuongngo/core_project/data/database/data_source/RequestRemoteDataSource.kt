package com.cuongngo.core_project.data.database.data_source

import com.cuongngo.core_project.services.THPApi
import com.cuongngo.core_project.services.network.BaseRemoteDataSource

class RequestRemoteDataSource(
    private val apiService: THPApi
) : BaseRemoteDataSource() {
    suspend fun pushRequest(
        device_code: String,
        json_data: String?,
        process_id: String?,
        version: Int?,
        id: String?
    ) = getResult {
        apiService.pushRequest(
            device_code = device_code,
            json_data = json_data,
            process_id = process_id,
            version = version,
            id = id
        )
    }

}