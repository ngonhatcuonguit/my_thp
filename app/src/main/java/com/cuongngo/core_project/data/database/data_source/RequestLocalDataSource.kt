package com.cuongngo.core_project.data.database.data_source

import com.cuongngo.core_project.data.database.AppDatabase
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Sheet

class RequestLocalDataSource(private val database: AppDatabase) : BaseLocalDataSource() {

    suspend fun getAllRequest() = getResult {
        database.requestDao().getAllRequest()
    }

    suspend fun getRequestByName(name: String) = getResult {
        database.requestDao().getRequestByName(name)
    }

    suspend fun upsertRequest(record: RequestEntity) = getResult {
        database.requestDao().upsertRequest(record)
    }

    suspend fun updateListSheet(requestID: Long, listSheet: List<Sheet>?, currentTime: String) =
        getResult {
            database.requestDao().updateListSheet(
                requestId = requestID,
                listSheet = listSheet,
                currentTimestamp = currentTime
            )
        }

    suspend fun insertRequest(record: RequestEntity) = getResult {
        database.requestDao().insertRequest(record)
    }

    suspend fun deleteRequest(record: RequestEntity) = getResult {
        database.requestDao().deleteRequest(record)
    }

    suspend fun getRequestByID(requestID: Long) = getResult {
        database.requestDao().getRequestByID(requestID)
    }

    suspend fun getRequestByCode(requestCode: String) = getResult {
        database.requestDao().getListRequestByCode(requestCode)
    }

}