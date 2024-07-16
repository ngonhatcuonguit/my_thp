package com.cuongngo.core_project.data.database.data_source

import com.cuongngo.core_project.data.database.AppDatabase
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity

class RequestLocalDataSource(private val database: AppDatabase) : BaseLocalDataSource(){

    suspend fun getAllRequest() = getResult {
        database.requestDao().getAllRequest()
    }
    suspend fun getRequestByName(name: String) = getResult {
        database.requestDao().getRequestByName(name)
    }
    suspend fun upsertRequest(record: RequestEntity) = getResult {
        database.requestDao().upsertRequest(record)
    }
    suspend fun insertRequest(record: RequestEntity) = getResult {
        database.requestDao().insertRequest(record)
    }
    suspend fun deleteRequest(record: RequestEntity) = getResult {
        database.requestDao().deleteRequest(record)
    }

}