package com.cuongngo.core_project.data.database.data_source

import com.cuongngo.core_project.services.THPApi
import com.cuongngo.core_project.services.network.BaseRemoteDataSource
import com.cuongngo.core_project.ui.request_detail.RequestBodyPush

class RequestRemoteDataSource(
    private val apiService: THPApi
) : BaseRemoteDataSource() {
    suspend fun pushRequest(
        requestBodyPush : List<RequestBodyPush>
    ) = getResult {
        apiService.pushRequest(
            requestBodyPush = requestBodyPush
        )
    }

}