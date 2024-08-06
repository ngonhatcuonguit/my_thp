package com.cuongngo.core_project.services.repository

import com.cuongngo.core_project.data.database.data_source.RequestLocalDataSource
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Body
import com.cuongngo.core_project.services.network.BaseResult

class RequestRepository(
    private val requestLocalDataSource: RequestLocalDataSource
) {
    companion object {
        val TAG = RequestRepository::class.simpleName
    }

    suspend fun getAllRequest(): BaseResult<List<RequestEntity>> {
        return requestLocalDataSource.getAllRequest()
    }
    suspend fun insertRequest(request: RequestEntity): BaseResult<Long> {
        return requestLocalDataSource.insertRequest(request)
    }

    suspend fun upsertRequest(request: RequestEntity): BaseResult<Long> {
        return requestLocalDataSource.upsertRequest(request)
    }

    suspend fun getRequestByID(requestID: Long): BaseResult<RequestEntity> {
        return requestLocalDataSource.getRequestByID(requestID = requestID)
    }
    suspend fun getRequestByCode(requestCode: String): BaseResult<RequestEntity> {
        return requestLocalDataSource.getRequestByCode(requestCode)
    }

    suspend fun updateListSheet(requestID: Long, listBody: List<Body>?, currentTime: String) =
        requestLocalDataSource.updateListSheet(
            requestID = requestID,
            listBody = listBody,
            currentTime = currentTime
        )

    suspend fun deleteRequest(record: RequestEntity): BaseResult<Unit> {
        return requestLocalDataSource.deleteRequest(record)
    }
}