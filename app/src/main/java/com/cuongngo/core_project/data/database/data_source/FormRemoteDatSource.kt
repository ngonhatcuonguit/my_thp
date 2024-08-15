package com.cuongngo.core_project.data.database.data_source

import com.cuongngo.core_project.services.THPApi
import com.cuongngo.core_project.services.network.BaseRemoteDataSource

class FormRemoteDatSource(
    private val apiService: THPApi
) : BaseRemoteDataSource() {
    suspend fun getListForm() = getResult {
        apiService.getListForm(isGetAll = true)
    }
}