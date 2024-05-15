package com.cuongngo.core_project.data.database.data_source

import com.cuongngo.core_project.data.database.AppDatabase
import com.cuongngo.core_project.data.database.roomdb.entity.RecordProcessEntity

class RecordProcessLocalDataSource(
    private val database: AppDatabase
) : BaseLocalDataSource() {

    suspend fun recordProcessAllInfo() = getResult {
        database.recordProcessDao().getAllRecordProcess()
    }

    suspend fun getRecordProcessByName(name: String) = getResult {
        database.recordProcessDao().getRecordProcessByName(name = name)
    }

    suspend fun upsertRecordProcess(record: RecordProcessEntity) = getResult {
        database.recordProcessDao().upsert(record)
    }

}