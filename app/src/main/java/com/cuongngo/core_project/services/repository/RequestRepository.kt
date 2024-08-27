package com.cuongngo.core_project.services.repository

import com.cuongngo.core_project.data.database.data_source.RequestLocalDataSource
import com.cuongngo.core_project.data.database.data_source.RequestRemoteDataSource
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Body
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.ui.request_detail.PushRequestModel
import com.cuongngo.core_project.ui.request_detail.PushRequestResponse
import com.cuongngo.core_project.ui.request_detail.RequestBodyPush

class RequestRepository(
    private val requestLocalDataSource: RequestLocalDataSource,
    private val requestRemoteDataSource: RequestRemoteDataSource
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
    suspend fun updateSyncStatus(requestCode: String, is_sync: Boolean, currentTime: String) =
            requestLocalDataSource.updateSyncStatus(
                requestCode = requestCode,
                is_sync = is_sync,
                currentTime = currentTime
            )

    suspend fun getRequestNeedUpload() = requestLocalDataSource.getRequestNeedUpload()

    suspend fun deleteRequest(record: RequestEntity): BaseResult<Unit> {
        return requestLocalDataSource.deleteRequest(record)
    }

    //---remote---
    suspend fun pushRequest(
        requestBodyPush : List<RequestBodyPush>
    ): BaseResult<PushRequestResponse>{
        return requestRemoteDataSource.pushRequest(
            requestBodyPush = requestBodyPush
        )
    }

}