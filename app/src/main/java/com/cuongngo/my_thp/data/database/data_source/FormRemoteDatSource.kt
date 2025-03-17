package com.cuongngo.my_thp.data.database.data_source

import com.cuongngo.my_thp.services.THPApi
import com.cuongngo.my_thp.services.network.BaseRemoteDataSource

class FormRemoteDatSource(
    private val apiService: THPApi
) : BaseRemoteDataSource() {
    suspend fun getListForm(isGetAll: Boolean) = getResult {
        apiService.getListForm(isGetAll = isGetAll)
    }
}