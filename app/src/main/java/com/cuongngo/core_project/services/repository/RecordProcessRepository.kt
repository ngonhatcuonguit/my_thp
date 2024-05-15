package com.cuongngo.core_project.services.repository
import android.util.Log
import com.cuongngo.core_project.data.database.data_source.RecordProcessLocalDataSource
import com.cuongngo.core_project.data.database.roomdb.entity.RecordProcessEntity
import com.cuongngo.core_project.services.network.BaseResult

class RecordProcessRepository(
    private val recordProcessLocalDataSource: RecordProcessLocalDataSource
) {

    companion object {
        val TAG =  RecordProcessRepository::class.simpleName
    }

    suspend fun getAllRecordProcess() : BaseResult<List<RecordProcessEntity>>{
        Log.d(TAG, "record process infomation all")
        return recordProcessLocalDataSource.recordProcessAllInfo()
    }

    suspend fun getRecordProcessByName(name: String) : BaseResult<RecordProcessEntity> {
        Log.d(TAG, "record process infomation by name id")
        return recordProcessLocalDataSource.getRecordProcessByName(
            name = name
        )
    }

    suspend fun upsertRecordProcessInfo(recordProcessEntity: RecordProcessEntity) : BaseResult<Long> {
        return recordProcessLocalDataSource.upsertRecordProcess(recordProcessEntity)
    }

}