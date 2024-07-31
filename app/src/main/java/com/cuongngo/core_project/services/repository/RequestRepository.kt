package com.cuongngo.core_project.services.repository

import com.cuongngo.core_project.data.database.data_source.RequestLocalDataSource
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Sheet

class RequestRepository(
    private val requestLocalDataSource: RequestLocalDataSource
) {
    companion object {
        val TAG = RequestRepository::class.simpleName
    }

    suspend fun getAllFormSchema() = requestLocalDataSource.getAllRequest()
    suspend fun getRequestByName(name: String) =
        requestLocalDataSource.getRequestByName(name)
    suspend fun upsertRequest(request: RequestEntity) =
        requestLocalDataSource.upsertRequest(request)
    suspend fun updateListSheet(requestID: Long, listSheet: List<Sheet>?, currentTime: String) =
        requestLocalDataSource.updateListSheet(requestID = requestID, listSheet = listSheet, currentTime = currentTime)

    suspend fun insertRequest(request: RequestEntity) =
        requestLocalDataSource.insertRequest(request)

    suspend fun deleteRequest(request: RequestEntity) =
        requestLocalDataSource.deleteRequest(request)

}